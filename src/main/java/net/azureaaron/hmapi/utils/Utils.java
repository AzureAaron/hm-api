package net.azureaaron.hmapi.utils;

import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class Utils {
	/**
	 * Available solely for debugging purposes.
	 */
	private static final String HYPIXEL_ADDRESS_OVERRIDE = System.getProperty("hmapi.alternateHypixelAddress", "");

	public static boolean isOnHypixel() {
		Minecraft client = Minecraft.getInstance();
		String serverAddress = client.getCurrentServer() != null ? client.getCurrentServer().ip.toLowerCase() : "";
		String serverBrand = client.player != null && client.player.connection != null && client.player.connection.serverBrand() != null ? client.player.connection.serverBrand() : "";

		return (!serverAddress.isEmpty() && HYPIXEL_ADDRESS_OVERRIDE.equalsIgnoreCase(serverAddress)) || serverAddress.contains("hypixel.net") || serverAddress.contains("hypixel.io") || serverBrand.contains("Hypixel BungeeCord");
	}

	public static int requireInRange(int version, int min, int max) {
		if (!(version >= min && version <= max)) {
			throw new UnsupportedOperationException("Requested an unsupported packet version");
		}

		return version;
	}

	public static void requireIsOnHypixel() {
		if (!isOnHypixel()) throw new UnsupportedOperationException("Requested to send a C2S packet to Hypixel whilst not being on the server!");
	}
}
