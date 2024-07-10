package net.azureaaron.hmapi.network;

import java.util.Objects;
import java.util.stream.Collectors;

import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.azureaaron.hmapi.events.HypixelPacketEvents;
import net.azureaaron.hmapi.network.packet.c2s.HypixelC2SPacket;
import net.azureaaron.hmapi.network.packet.c2s.RegisterC2SPacket;
import net.azureaaron.hmapi.network.packet.s2c.ErrorS2CPacket;
import net.azureaaron.hmapi.network.packet.s2c.HelloS2CPacket;
import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket;
import net.azureaaron.hmapi.network.packet.v1.s2c.LocationUpdateS2CPacket;
import net.azureaaron.hmapi.network.packet.v1.s2c.PlayerInfoS2CPacket;
import net.azureaaron.hmapi.network.packet.v2.s2c.PartyInfoS2CPacket;
import net.azureaaron.hmapi.utils.PacketSendResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;

@ApiStatus.Internal
public class HypixelNetworkingImpl {
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
	private static final long COOLDOWN = 1000L;
	private static final Object2LongMap<CustomPayload.Id<?>> COOLDOWNS = Object2LongMaps.synchronize(new Object2LongOpenHashMap<>());

	static <T extends HypixelC2SPacket> PacketSendResult sendPacket(T payload, boolean bypassCooldown) {
		if ((System.currentTimeMillis() + COOLDOWN > COOLDOWNS.computeIfAbsent(payload.getId(), _id -> 0L)) || bypassCooldown) {
			Objects.requireNonNull(CLIENT.getNetworkHandler(), "Cannot send packet while not in game!").sendPacket(new CustomPayloadC2SPacket(payload));
			COOLDOWNS.put(payload.getId(), System.currentTimeMillis());

			return PacketSendResult.success();
		}

		return PacketSendResult.onCooldown(COOLDOWNS.getLong(payload.getId()) - System.currentTimeMillis());
	}

	private static void sendInitialEventRegistrations() {
		if (!HypixelNetworking.REGISTERED_EVENTS.isEmpty()) {
			Object2IntMap<Identifier> packetsToRegisterFor = HypixelNetworking.REGISTERED_EVENTS.object2IntEntrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey().id(), Object2IntMap.Entry::getIntValue, (a, b) -> a > b ? a : b, Object2IntOpenHashMap::new));

			sendPacket(new RegisterC2SPacket(1, packetsToRegisterFor), true);
		}
	}

	public static <T extends HypixelS2CPacket> void handlePayload(T payload) {
		CLIENT.execute(() -> {
			switch (payload) {
				case PartyInfoS2CPacket packet -> HypixelPacketEvents.PARTY_INFO.invoker().onPacket(packet);
				case PlayerInfoS2CPacket packet -> HypixelPacketEvents.PLAYER_INFO.invoker().onPacket(packet);
				case HelloS2CPacket packet -> {
					sendInitialEventRegistrations();
					HypixelPacketEvents.HELLO.invoker().onPacket(packet);
					
				}
				case LocationUpdateS2CPacket packet -> HypixelPacketEvents.LOCATION_UPDATE.invoker().onPacket(packet);

				//Error cases
				case ErrorS2CPacket(var id, var err) when id.equals(PartyInfoS2CPacket.ID) -> HypixelPacketEvents.PARTY_INFO.invoker().onPacket(payload);
				case ErrorS2CPacket(var id, var err) when id.equals(PlayerInfoS2CPacket.ID) -> HypixelPacketEvents.PLAYER_INFO.invoker().onPacket(payload);
				case ErrorS2CPacket(var id, var err) when id.equals(HelloS2CPacket.ID) -> HypixelPacketEvents.HELLO.invoker().onPacket(payload);
				case ErrorS2CPacket(var id, var err) when id.equals(LocationUpdateS2CPacket.ID) -> HypixelPacketEvents.LOCATION_UPDATE.invoker().onPacket(payload);

				//When the packet encountered was unknown, likely due to encountering an unimplemented version of a packet
				case HypixelS2CPacket packet when packet == HypixelS2CPacket.NOP -> LOGGER.warn("[HM API] Recevied an unknown or unexpected packet!");

				case null, default -> {}
			}
		});
	}
}
