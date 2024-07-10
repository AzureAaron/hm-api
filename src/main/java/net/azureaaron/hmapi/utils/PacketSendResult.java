package net.azureaaron.hmapi.utils;

import java.util.OptionalLong;

import org.jetbrains.annotations.ApiStatus;

public record PacketSendResult(boolean sent, OptionalLong retryAfter) {

	@ApiStatus.Internal
	public static PacketSendResult success() {
		return new PacketSendResult(true, OptionalLong.empty());
	}

	@ApiStatus.Internal
	public static PacketSendResult onCooldown(long retryAfter) {
		return new PacketSendResult(false, OptionalLong.of(retryAfter));
	}
}
