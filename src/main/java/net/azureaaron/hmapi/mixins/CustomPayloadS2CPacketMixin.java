package net.azureaaron.hmapi.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.azureaaron.hmapi.network.HijackedCustomPayload;
import net.azureaaron.hmapi.network.WrappedPacketCodec;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientCommonPacketListener;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;

@Mixin(value = CustomPayloadS2CPacket.class, priority = 1888)
public class CustomPayloadS2CPacketMixin {

	@ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/CustomPayload;createCodec(Lnet/minecraft/network/packet/CustomPayload$CodecFactory;Ljava/util/List;)Lnet/minecraft/network/codec/PacketCodec;"))
	private static PacketCodec<PacketByteBuf, CustomPayload> wrapS2CPacketCodec(PacketCodec<PacketByteBuf, CustomPayload> original) {
		return new WrappedPacketCodec(original, NetworkSide.CLIENTBOUND);
	}

	@Inject(method = "apply", at = @At("HEAD"), cancellable = true)
	private void onApplication(ClientCommonPacketListener clientCommonPacketListener, CallbackInfo ci) {
		if (((CustomPayloadS2CPacket) (Object) this).payload() instanceof HijackedCustomPayload hijacked) {
			new CustomPayloadS2CPacket(hijacked.original()).apply(clientCommonPacketListener);
			new CustomPayloadS2CPacket(hijacked.hijacked()).apply(clientCommonPacketListener);

			ci.cancel();
		}
	}
}
