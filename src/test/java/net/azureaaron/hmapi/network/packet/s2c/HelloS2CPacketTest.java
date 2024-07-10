package net.azureaaron.hmapi.network.packet.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.azureaaron.hmapi.data.server.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.DynamicRegistryManager;

public class HelloS2CPacketTest {

	@BeforeAll
	public static void setupEnvironment() {
		SharedConstants.createGameVersion();
		Bootstrap.initialize();
	}

	@Test
	void testDeserializeByteBuf() {
		RegistryByteBuf buf = RegistryByteBuf.makeFactory(DynamicRegistryManager.EMPTY).apply(PacketByteBufs.create());

		buf.writeVarInt(0);

		HelloS2CPacket expected = new HelloS2CPacket(Environment.PRODUCTION);
		HelloS2CPacket actual = HelloS2CPacket.PACKET_CODEC.decode(buf);

		Assertions.assertEquals(expected, actual);
	}
}
