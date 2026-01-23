package net.azureaaron.hmapi.network.packet.c2s;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.azureaaron.hmapi.network.packet.v1.s2c.LocationUpdateS2CPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.server.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;

public class RegisterC2SPacketTest {

	@BeforeAll
	public static void setupEnvironment() {
		SharedConstants.tryDetectVersion();
		Bootstrap.bootStrap();
	}

	@Test
	void testSerializeByteBuf() {
		RegistryFriendlyByteBuf expected = RegistryFriendlyByteBuf.decorator(RegistryAccess.EMPTY).apply(PacketByteBufs.create());
		RegistryFriendlyByteBuf actual = RegistryFriendlyByteBuf.decorator(RegistryAccess.EMPTY).apply(PacketByteBufs.create());

		Object2IntMap<ResourceLocation> wantedPackets = Util.make(new Object2IntOpenHashMap<>(), map -> map.put(LocationUpdateS2CPacket.ID.id(), 1));

		expected
		.writeVarInt(1)
		.writeMap(wantedPackets, ResourceLocation.STREAM_CODEC, ByteBufCodecs.VAR_INT);

		RegisterC2SPacket.PACKET_CODEC.encode(actual, new RegisterC2SPacket(1, wantedPackets));

		Assertions.assertEquals(expected, actual);
	}
}
