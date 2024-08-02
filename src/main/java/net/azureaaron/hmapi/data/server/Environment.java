package net.azureaaron.hmapi.data.server;

/**
 * Describes the current Hypixel server environment, see constants for further information.
 */
public enum Environment {
	/**
	 * The main server network (e.g. mc.hypixel.net)
	 */
	PRODUCTION,
	/**
	 * The alpha server (alpha.hypixel.net)
	 */
	BETA,
	/**
	 * Hypixel's internal developer server
	 */
	TEST;
}
