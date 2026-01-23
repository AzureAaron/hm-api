package net.azureaaron.hmapi.network.packet.c2s;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PlayerInfoC2SPacket(int version) implements HypixelC2SPacket {
	public static final CustomPacketPayload.Type<PlayerInfoC2SPacket> ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("hypixel", "player_info"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PlayerInfoC2SPacket> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, PlayerInfoC2SPacket::version, PlayerInfoC2SPacket::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
