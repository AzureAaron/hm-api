package net.azureaaron.hmapi.network.packet.v1.s2c;

import java.util.Optional;

import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

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
	public static final CustomPacketPayload.Type<HypixelS2CPacket> ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("hyevent", "location"));
	public static final StreamCodec<RegistryFriendlyByteBuf, LocationUpdateS2CPacket> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, LocationUpdateS2CPacket::serverName,
			ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), LocationUpdateS2CPacket::serverType,
			ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), LocationUpdateS2CPacket::lobbyName,
			ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), LocationUpdateS2CPacket::mode,
			ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), LocationUpdateS2CPacket::map, LocationUpdateS2CPacket::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
