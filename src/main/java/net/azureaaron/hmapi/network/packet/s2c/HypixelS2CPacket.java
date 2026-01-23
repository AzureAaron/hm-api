package net.azureaaron.hmapi.network.packet.s2c;

import org.jetbrains.annotations.ApiStatus;

import net.azureaaron.hmapi.network.packet.HypixelPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Used to establish a basic record-compatible inheritance model for each S2C packet. In order to get the original type you
 * must use pattern matching or {@code instanceof}.
 */
@ApiStatus.NonExtendable
public interface HypixelS2CPacket extends HypixelPacket {
	/**
	 * Catch all instance for unknown S2C packets, event handlers will never receive this.
	 */
	@ApiStatus.Internal
	HypixelS2CPacket NOP = new HypixelS2CPacket() {

		@Override
		public Type<? extends CustomPacketPayload> type() {
			//This needs to return something non null that is a packet channel we're registered to
			return new CustomPacketPayload.Type<CustomPacketPayload>(Identifier.fromNamespaceAndPath("hypixel", "location"));
		}
	};
}
