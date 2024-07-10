package net.azureaaron.hmapi.network.packet.s2c;

import net.azureaaron.hmapi.data.server.Environment;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * This record/packet is subject to potentially breaking changes in the future as specified by Hypixel.
 * 
 * In the event of anything unexpected, we return a default fallback value to avoid any unexpected behaviour.
 */
public record HelloS2CPacket(Environment environment) implements HypixelS2CPacket {
	public static final CustomPayload.Id<HypixelS2CPacket> ID = new CustomPayload.Id<>(Identifier.of("hypixel", "hello"));
	public static final PacketCodec<RegistryByteBuf, HelloS2CPacket> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.indexed(i -> Environment.values()[i], Environment::ordinal), HelloS2CPacket::environment, HelloS2CPacket::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
