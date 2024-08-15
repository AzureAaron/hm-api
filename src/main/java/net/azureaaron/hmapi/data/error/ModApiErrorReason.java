package net.azureaaron.hmapi.data.error;

import java.util.function.IntFunction;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.util.function.ValueLists;

/**
 * Error reasons native to the Mod API.
 */
public enum ModApiErrorReason implements ErrorReason {
	DISABLED(1),
	INTERNAL_SERVER_ERROR(2),
	RATE_LIMITED(3),
	INVALID_PACKET_VERSION(4),
	NO_LONGER_SUPPORTED(5);

	private static final IntFunction<ModApiErrorReason> BY_ID = ValueLists.createIdToValueFunction(ModApiErrorReason::id, ModApiErrorReason.values(), (ModApiErrorReason) null);

	private final int id;

	ModApiErrorReason(int id) {
		this.id = id;
	}

	@Override
	@ApiStatus.Internal
	public int id() {
		return this.id;
	}

	/**
	 * If the {@code id} is a known Mod API error reason id, returns the appropriate reason otherwise returns an {@link InternalErrorReason}.
	 */
	@ApiStatus.Internal
	public static ErrorReason tryResolveReason(int id) {
		ModApiErrorReason modApiReason = BY_ID.apply(id);

		return modApiReason != null ? modApiReason : InternalErrorReason.UNKNOWN;
	}
}
