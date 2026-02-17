package net.azureaaron.hmapi.network.packet.s2c;

import net.azureaaron.hmapi.data.server.Environment;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * This packet is sent each time upon logging into Hypixel, currently it only communicates the server's environment.
 * 
 * @param environment The current Hypixel server environment
 * 
 * @see {@link Environment}
 * @implSpec This record/packet is subject to potentially breaking changes in the future without notice as specified by Hypixel.
 */
public record HelloS2CPacket(Environment environment) implements HypixelS2CPacket {
	public static final CustomPacketPayload.Type<HypixelS2CPacket> ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("hypixel", "hello"));
	public static final StreamCodec<RegistryFriendlyByteBuf, HelloS2CPacket> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.idMapper(i -> Environment.values()[i], Environment::ordinal), HelloS2CPacket::environment, HelloS2CPacket::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
