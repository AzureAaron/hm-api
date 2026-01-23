package net.azureaaron.hmapi.network.packet.c2s;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record RegisterC2SPacket(int version, Object2IntMap<ResourceLocation> eventsToRegister) implements HypixelC2SPacket {
	public static final CustomPacketPayload.Type<RegisterC2SPacket> ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("hypixel", "register"));
	public static final StreamCodec<RegistryFriendlyByteBuf, RegisterC2SPacket> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, RegisterC2SPacket::version,
			ByteBufCodecs.map(Object2IntOpenHashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.VAR_INT, 5), RegisterC2SPacket::eventsToRegister, RegisterC2SPacket::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
