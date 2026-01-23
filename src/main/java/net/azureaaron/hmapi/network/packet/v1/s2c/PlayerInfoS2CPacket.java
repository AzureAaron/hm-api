package net.azureaaron.hmapi.network.packet.v1.s2c;

import java.util.Optional;

import net.azureaaron.hmapi.data.rank.MonthlyPackageRank;
import net.azureaaron.hmapi.data.rank.PackageRank;
import net.azureaaron.hmapi.data.rank.PlayerRank;
import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * This packet gives information about the player's rank and how its displayed.
 * 
 * @param playerRank         the player's base rank type
 * @param packageRank        the player's paid rank (if applicable) (e.g. MVP++)
 * @param monthlyPackageRank the player's monthly subscription rank, currently only MVP++ applies to this
 * @param prefix             the player's rank prefix override (Note: this may contain formatting codes)
 */
public record PlayerInfoS2CPacket(PlayerRank playerRank, PackageRank packageRank, MonthlyPackageRank monthlyPackageRank, Optional<String> prefix) implements HypixelS2CPacket {
	public static final CustomPacketPayload.Type<HypixelS2CPacket> ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("hypixel", "player_info"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PlayerInfoS2CPacket> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.idMapper(PlayerRank.BY_ID, PlayerRank::id), PlayerInfoS2CPacket::playerRank,
			ByteBufCodecs.idMapper(PackageRank.BY_ID, PackageRank::id), PlayerInfoS2CPacket::packageRank,
			ByteBufCodecs.idMapper(MonthlyPackageRank.BY_ID, MonthlyPackageRank::id), PlayerInfoS2CPacket::monthlyPackageRank,
			ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), PlayerInfoS2CPacket::prefix,
			PlayerInfoS2CPacket::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
