package net.azureaaron.hmapi.data.error;

import org.jetbrains.annotations.ApiStatus;

/**
 * Error reasons specific to the library's Mod API implementation.
 */
public enum InternalErrorReason implements ErrorReason {
	/**
	 * Catch all for unknown {@link ModApiErrorReason}s.
	 */
	UNKNOWN(0);

	private final int id;

	InternalErrorReason(int id) {
		this.id = id;
	}

	@Override
	@ApiStatus.Internal
	public int id() {
		return this.id;
	}
}
