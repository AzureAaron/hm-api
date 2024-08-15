package net.azureaaron.hmapi.data.error;

import org.jetbrains.annotations.ApiStatus;

public sealed interface ErrorReason permits ModApiErrorReason, InternalErrorReason {

	@ApiStatus.Internal
	int id();
}
