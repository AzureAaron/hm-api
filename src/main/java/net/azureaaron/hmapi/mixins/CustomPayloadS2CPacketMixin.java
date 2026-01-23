package net.azureaaron.hmapi.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.azureaaron.hmapi.network.HijackedCustomPayload;
import net.azureaaron.hmapi.network.WrappedPacketCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@Mixin(value = ClientboundCustomPayloadPacket.class, priority = 1888)
public abstract class CustomPayloadS2CPacketMixin {
	@Shadow
	public abstract CustomPacketPayload payload();

	@ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;codec(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$FallbackProvider;Ljava/util/List;)Lnet/minecraft/network/codec/StreamCodec;"))
	private static StreamCodec<FriendlyByteBuf, CustomPacketPayload> wrapS2CPacketCodec(StreamCodec<FriendlyByteBuf, CustomPacketPayload> original) {
		return new WrappedPacketCodec(original, PacketFlow.CLIENTBOUND);
	}

	@Inject(method = "handle", at = @At("HEAD"), cancellable = true)
	private void onApplication(ClientCommonPacketListener clientCommonPacketListener, CallbackInfo ci) {
		if (payload() instanceof HijackedCustomPayload hijacked) {
			new ClientboundCustomPayloadPacket(hijacked.original()).handle(clientCommonPacketListener);
			new ClientboundCustomPayloadPacket(hijacked.hijacked()).handle(clientCommonPacketListener);

			ci.cancel();
		}
	}
}
