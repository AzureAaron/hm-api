package net.azureaaron.hmapi.network;

import org.jetbrains.annotations.ApiStatus;

import net.azureaaron.hmapi.network.packet.HypixelPacket;
import net.azureaaron.hmapi.network.packet.c2s.HypixelC2SPacket;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

@ApiStatus.Internal
public record WrappedPacketCodec(PacketCodec<PacketByteBuf, CustomPayload> wrappedPacketCodec, NetworkSide direction) implements PacketCodec<PacketByteBuf, CustomPayload> {

	@Override
	public CustomPayload decode(PacketByteBuf buf) {
		PacketByteBuf copiedBuf = new PacketByteBuf(buf.slice());

		CustomPayload original = this.wrappedPacketCodec.decode(buf);
		CustomPayload hijacked = HypixelCustomPayloadCodecs.get4Direction(this.direction).decode(copiedBuf);

		return hijacked instanceof HypixelPacket.Unknown ? original : new HijackedCustomPayload(original, hijacked);
	}

	@Override
	public void encode(PacketByteBuf buf, CustomPayload payload) {
		if (payload instanceof HypixelC2SPacket) {
			HypixelCustomPayloadCodecs.get4Direction(this.direction).encode(buf, payload);
		} else {
			this.wrappedPacketCodec.encode(buf, payload);
		}
	}
}
