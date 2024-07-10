package net.azureaaron.hmapi.network.packet.v1.s2c;

import java.util.Optional;

import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record LocationUpdateS2CPacket(String serverName, Optional<String> serverType, Optional<String> lobbyName, Optional<String> mode, Optional<String> map) implements HypixelS2CPacket {
	public static final CustomPayload.Id<HypixelS2CPacket> ID = new CustomPayload.Id<>(Identifier.of("hyevent", "location"));
	public static final PacketCodec<RegistryByteBuf, LocationUpdateS2CPacket> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.STRING, LocationUpdateS2CPacket::serverName,
			PacketCodecs.optional(PacketCodecs.STRING), LocationUpdateS2CPacket::serverType,
			PacketCodecs.optional(PacketCodecs.STRING), LocationUpdateS2CPacket::lobbyName,
			PacketCodecs.optional(PacketCodecs.STRING), LocationUpdateS2CPacket::mode,
			PacketCodecs.optional(PacketCodecs.STRING), LocationUpdateS2CPacket::map, LocationUpdateS2CPacket::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
