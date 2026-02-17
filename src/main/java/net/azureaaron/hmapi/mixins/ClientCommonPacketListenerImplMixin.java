package net.azureaaron.hmapi.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.azureaaron.hmapi.network.HypixelNetworkingImpl;
import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;

@Mixin(value = ClientCommonPacketListenerImpl.class, priority = 888)
public class ClientCommonPacketListenerImplMixin {

	@Inject(method = "handleCustomPayload(Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;)V", at = @At("HEAD"), cancellable = true)
	private void onCustomPayloadReceived(ClientboundCustomPayloadPacket customPayload, CallbackInfo ci) {
		if (customPayload.payload() instanceof HypixelS2CPacket packet) {
			HypixelNetworkingImpl.handlePayload(packet);
			ci.cancel();
		}
	}
}
