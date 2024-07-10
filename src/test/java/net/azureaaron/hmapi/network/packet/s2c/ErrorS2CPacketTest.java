package net.azureaaron.hmapi.network.packet.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.azureaaron.hmapi.data.error.ModApiErrorReason;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.DynamicRegistryManager;

public class ErrorS2CPacketTest {

	@BeforeAll
	public static void setupEnvironment() {
		SharedConstants.createGameVersion();
		Bootstrap.initialize();
	}

	@Test
	void testDeserializeByteBuf() {
		RegistryByteBuf buf = RegistryByteBuf.makeFactory(DynamicRegistryManager.EMPTY).apply(PacketByteBufs.create());

		buf.writeVarInt(2);

		ErrorS2CPacket expected = new ErrorS2CPacket(null, ModApiErrorReason.INTERNAL_SERVER_ERROR);
		ErrorS2CPacket actual = ErrorS2CPacket.PACKET_CODEC.apply(null).decode(buf);

		Assertions.assertEquals(expected, actual);
	}
}
