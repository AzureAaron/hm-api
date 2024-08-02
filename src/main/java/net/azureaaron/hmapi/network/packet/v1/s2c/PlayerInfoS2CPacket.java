package net.azureaaron.hmapi.network.packet.v1.s2c;

import java.util.Optional;

import net.azureaaron.hmapi.data.rank.MonthlyPackageRank;
import net.azureaaron.hmapi.data.rank.PackageRank;
import net.azureaaron.hmapi.data.rank.PlayerRank;
import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * This packet gives information about the player's rank and how its displayed.
 * 
 * @param playerRank         the player's base rank type
 * @param packageRank        the player's paid rank (if applicable) (e.g. MVP++)
 * @param monthlyPackageRank the player's monthly subscription rank, currently only MVP++ applies to this
 * @param prefix             the player's rank prefix override (Note: this may contain formatting codes)
 */
public record PlayerInfoS2CPacket(PlayerRank playerRank, PackageRank packageRank, MonthlyPackageRank monthlyPackageRank, Optional<String> prefix) implements HypixelS2CPacket {
	public static final CustomPayload.Id<HypixelS2CPacket> ID = new CustomPayload.Id<>(Identifier.of("hypixel", "player_info"));
	public static final PacketCodec<RegistryByteBuf, PlayerInfoS2CPacket> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.indexed(PlayerRank.BY_ID, PlayerRank::id), PlayerInfoS2CPacket::playerRank,
			PacketCodecs.indexed(PackageRank.BY_ID, PackageRank::id), PlayerInfoS2CPacket::packageRank,
			PacketCodecs.indexed(MonthlyPackageRank.BY_ID, MonthlyPackageRank::id), PlayerInfoS2CPacket::monthlyPackageRank,
			PacketCodecs.optional(PacketCodecs.STRING), PlayerInfoS2CPacket::prefix,
			PlayerInfoS2CPacket::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
