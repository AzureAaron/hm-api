package net.azureaaron.hmapi.network.packet.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.azureaaron.hmapi.data.server.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.SharedConstants;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.Bootstrap;

public class HelloS2CPacketTest {

	@BeforeAll
	public static void setupEnvironment() {
		SharedConstants.tryDetectVersion();
		Bootstrap.bootStrap();
	}

	@Test
	void testDeserializeByteBuf() {
		RegistryFriendlyByteBuf buf = RegistryFriendlyByteBuf.decorator(RegistryAccess.EMPTY).apply(PacketByteBufs.create());

		buf.writeVarInt(0);

		HelloS2CPacket expected = new HelloS2CPacket(Environment.PRODUCTION);
		HelloS2CPacket actual = HelloS2CPacket.PACKET_CODEC.decode(buf);

		Assertions.assertEquals(expected, actual);
	}
}
