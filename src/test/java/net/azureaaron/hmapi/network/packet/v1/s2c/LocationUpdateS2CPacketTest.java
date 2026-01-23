package net.azureaaron.hmapi.network.packet.v1.s2c;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.server.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.core.RegistryAccess;

public class LocationUpdateS2CPacketTest {

	@BeforeAll
	public static void setupEnvironment() {
		SharedConstants.tryDetectVersion();
		Bootstrap.bootStrap();
	}

	@Test
	void testDeserializeByteBuf() {
		RegistryFriendlyByteBuf buf = RegistryFriendlyByteBuf.decorator(RegistryAccess.EMPTY).apply(PacketByteBufs.create());

		buf.writeUtf("mini88H")
		.writeOptional(Optional.of("SKYBLOCK"), FriendlyByteBuf::writeUtf);

		buf.writeOptional(Optional.<String>empty(), FriendlyByteBuf::writeUtf);
		buf.writeOptional(Optional.of("dynamic"), FriendlyByteBuf::writeUtf);
		buf.writeOptional(Optional.of("Private Island"), FriendlyByteBuf::writeUtf);

		LocationUpdateS2CPacket expected = new LocationUpdateS2CPacket("mini88H", Optional.of("SKYBLOCK"), Optional.empty(), Optional.of("dynamic"), Optional.of("Private Island"));
		LocationUpdateS2CPacket actual = LocationUpdateS2CPacket.PACKET_CODEC.decode(buf);

		Assertions.assertEquals(expected, actual);
	}
}
