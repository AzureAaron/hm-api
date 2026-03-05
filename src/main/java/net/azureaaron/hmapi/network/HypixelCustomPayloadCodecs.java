package net.azureaaron.hmapi.network;

import org.jetbrains.annotations.ApiStatus;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.azureaaron.hmapi.network.packet.HypixelPacket;
import net.azureaaron.hmapi.network.packet.c2s.PartyInfoC2SPacket;
import net.azureaaron.hmapi.network.packet.c2s.PlayerInfoC2SPacket;
import net.azureaaron.hmapi.network.packet.c2s.RegisterC2SPacket;
import net.azureaaron.hmapi.network.packet.s2c.ErrorS2CPacket;
import net.azureaaron.hmapi.network.packet.s2c.HelloS2CPacket;
import net.azureaaron.hmapi.network.packet.v1.s2c.LocationUpdateS2CPacket;
import net.azureaaron.hmapi.network.packet.v1.s2c.PlayerInfoS2CPacket;
import net.azureaaron.hmapi.network.packet.v2.s2c.PartyInfoS2CPacket;
import net.azureaaron.hmapi.utils.PacketCodecUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.Util;

@ApiStatus.Internal
public class HypixelCustomPayloadCodecs {
	private static final StreamCodec<FriendlyByteBuf, CustomPacketPayload> C2S_PACKET_CODEC = CustomPacketPayload.codec(
			HypixelPacket.Unknown::createPacketCodec, Lists.newArrayList(
					createType(PartyInfoC2SPacket.ID, PartyInfoC2SPacket.PACKET_CODEC),
					createType(PlayerInfoC2SPacket.ID, PlayerInfoC2SPacket.PACKET_CODEC),
					createType(RegisterC2SPacket.ID, RegisterC2SPacket.PACKET_CODEC))
			);

	private static final StreamCodec<FriendlyByteBuf, CustomPacketPayload> S2C_PACKET_CODEC = CustomPacketPayload.codec(
			HypixelPacket.Unknown::createPacketCodec, Lists.newArrayList(
					createType(PartyInfoS2CPacket.ID, PacketCodecUtils.dispatchHypixel(
							Util.make(new Int2ObjectOpenHashMap<>(), map -> {
								map.put(2, PartyInfoS2CPacket.PACKET_CODEC);
							}),
							ErrorS2CPacket.PACKET_CODEC.apply(PartyInfoS2CPacket.ID))),
					createType(PlayerInfoS2CPacket.ID, PacketCodecUtils.dispatchHypixel(
							Util.make(new Int2ObjectOpenHashMap<>(), map -> map.put(1, PlayerInfoS2CPacket.PACKET_CODEC)),
							ErrorS2CPacket.PACKET_CODEC.apply(PlayerInfoS2CPacket.ID))),
					createType(HelloS2CPacket.ID, PacketCodecUtils.dispatchSafely(HelloS2CPacket.PACKET_CODEC, ErrorS2CPacket.PACKET_CODEC.apply(HelloS2CPacket.ID))),
					createType(LocationUpdateS2CPacket.ID, PacketCodecUtils.dispatchHypixel(
							Util.make(new Int2ObjectOpenHashMap<>(), map -> map.put(1, LocationUpdateS2CPacket.PACKET_CODEC)),
							ErrorS2CPacket.PACKET_CODEC.apply(LocationUpdateS2CPacket.ID))))
			);

	@SuppressWarnings("unchecked")
	private static <B extends FriendlyByteBuf, T extends CustomPacketPayload> CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, CustomPacketPayload> createType(CustomPacketPayload.Type<T> id, StreamCodec<B, T> packetCodec) {
		return new CustomPacketPayload.TypeAndCodec<>(id, StreamCodec.class.cast(packetCodec));
	}

	static StreamCodec<FriendlyByteBuf, CustomPacketPayload> get4Direction(PacketFlow direction) {
		return switch (direction) {
			case SERVERBOUND -> C2S_PACKET_CODEC;
			case CLIENTBOUND -> S2C_PACKET_CODEC;
		};
	}
}
