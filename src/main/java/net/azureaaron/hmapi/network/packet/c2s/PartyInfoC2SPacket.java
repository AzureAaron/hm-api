package net.azureaaron.hmapi.network.packet.c2s;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PartyInfoC2SPacket(int version) implements HypixelC2SPacket {
	public static final CustomPacketPayload.Type<PartyInfoC2SPacket> ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("hypixel", "party_info"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PartyInfoC2SPacket> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, PartyInfoC2SPacket::version, PartyInfoC2SPacket::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
