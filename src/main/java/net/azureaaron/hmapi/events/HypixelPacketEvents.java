package net.azureaaron.hmapi.events;

import net.azureaaron.hmapi.network.packet.s2c.ErrorS2CPacket;
import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Contains callback events for each packet from the Hypixel Mod Api. All events are fired regardless of whether the received packet was
 * successful or not (e.g. there was no error returned by Hypixel) so the correct approach is to use pattern matching
 * along with record patterns when registering to each event.<br><br>
 * 
 * Additionally, these events are globally shared between all mods that are registered to them.
 * 
 * @implSpec No guarantees are provided that future versions of a packet will invoke a new or dedicated event so take caution of that when
 * implementing your switch statements.
 */
public class HypixelPacketEvents {
	/**
	 * The packed passed here will either be a {@code PartyInfoS2CPacket} or an {@link ErrorS2CPacket}.
	 */
	public static final Event<PacketCallback> PARTY_INFO = createEvent();

	/**
	 * The packed passed here will either be a {@code PlayerInfoS2CPacket} or an {@link ErrorS2CPacket}.
	 */
	public static final Event<PacketCallback> PLAYER_INFO = createEvent();

	/**
	 * Upon logging into the server, Hypixel sends a Hello packet.
	 * 
	 * The packed passed here will be a {@code HelloS2CPacket} or an {@link ErrorS2CPacket}.
	 */
	public static final Event<PacketCallback> HELLO = createEvent();

	/**
	 * The packed passed here will either be a {@code LocationUpdateS2CPacket} or an {@link ErrorS2CPacket}.
	 */
	public static final Event<PacketCallback> LOCATION_UPDATE = createEvent();

	private HypixelPacketEvents() {}

	private static Event<PacketCallback> createEvent() {
		return EventFactory.createArrayBacked(PacketCallback.class, callbacks -> packet -> {
			for (PacketCallback callback : callbacks) {
				callback.onPacket(packet);
			}
		});
	}

	@FunctionalInterface
	public interface PacketCallback {
		void onPacket(HypixelS2CPacket packet);
	}
}
