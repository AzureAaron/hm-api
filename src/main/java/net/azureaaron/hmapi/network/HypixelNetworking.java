package net.azureaaron.hmapi.network;

import java.util.stream.Collectors;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.azureaaron.hmapi.network.packet.c2s.PartyInfoC2SPacket;
import net.azureaaron.hmapi.network.packet.c2s.PlayerInfoC2SPacket;
import net.azureaaron.hmapi.network.packet.c2s.RegisterC2SPacket;
import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket;
import net.azureaaron.hmapi.network.packet.v1.s2c.LocationUpdateS2CPacket;
import net.azureaaron.hmapi.utils.PacketSendResult;
import net.azureaaron.hmapi.utils.Utils;
import net.minecraft.util.Util;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Public interface for interacting with the networking component (sending packets) of the Mod API.
 * 
 * @implNote There is a global cooldown of 1 second between trying send a specific packet type. Additionally, all packet send methods should be called from the {@code Render Thread}.
 */
public class HypixelNetworking {
	private static final Object2ObjectMap<CustomPacketPayload.Type<HypixelS2CPacket>, IntList> VALID_EVENTS = Util.make(new Object2ObjectOpenHashMap<>(), map -> map.put(LocationUpdateS2CPacket.ID, Util.make(new IntArrayList(), list -> list.add(1))));
	static final Object2IntMap<CustomPacketPayload.Type<HypixelS2CPacket>> REGISTERED_EVENTS = new Object2IntOpenHashMap<>();

	private HypixelNetworking() {}

	/**
	 * @param version must be 2
	 * 
	 * @throws {@link UnsupportedOperationException} if the player isn't connected to Hypixel or if the {@code version} is unsupported.
	 */
	public static PacketSendResult sendPartyInfoC2SPacket(int version) {
		Utils.requireIsOnHypixel();

		return HypixelNetworkingImpl.sendPacket(new PartyInfoC2SPacket(Utils.requireInRange(version, 2, 2)), false);
	}

	/**
	 * @param version must be 1
	 * 
	 * @throws {@link UnsupportedOperationException} if the player isn't connected to Hypixel or if the {@code version} is unsupported.
	 */
	public static PacketSendResult sendPlayerInfoC2SPacket(int version) {
		Utils.requireIsOnHypixel();

		return HypixelNetworkingImpl.sendPacket(new PlayerInfoC2SPacket(Utils.requireInRange(version, 1, 1)), false);
	}

	/**
	 * Allows for registering to the Mod API's event packets. When requesting to register to an event make sure to use the appropriate packet id from the correct class.<br><br>
	 * 
	 * Note, HM API automatically sends an event registration packet to Hypixel when the {@link net.azureaaron.hmapi.network.packet.s2c.HelloS2CPacket}
	 * is received. This means you can register to Mod API events in your mod's initializer and the library will automatically take care of the
	 * registration when the player logs into Hypixel.
	 * 
	 * @throws {@link UnsupportedOperationException} if any requested event is invalid.
	 * 
	 * @implNote If two mods request the same packet, albeit with different versions the mod will only subscribe to the one with the greatest version,
	 * to avoid any unexpected behavior it is important to keep up to date with this library and the Mod API at large. Note that this is a limitation of the Mod API
	 * and not the library itself.
	 */
	public static void registerToEvents(Object2IntMap<CustomPacketPayload.Type<HypixelS2CPacket>> requestedEvents) {
		Object2IntMap<CustomPacketPayload.Type<HypixelS2CPacket>> newEventRegistrations = new Object2IntOpenHashMap<>();

		for (Object2IntMap.Entry<CustomPacketPayload.Type<HypixelS2CPacket>> entry : requestedEvents.object2IntEntrySet()) {
			CustomPacketPayload.Type<HypixelS2CPacket> id = entry.getKey();
			int version = entry.getIntValue();

			//Require that the requested event is actually an event and that the version is supported
			if (!VALID_EVENTS.containsKey(id) || !VALID_EVENTS.get(id).contains(version)) {
				throw new UnsupportedOperationException(id.toString() + " is either not a valid event or is an unsupported version of an event!");
			}

			//If the event doesn't have any registrations or if the requested version is higher than the one currently registered (if any)
			if (!REGISTERED_EVENTS.containsKey(id) || version > REGISTERED_EVENTS.getInt(id)) {
				newEventRegistrations.put(id, version);
			}
		}

		if (!newEventRegistrations.isEmpty()) {
			//Merge maps because the server stores the map of packets that we send as the ones the player wants
			//So if we leave out the currently registered events then we unregister from them which is NOT what we want
			REGISTERED_EVENTS.putAll(newEventRegistrations);

			//Only register when we're on Hypixel to allow for events to be registered at mod init
			if (Utils.isOnHypixel()) {
				Object2IntMap<Identifier> packetsToRegisterFor = REGISTERED_EVENTS.object2IntEntrySet().stream()
						.collect(Collectors.toMap(e -> e.getKey().id(), Object2IntMap.Entry::getIntValue, (a, b) -> a > b ? a : b, Object2IntOpenHashMap::new));

				HypixelNetworkingImpl.sendPacket(new RegisterC2SPacket(1, packetsToRegisterFor), true);
			}
		}
	}
}
