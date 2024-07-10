package net.azureaaron.hmapi.network.packet.v2.s2c;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.azureaaron.hmapi.data.party.PartyRole;
import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket;
import net.azureaaron.hmapi.utils.PacketCodecUtils;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

public record PartyInfoS2CPacket(boolean inParty, @Nullable Map<UUID, PartyRole> members) implements HypixelS2CPacket {
	public static final CustomPayload.Id<HypixelS2CPacket> ID = new CustomPayload.Id<>(Identifier.of("hypixel", "party_info"));
	private static final PacketCodec<RegistryByteBuf, PartyInfoS2CPacket> IN_PARTY_PACKET_CODEC = PacketCodec.tuple(PacketCodecs.map(Object2ReferenceOpenHashMap::new, Uuids.PACKET_CODEC, PacketCodecs.indexed(i -> PartyRole.values()[i], PartyRole::ordinal)), PartyInfoS2CPacket::members, PartyInfoS2CPacket::new);
	public static final PacketCodec<RegistryByteBuf, PartyInfoS2CPacket> PACKET_CODEC = PacketCodecUtils.dispatchConditionally(IN_PARTY_PACKET_CODEC, PacketCodec.unit(new PartyInfoS2CPacket(false, null)));

	private PartyInfoS2CPacket(Map<UUID, PartyRole> members) {
		this(true, Collections.unmodifiableMap(members));
	}

	@Override
	public Id<HypixelS2CPacket> getId() {
		return ID;
	}
}
