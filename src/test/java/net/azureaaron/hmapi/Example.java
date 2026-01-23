package net.azureaaron.hmapi;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.azureaaron.hmapi.events.HypixelPacketEvents;
import net.azureaaron.hmapi.network.HypixelNetworking;
import net.azureaaron.hmapi.network.packet.s2c.ErrorS2CPacket;
import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket;
import net.azureaaron.hmapi.network.packet.v1.s2c.LocationUpdateS2CPacket;
import net.minecraft.Util;

public class Example {

	public static void init() {
		HypixelPacketEvents.LOCATION_UPDATE.register(Example::handlePacket);

		//HM API will automatically send the event registration packet upon logging into Hypixel.
		HypixelNetworking.registerToEvents(Util.make(new Object2IntOpenHashMap<>(), map -> {
			map.put(LocationUpdateS2CPacket.ID, 1);
		}));
	}

	/**
	 * This example demonstrates the ideal way to handle received packets which is by using switch pattern matching and record patterns.
	 * 
	 * You can also handle more than one packet type by adding new switch cases as demonstrated.
	 */
	private static void handlePacket(HypixelS2CPacket packet) {
		switch (packet) {
			case LocationUpdateS2CPacket(var serverName, var serverType, var lobbyName, var mode, var map) -> {
				//Do something with the updated location data
			}

			//If you merge multiple handlers together, its recommended to add a guard case for the packet id, so that you can differentiate
			//between the sources of errors
			case ErrorS2CPacket(var id, var errorReason) -> {
				//Handle errors if you want
			}

			default -> {
				//Its highly recommended to do nothing here, future versions of the location packet for instance may also invoke this same event
				//which could cause exceptions to be thrown if thats what you do here. The same could hold true for any packet/packet event.
			}
		}
	}
}
