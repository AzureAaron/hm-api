package net.azureaaron.hmapi.network.packet.v1.s2c;

import java.util.Optional;

import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * When subscribed to this event packet it's sent every time the player swaps servers and it gives information on their new location on Hypixel.
 * 
 * @param serverName the name of the server (e.g. {@code mini88A})
 * @param serverType the type of server the player has connected to if available such as {@code SKYBLOCK}
 * @param lobbyName  the current lobby's name, this value is only present when the player is in a lobby (e.g. {@code prototypelobby2})
 * @param mode       the current gamemode's name if present such as {@code dynamic} for SkyBlock Private Islands
 * @param map        the current server's map if available (e.g. {@code Dungeon} for SkyBlock Dungeons)
 * 
 * @see <a href="https://github.com/HypixelDev/HypixelData/tree/master/src/main/java/net/hypixel/data/type">Hypixel Data</a> ServerType varient enum constant names for possible {@link #serverType} values.
 */
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
