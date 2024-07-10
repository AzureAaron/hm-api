package net.azureaaron.hmapi.network.packet.c2s;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record PartyInfoC2SPacket(int version) implements HypixelC2SPacket {
	public static final CustomPayload.Id<PartyInfoC2SPacket> ID = new CustomPayload.Id<>(Identifier.of("hypixel", "party_info"));
	public static final PacketCodec<RegistryByteBuf, PartyInfoC2SPacket> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, PartyInfoC2SPacket::version, PartyInfoC2SPacket::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
