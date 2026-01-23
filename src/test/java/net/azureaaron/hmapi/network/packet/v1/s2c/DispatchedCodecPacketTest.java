package net.azureaaron.hmapi.network.packet.v1.s2c;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.azureaaron.hmapi.data.error.ModApiErrorReason;
import net.azureaaron.hmapi.data.rank.MonthlyPackageRank;
import net.azureaaron.hmapi.data.rank.PackageRank;
import net.azureaaron.hmapi.data.rank.PlayerRank;
import net.azureaaron.hmapi.network.packet.s2c.ErrorS2CPacket;
import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket;
import net.azureaaron.hmapi.utils.PacketCodecUtils;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.server.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.Util;

public class DispatchedCodecPacketTest {

	@BeforeAll
	public static void setupEnvironment() {
		SharedConstants.tryDetectVersion();
		Bootstrap.bootStrap();
	}

	@Test
	void testNormalPacket() {
		RegistryFriendlyByteBuf buf = RegistryFriendlyByteBuf.decorator(RegistryAccess.EMPTY).apply(PacketByteBufs.create());

		buf.writeBoolean(true)
		.writeVarInt(1)
		.writeVarInt(1)
		.writeVarInt(5)
		.writeVarInt(2)
		.writeOptional(Optional.<String>empty(), FriendlyByteBuf::writeUtf);

		Int2ObjectOpenHashMap<StreamCodec<RegistryFriendlyByteBuf, ? extends HypixelS2CPacket>> playerInfoPacketCodecs = Util.make(new Int2ObjectOpenHashMap<>(), map -> map.put(1, PlayerInfoS2CPacket.PACKET_CODEC));

		PlayerInfoS2CPacket expected = new PlayerInfoS2CPacket(PlayerRank.NORMAL, PackageRank.MVP_PLUS, MonthlyPackageRank.SUPERSTAR, Optional.empty());
		HypixelS2CPacket actual = PacketCodecUtils.dispatchHypixel(playerInfoPacketCodecs, ErrorS2CPacket.PACKET_CODEC.apply(PlayerInfoS2CPacket.ID)).decode(buf);

		Assertions.assertInstanceOf(PlayerInfoS2CPacket.class, actual);
		Assertions.assertEquals(expected, (PlayerInfoS2CPacket) actual);
	}

	@Test
	void testUnknownVersionPacket() {
		RegistryFriendlyByteBuf buf = RegistryFriendlyByteBuf.decorator(RegistryAccess.EMPTY).apply(PacketByteBufs.create());

		buf.writeBoolean(true)
		.writeVarInt(256);

		HypixelS2CPacket actual = PacketCodecUtils.dispatchHypixel(null, ErrorS2CPacket.PACKET_CODEC.apply(null)).decode(buf);

		Assertions.assertSame(HypixelS2CPacket.NOP, actual);
	}
	
	@Test
	void testErrorPacket() {
		RegistryFriendlyByteBuf buf = RegistryFriendlyByteBuf.decorator(RegistryAccess.EMPTY).apply(PacketByteBufs.create());

		buf.writeBoolean(false)
		.writeVarInt(2);

		ErrorS2CPacket expected = new ErrorS2CPacket(null, ModApiErrorReason.INTERNAL_SERVER_ERROR);
		HypixelS2CPacket actual = PacketCodecUtils.dispatchHypixel(null, ErrorS2CPacket.PACKET_CODEC.apply(null)).decode(buf);

		Assertions.assertInstanceOf(ErrorS2CPacket.class, actual);
		Assertions.assertEquals(expected, (ErrorS2CPacket) actual);
	}
}
