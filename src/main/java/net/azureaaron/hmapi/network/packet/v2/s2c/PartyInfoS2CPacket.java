package net.azureaaron.hmapi.network.packet.v2.s2c;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.azureaaron.hmapi.data.party.PartyRole;
import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket;
import net.azureaaron.hmapi.utils.PacketCodecUtils;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * This packet gives information about whether the player is in a party, and if they are the {@link #members} map is populated with each party member and their role.
 * 
 * @param inParty whether the player is in a party or not
 * @param members a mapping of player {@link UUID}s to {@link PartyRole}s, this field will not be null when {@link #inParty} returns true
 */
public record PartyInfoS2CPacket(boolean inParty, @Nullable @Unmodifiable Map<UUID, PartyRole> members) implements HypixelS2CPacket {
	public static final CustomPacketPayload.Type<HypixelS2CPacket> ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("hypixel", "party_info"));
	private static final StreamCodec<RegistryFriendlyByteBuf, PartyInfoS2CPacket> IN_PARTY_PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.map(Object2ReferenceOpenHashMap::new, UUIDUtil.STREAM_CODEC, ByteBufCodecs.idMapper(i -> PartyRole.values()[i], PartyRole::ordinal)), PartyInfoS2CPacket::members, PartyInfoS2CPacket::new);
	public static final StreamCodec<RegistryFriendlyByteBuf, PartyInfoS2CPacket> PACKET_CODEC = PacketCodecUtils.dispatchConditionally(IN_PARTY_PACKET_CODEC, StreamCodec.unit(new PartyInfoS2CPacket(false, null)));

	private PartyInfoS2CPacket(Map<UUID, PartyRole> members) {
		this(true, Collections.unmodifiableMap(members));
	}

	@Override
	public Type<HypixelS2CPacket> type() {
		return ID;
	}
}
