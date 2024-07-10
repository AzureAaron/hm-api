package net.azureaaron.hmapi.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.azureaaron.hmapi.network.HypixelNetworkingImpl;
import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;

@Mixin(value = ClientCommonNetworkHandler.class, priority = 888)
public class ClientCommonNetworkHandlerMixin {

	@Inject(method = "onCustomPayload(Lnet/minecraft/network/packet/s2c/common/CustomPayloadS2CPacket;)V", at = @At("HEAD"), cancellable = true)
	private void onCustomPayloadReceived(CustomPayloadS2CPacket customPayload, CallbackInfo ci) {
		if (customPayload.payload() instanceof HypixelS2CPacket packet) {
			HypixelNetworkingImpl.handlePayload(packet);
			ci.cancel();
		}
	}
}
