package net.azureaaron.hmapi.network.packet.c2s;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record PlayerInfoC2SPacket(int version) implements HypixelC2SPacket {
	public static final CustomPayload.Id<PlayerInfoC2SPacket> ID = new CustomPayload.Id<>(Identifier.of("hypixel", "player_info"));
	public static final PacketCodec<RegistryByteBuf, PlayerInfoC2SPacket> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, PlayerInfoC2SPacket::version, PlayerInfoC2SPacket::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
