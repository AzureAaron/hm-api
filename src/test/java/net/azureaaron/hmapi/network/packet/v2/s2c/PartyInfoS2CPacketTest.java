package net.azureaaron.hmapi.network.packet.v2.s2c;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.azureaaron.hmapi.data.party.PartyRole;
import net.fabricmc.fabric.api.networking.v1.FriendlyByteBufs;
import net.minecraft.SharedConstants;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.Bootstrap;
import net.minecraft.util.Util;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;

public class PartyInfoS2CPacketTest {

	@BeforeAll
	public static void setupEnvironment() {
		SharedConstants.tryDetectVersion();
		Bootstrap.bootStrap();
	}

	//@Test
	void testDeserializeByteBuf() {
		RegistryFriendlyByteBuf buf = RegistryFriendlyByteBuf.decorator(RegistryAccess.EMPTY).apply(FriendlyByteBufs.create());
		XoroshiroRandomSource rand = new XoroshiroRandomSource(0x88888888L);

		Map<UUID, PartyRole> members = Util.make(new Object2ReferenceOpenHashMap<>(), map -> {
			for (int i = 0; i < 8; i++) {
				map.put(UUID.randomUUID(), PartyRole.values()[Math.clamp(rand.nextIntBetweenInclusive(0, 2), 0, 2)]);
			}
		});

		PartyInfoS2CPacket.PACKET_CODEC.encode(buf, new PartyInfoS2CPacket(true, members));

		PartyInfoS2CPacket expected = new PartyInfoS2CPacket(true, members);
		PartyInfoS2CPacket actual = PartyInfoS2CPacket.PACKET_CODEC.decode(buf);

		Assertions.assertEquals(expected, actual);
	}
}
