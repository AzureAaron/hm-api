package net.azureaaron.hmapi.network.packet.s2c;

import java.util.function.Function;

import net.azureaaron.hmapi.data.error.ErrorReason;
import net.azureaaron.hmapi.data.error.ModApiErrorReason;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

/**
 * @param id the id of the packet for which this error was created for.
 */
public record ErrorS2CPacket(CustomPayload.Id<HypixelS2CPacket> id, ErrorReason reason) implements HypixelS2CPacket {
	public static final Function<CustomPayload.Id<HypixelS2CPacket>, PacketCodec<RegistryByteBuf, ErrorS2CPacket>> PACKET_CODEC = payloadId -> PacketCodec.tuple(PacketCodecs.indexed(ModApiErrorReason::tryResolveReason, ErrorReason::id), ErrorS2CPacket::reason,
			errorId -> new ErrorS2CPacket(payloadId, errorId));

	@Override
	public Id<? extends CustomPayload> getId() {
		return this.id;
	}
}
