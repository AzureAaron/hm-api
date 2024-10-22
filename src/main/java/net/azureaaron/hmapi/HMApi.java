package net.azureaaron.hmapi;

import org.slf4j.Logger;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.azureaaron.hmapi.events.HypixelPacketEvents;
import net.azureaaron.hmapi.network.HypixelNetworking;
import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket;
import net.azureaaron.hmapi.network.packet.v1.s2c.LocationUpdateS2CPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class HMApi implements ClientModInitializer {
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final boolean DEBUG_ENABLED = Boolean.parseBoolean(System.getProperty("hmapi.debug", "false")) || FabricLoader.getInstance().isDevelopmentEnvironment();

	private static boolean sendPacketsInChat;

	/**
	 * This class is not part of the API.
	 */
	@Deprecated(forRemoval = true)
	public HMApi () {}

	@Override
	public void onInitializeClient() {		
		if (DEBUG_ENABLED) {
			ClientCommandRegistrationCallback.EVENT.register(HMApi::registerCommands);
			HypixelPacketEvents.PARTY_INFO.register(HMApi::logPacket);
			HypixelPacketEvents.PLAYER_INFO.register(HMApi::logPacket);
			HypixelPacketEvents.HELLO.register(HMApi::logPacket);
			HypixelPacketEvents.LOCATION_UPDATE.register(HMApi::logPacket);
		}
	}

	private static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		dispatcher.register(ClientCommandManager.literal("hmapi")
				.then(ClientCommandManager.literal("sendPacket")
						.then(ClientCommandManager.literal("partyInfo2")
								.executes(context -> {
									HypixelNetworking.sendPartyInfoC2SPacket(2);

									return Command.SINGLE_SUCCESS;
								}))
						.then(ClientCommandManager.literal("playerInfo")
								.executes(context -> {
									HypixelNetworking.sendPlayerInfoC2SPacket(1);

									return Command.SINGLE_SUCCESS;
								}))
						.then(ClientCommandManager.literal("register4LocationUpdates")
								.executes(context -> {
									HypixelNetworking.registerToEvents(Util.make(new Object2IntOpenHashMap<>(), map -> {
										map.put(LocationUpdateS2CPacket.ID, 1);
									}));

									return Command.SINGLE_SUCCESS;
								}))
						)
				.then(ClientCommandManager.literal("toggleSendPacketsInChat")
						.executes(context -> {
							sendPacketsInChat = true;

							return Command.SINGLE_SUCCESS;
						})));
	}

	private static void logPacket(HypixelS2CPacket packet) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;

		LOGGER.info("[HM API] Received Packet: {}", packet);

		if (player != null && sendPacketsInChat) {
			player.sendMessage(Text.of(packet.toString()), false);
		}
	}
}