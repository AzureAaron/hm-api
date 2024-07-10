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
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Util;

@ApiStatus.Internal
public class HypixelCustomPayloadCodecs {
	private static final PacketCodec<PacketByteBuf, CustomPayload> C2S_PACKET_CODEC = CustomPayload.createCodec(
			HypixelPacket.Unknown::createPacketCodec, Lists.newArrayList(
					createType(PartyInfoC2SPacket.ID, PartyInfoC2SPacket.PACKET_CODEC),
					createType(PlayerInfoC2SPacket.ID, PlayerInfoC2SPacket.PACKET_CODEC),
					createType(RegisterC2SPacket.ID, RegisterC2SPacket.PACKET_CODEC))
			);

	@SuppressWarnings("unchecked")
	private static final PacketCodec<PacketByteBuf, CustomPayload> S2C_PACKET_CODEC = CustomPayload.createCodec(
			HypixelPacket.Unknown::createPacketCodec, Lists.newArrayList(
					createType(PartyInfoS2CPacket.ID, PacketCodecUtils.dispatchHypixel(
							Util.make(new Int2ObjectOpenHashMap<>(), map -> {
								map.put(2, PacketCodec.class.cast(PartyInfoS2CPacket.PACKET_CODEC));
							}),
							ErrorS2CPacket.PACKET_CODEC.apply(PartyInfoS2CPacket.ID))),
					createType(PlayerInfoS2CPacket.ID, PacketCodecUtils.dispatchHypixel(
							Util.make(new Int2ObjectOpenHashMap<>(), map -> map.put(1, PacketCodec.class.cast(PlayerInfoS2CPacket.PACKET_CODEC))),
							ErrorS2CPacket.PACKET_CODEC.apply(PlayerInfoS2CPacket.ID))),
					createType(HelloS2CPacket.ID, PacketCodecUtils.dispatchSafely(HelloS2CPacket.PACKET_CODEC, ErrorS2CPacket.PACKET_CODEC.apply(HelloS2CPacket.ID))),
					createType(LocationUpdateS2CPacket.ID, PacketCodecUtils.dispatchHypixel(
							Util.make(new Int2ObjectOpenHashMap<>(), map -> map.put(1, PacketCodec.class.cast(LocationUpdateS2CPacket.PACKET_CODEC))),
							ErrorS2CPacket.PACKET_CODEC.apply(LocationUpdateS2CPacket.ID))))
			);

	@SuppressWarnings("unchecked")
	//T2 is because the HelloS2CPacket's id generic type is HypixelS2CPacket and not the same as the codec
	private static <B extends PacketByteBuf, T1 extends CustomPayload, T2 extends CustomPayload> CustomPayload.Type<PacketByteBuf, CustomPayload> createType(CustomPayload.Id<T1> id, PacketCodec<B, T2> packetCodec) {
		return new CustomPayload.Type<>(id, PacketCodec.class.cast(packetCodec));
	}

	static PacketCodec<PacketByteBuf, CustomPayload> get4Direction(NetworkSide direction) {
		return switch (direction) {
			case SERVERBOUND -> C2S_PACKET_CODEC;
			case CLIENTBOUND -> S2C_PACKET_CODEC;
		};
	}
}
