package net.azureaaron.hmapi.network.packet.s2c;

import java.util.function.Function;

import net.azureaaron.hmapi.data.error.ErrorReason;
import net.azureaaron.hmapi.data.error.ModApiErrorReason;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * This packet is used to communicate when Hypixel returned an error instead of a successful packet.
 * 
 * @param id the id of the packet for which this error was created for
 * @param reason the reason behind the error
 * 
 * @see {@link ModApiErrorReason}
 * @see {@link net.azureaaron.hmapi.data.error.InternalErrorReason InternalErrorReason}
 */
public record ErrorS2CPacket(CustomPacketPayload.Type<HypixelS2CPacket> id, ErrorReason reason) implements HypixelS2CPacket {
	public static final Function<CustomPacketPayload.Type<HypixelS2CPacket>, StreamCodec<RegistryFriendlyByteBuf, ErrorS2CPacket>> PACKET_CODEC = payloadId -> StreamCodec.composite(ByteBufCodecs.idMapper(ModApiErrorReason::tryResolveReason, ErrorReason::id), ErrorS2CPacket::reason,
			errorId -> new ErrorS2CPacket(payloadId, errorId));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return this.id;
	}
}
