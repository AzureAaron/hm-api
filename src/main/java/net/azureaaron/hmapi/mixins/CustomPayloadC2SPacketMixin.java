package net.azureaaron.hmapi.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.azureaaron.hmapi.network.WrappedPacketCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@Mixin(value = ServerboundCustomPayloadPacket.class, priority = 1888)
public class CustomPayloadC2SPacketMixin {

	@ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;codec(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$FallbackProvider;Ljava/util/List;)Lnet/minecraft/network/codec/StreamCodec;"))
	private static StreamCodec<FriendlyByteBuf, CustomPacketPayload> wrapC2SPacketCodec(StreamCodec<FriendlyByteBuf, CustomPacketPayload> original) {
		return new WrappedPacketCodec(original, PacketFlow.SERVERBOUND);
	}
}
