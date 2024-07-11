package net.azureaaron.hmapi.network.packet.c2s;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.azureaaron.hmapi.network.packet.v1.s2c.LocationUpdateS2CPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class RegisterC2SPacketTest {

	@BeforeAll
	public static void setupEnvironment() {
		SharedConstants.createGameVersion();
		Bootstrap.initialize();
	}

	@Test
	void testSerializeByteBuf() {
		RegistryByteBuf expected = RegistryByteBuf.makeFactory(DynamicRegistryManager.EMPTY).apply(PacketByteBufs.create());
		RegistryByteBuf actual = RegistryByteBuf.makeFactory(DynamicRegistryManager.EMPTY).apply(PacketByteBufs.create());

		Object2IntMap<Identifier> wantedPackets = Util.make(new Object2IntOpenHashMap<>(), map -> map.put(LocationUpdateS2CPacket.ID.id(), 1));

		expected
		.writeVarInt(1)
		.writeMap(wantedPackets, Identifier.PACKET_CODEC, PacketCodecs.VAR_INT);

		RegisterC2SPacket.PACKET_CODEC.encode(actual, new RegisterC2SPacket(1, wantedPackets));

		Assertions.assertEquals(expected, actual);
	}
}
