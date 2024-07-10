package net.azureaaron.hmapi.network.packet.v2.s2c;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.azureaaron.hmapi.data.party.PartyRole;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Util;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.random.Xoroshiro128PlusPlusRandom;

public class PartyInfoS2CPacketTest {

	@BeforeAll
	public static void setupEnvironment() {
		SharedConstants.createGameVersion();
		Bootstrap.initialize();
	}

	@Test
	void testDeserializeByteBuf() {
		RegistryByteBuf buf = RegistryByteBuf.makeFactory(DynamicRegistryManager.EMPTY).apply(PacketByteBufs.create());
		Xoroshiro128PlusPlusRandom rand = new Xoroshiro128PlusPlusRandom(0x88888888L);

		Map<UUID, PartyRole> members = Util.make(new Object2ReferenceOpenHashMap<>(), map -> {
			for (int i = 0; i < 8; i++) {
				map.put(UUID.randomUUID(), PartyRole.values()[Math.clamp(rand.nextBetween(0, 2), 0, 2)]);
			}
		});

		buf.writeBoolean(true);
		buf.writeMap(members, Uuids.PACKET_CODEC, PacketCodecs.indexed(i -> PartyRole.values()[i], PartyRole::ordinal));

		PartyInfoS2CPacket expected = new PartyInfoS2CPacket(true, members);
		PartyInfoS2CPacket actual = PartyInfoS2CPacket.PACKET_CODEC.decode(buf);

		Assertions.assertEquals(expected, actual);
	}
}
