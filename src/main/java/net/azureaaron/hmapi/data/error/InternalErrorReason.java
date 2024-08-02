package net.azureaaron.hmapi.data.error;

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
	public int id() {
		return this.id;
	}
}
