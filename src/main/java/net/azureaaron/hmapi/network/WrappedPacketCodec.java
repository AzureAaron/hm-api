package net.azureaaron.hmapi.network;

import org.jetbrains.annotations.ApiStatus;

import net.azureaaron.hmapi.network.packet.HypixelPacket;
import net.azureaaron.hmapi.network.packet.c2s.HypixelC2SPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@ApiStatus.Internal
public record WrappedPacketCodec(StreamCodec<FriendlyByteBuf, CustomPacketPayload> wrappedPacketCodec, PacketFlow direction) implements StreamCodec<FriendlyByteBuf, CustomPacketPayload> {

	@Override
	public CustomPacketPayload decode(FriendlyByteBuf buf) {
		FriendlyByteBuf copiedBuf = new FriendlyByteBuf(buf.slice());

		CustomPacketPayload original = this.wrappedPacketCodec.decode(buf);
		CustomPacketPayload hijacked = HypixelCustomPayloadCodecs.get4Direction(this.direction).decode(copiedBuf);

		return hijacked instanceof HypixelPacket.Unknown ? original : new HijackedCustomPayload(original, hijacked);
	}

	@Override
	public void encode(FriendlyByteBuf buf, CustomPacketPayload payload) {
		if (payload instanceof HypixelC2SPacket) {
			HypixelCustomPayloadCodecs.get4Direction(this.direction).encode(buf, payload);
		} else {
			this.wrappedPacketCodec.encode(buf, payload);
		}
	}
}
