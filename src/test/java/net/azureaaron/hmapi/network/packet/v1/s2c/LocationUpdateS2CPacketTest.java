package net.azureaaron.hmapi.network.packet.v1.s2c;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.DynamicRegistryManager;

public class LocationUpdateS2CPacketTest {

	@BeforeAll
	public static void setupEnvironment() {
		SharedConstants.createGameVersion();
		Bootstrap.initialize();
	}

	@Test
	void testDeserializeByteBuf() {
		RegistryByteBuf buf = RegistryByteBuf.makeFactory(DynamicRegistryManager.EMPTY).apply(PacketByteBufs.create());

		buf.writeString("mini88H")
		.writeOptional(Optional.of("SKYBLOCK"), PacketByteBuf::writeString);

		buf.writeOptional(Optional.<String>empty(), PacketByteBuf::writeString);
		buf.writeOptional(Optional.of("dynamic"), PacketByteBuf::writeString);
		buf.writeOptional(Optional.of("Private Island"), PacketByteBuf::writeString);

		LocationUpdateS2CPacket expected = new LocationUpdateS2CPacket("mini88H", Optional.of("SKYBLOCK"), Optional.empty(), Optional.of("dynamic"), Optional.of("Private Island"));
		LocationUpdateS2CPacket actual = LocationUpdateS2CPacket.PACKET_CODEC.decode(buf);

		Assertions.assertEquals(expected, actual);
	}
}
