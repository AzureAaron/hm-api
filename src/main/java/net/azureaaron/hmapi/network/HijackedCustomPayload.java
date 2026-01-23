package net.azureaaron.hmapi.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public record HijackedCustomPayload(CustomPacketPayload original, CustomPacketPayload hijacked) implements CustomPacketPayload {
	private static final CustomPacketPayload.Type<HijackedCustomPayload> ID = new CustomPacketPayload.Type<HijackedCustomPayload>(ResourceLocation.fromNamespaceAndPath("hmapi", "hijacked"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
