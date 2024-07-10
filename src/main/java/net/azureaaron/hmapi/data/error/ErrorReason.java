package net.azureaaron.hmapi.data.error;

public sealed interface ErrorReason permits ModApiErrorReason, InternalErrorReason {

	int id();
}
