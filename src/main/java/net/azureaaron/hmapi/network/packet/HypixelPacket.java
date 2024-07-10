package net.azureaaron.hmapi.network.packet;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

@ApiStatus.Internal
public interface HypixelPacket extends CustomPayload {

	@ApiStatus.Internal
	public record Unknown(Identifier id) implements HypixelPacket {

		public static PacketCodec<PacketByteBuf, Unknown> createPacketCodec(Identifier id) {
			return new PacketCodec<>() {

				@Override
				public Unknown decode(PacketByteBuf buf) {
					//Since we duplicate the buffer we don't need to read all bytes
					return new Unknown(id);
				}

				@Override
				public void encode(PacketByteBuf buf, Unknown value) {
					//We will never encode this
				}
			};
		}

		@Override
		public Id<Unknown> getId() {
			return new CustomPayload.Id<>(this.id);
		}
	}
}
