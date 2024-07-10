package net.azureaaron.hmapi.network;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

@ApiStatus.Internal
public record HijackedCustomPayload(CustomPayload original, CustomPayload hijacked) implements CustomPayload {
	private static final CustomPayload.Id<HijackedCustomPayload> ID = new CustomPayload.Id<HijackedCustomPayload>(Identifier.of("hmapi", "hijacked"));

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
