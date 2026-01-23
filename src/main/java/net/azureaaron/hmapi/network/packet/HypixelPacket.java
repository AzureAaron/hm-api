package net.azureaaron.hmapi.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface HypixelPacket extends CustomPacketPayload {

	@ApiStatus.Internal
	public record Unknown(ResourceLocation id) implements HypixelPacket {

		public static StreamCodec<FriendlyByteBuf, Unknown> createPacketCodec(ResourceLocation id) {
			return new StreamCodec<>() {

				@Override
				public Unknown decode(FriendlyByteBuf buf) {
					//Since we duplicate the buffer we don't need to read all bytes
					return new Unknown(id);
				}

				@Override
				public void encode(FriendlyByteBuf buf, Unknown value) {
					//We will never encode this
				}
			};
		}

		@Override
		public Type<Unknown> type() {
			return new CustomPacketPayload.Type<>(this.id);
		}
	}
}
