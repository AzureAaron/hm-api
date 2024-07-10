package net.azureaaron.hmapi.utils;

import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

/**
 * Extension class for {@link PacketCodec PacketCodecs}.
 */
@ApiStatus.Internal
public class PacketCodecUtils {
	private static final Logger LOGGER = LogUtils.getLogger();

	/**
	 * @param <C> The common interface shared between between all supplied primary packets and the error packet.
	 * @param <E> The type of the error codec
	 *
	 * @return A codec that decodes the {@link RegistryByteBuf} in line with the packet format of Hypixel's Mod API and the requirements of my implementation.
	 */
	public static <B extends PacketByteBuf, C, E extends C> PacketCodec<B, C> dispatchHypixel(Int2ObjectMap<PacketCodec<B, C>> primaryPacketCodecs, PacketCodec<B, E> errorCodec) {
		return new PacketCodec<B, C>() {

			@SuppressWarnings("unchecked")
			@Override
			public C decode(B buf) {
				Boolean success = buf.readBoolean();
				int version = success ? buf.readVarInt() : -1;

				return switch (success) {
					case Boolean s when s && version == 1 && primaryPacketCodecs.containsKey(version) -> decodeInternal(buf, primaryPacketCodecs.get(version));
					case Boolean s when s && version == 2 && primaryPacketCodecs.containsKey(version) -> decodeInternal(buf, primaryPacketCodecs.get(version));
					case Boolean s when !s -> errorCodec.decode(buf);

					default -> (C) HypixelS2CPacket.NOP;
				};
			}

			private C decodeInternal(B buf, PacketCodec<B, C> packetCodec) {
				C c = packetCodec.decode(buf);

				//There was a bug where Hypixel would send a bunch of extra bytes with a value of 0 so this was a work around to avoid being kicked from the server
				//for not fully reading the packet
				//Its also good practice if a breaking change is added
				readAllBytes(buf, true);

				return c;
			}

			//Since this is only used for S2C packets we don't really need to care about what it does
			//so we will just throw an exception
			@Override
			public void encode(B buf, C value) {
				throw new UnsupportedOperationException();
			}
		};
	}

	public static <B extends PacketByteBuf, T> PacketCodec<B, T> dispatchConditionally(PacketCodec<B, T> ifTrue, PacketCodec<B, T> ifFalse) {
		return new PacketCodec<B, T>() {

			@Override
			public T decode(B buf) {
				return buf.readBoolean() ? ifTrue.decode(buf) : ifFalse.decode(buf);
			}

			//Only used for S2C
			@Override
			public void encode(B buf, T value) {
				throw new UnsupportedOperationException();
			}
		};
	}
	
	public static <B extends PacketByteBuf, C, T extends C, E extends C> PacketCodec<B, C> dispatchSafely(PacketCodec<B, T> packetCodec, PacketCodec<B, E> errorCodec) {
		return new PacketCodec<B, C>() {

			@SuppressWarnings("unchecked")
			@Override
			public C decode(B buf) {
				try {
					Boolean success = buf.readBoolean();

					return switch (success) {
						case Boolean s when s -> {
							C packet = packetCodec.decode(buf);

							//This is used only for the HelloS2CPacket right now, which can return extra bytes if necessary in the future
							readAllBytes(buf, false);

							yield packet;
						}

						case Boolean s when !s -> errorCodec.decode(buf);

						default -> (C) HypixelS2CPacket.NOP;
					};
				} catch (Throwable t) {
					LOGGER.error("[HM API] Encountered an unexpected exception while decoding a packet!", t);
				}

				return (C) HypixelS2CPacket.NOP;
			}

			//Only used for S2C
			@Override
			public void encode(B buf, C value) {
				throw new UnsupportedOperationException();
			}
		};
	}

	private static void readAllBytes(PacketByteBuf buf, boolean logWarning) {
		buf.readerIndex(buf.writerIndex());
		//TODO restore logging maybe
	}
}
