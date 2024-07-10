package net.azureaaron.hmapi.network.packet.c2s;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RegisterC2SPacket(int version, Object2IntMap<Identifier> eventsToRegister) implements HypixelC2SPacket {
	public static final CustomPayload.Id<RegisterC2SPacket> ID = new CustomPayload.Id<>(Identifier.of("hypixel", "register"));
	public static final PacketCodec<RegistryByteBuf, RegisterC2SPacket> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, RegisterC2SPacket::version,
			PacketCodecs.map(Object2IntOpenHashMap::new, Identifier.PACKET_CODEC, PacketCodecs.VAR_INT, 5), RegisterC2SPacket::eventsToRegister, RegisterC2SPacket::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
