package net.azureaaron.hmapi.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.azureaaron.hmapi.network.WrappedPacketCodec;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;

@Mixin(value = CustomPayloadC2SPacket.class, priority = 1888)
public class CustomPayloadC2SPacketMixin {

	@ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/CustomPayload;createCodec(Lnet/minecraft/network/packet/CustomPayload$CodecFactory;Ljava/util/List;)Lnet/minecraft/network/codec/PacketCodec;"))
	private static PacketCodec<PacketByteBuf, CustomPayload> wrapC2SPacketCodec(PacketCodec<PacketByteBuf, CustomPayload> original) {
		return new WrappedPacketCodec(original, NetworkSide.SERVERBOUND);
	}
}
