package net.azureaaron.hmapi.network.packet.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.azureaaron.hmapi.data.error.ModApiErrorReason;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.SharedConstants;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.Bootstrap;

public class ErrorS2CPacketTest {

	@BeforeAll
	public static void setupEnvironment() {
		SharedConstants.tryDetectVersion();
		Bootstrap.bootStrap();
	}

	@Test
	void testDeserializeByteBuf() {
		RegistryFriendlyByteBuf buf = RegistryFriendlyByteBuf.decorator(RegistryAccess.EMPTY).apply(PacketByteBufs.create());

		buf.writeVarInt(2);

		ErrorS2CPacket expected = new ErrorS2CPacket(null, ModApiErrorReason.INTERNAL_SERVER_ERROR);
		ErrorS2CPacket actual = ErrorS2CPacket.PACKET_CODEC.apply(null).decode(buf);

		Assertions.assertEquals(expected, actual);
	}
}
