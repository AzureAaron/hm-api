package net.azureaaron.hmapi.network.packet.v1.s2c;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.azureaaron.hmapi.data.rank.MonthlyPackageRank;
import net.azureaaron.hmapi.data.rank.PackageRank;
import net.azureaaron.hmapi.data.rank.PlayerRank;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.SharedConstants;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.Bootstrap;

public class PlayerInfoS2CPacketTest {

	@BeforeAll
	public static void setupEnvironment() {
		SharedConstants.tryDetectVersion();
		Bootstrap.bootStrap();
	}

	@Test
	void testDeserializeByteBuf() {
		RegistryFriendlyByteBuf buf = RegistryFriendlyByteBuf.decorator(RegistryAccess.EMPTY).apply(PacketByteBufs.create());

		buf.writeVarInt(1)
		.writeVarInt(5)
		.writeVarInt(2)
		.writeOptional(Optional.<String>empty(), FriendlyByteBuf::writeUtf);

		PlayerInfoS2CPacket expected = new PlayerInfoS2CPacket(PlayerRank.NORMAL, PackageRank.MVP_PLUS, MonthlyPackageRank.SUPERSTAR, Optional.empty());
		PlayerInfoS2CPacket actual = PlayerInfoS2CPacket.PACKET_CODEC.decode(buf);

		Assertions.assertEquals(expected, actual);
	}
}
