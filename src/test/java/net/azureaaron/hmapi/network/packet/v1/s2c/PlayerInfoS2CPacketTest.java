package net.azureaaron.hmapi.network.packet.v1.s2c;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.azureaaron.hmapi.data.rank.MonthlyPackageRank;
import net.azureaaron.hmapi.data.rank.PackageRank;
import net.azureaaron.hmapi.data.rank.PlayerRank;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.DynamicRegistryManager;

public class PlayerInfoS2CPacketTest {

	@BeforeAll
	public static void setupEnvironment() {
		SharedConstants.createGameVersion();
		Bootstrap.initialize();
	}

	@Test
	void testDeserializeByteBuf() {
		RegistryByteBuf buf = RegistryByteBuf.makeFactory(DynamicRegistryManager.EMPTY).apply(PacketByteBufs.create());

		buf.writeVarInt(1)
		.writeVarInt(5)
		.writeVarInt(2)
		.writeOptional(Optional.<String>empty(), PacketByteBuf::writeString);

		PlayerInfoS2CPacket expected = new PlayerInfoS2CPacket(PlayerRank.NORMAL, PackageRank.MVP_PLUS, MonthlyPackageRank.SUPERSTAR, Optional.empty());
		PlayerInfoS2CPacket actual = PlayerInfoS2CPacket.PACKET_CODEC.decode(buf);

		Assertions.assertEquals(expected, actual);
	}
}
