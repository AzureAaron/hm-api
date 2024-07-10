package net.azureaaron.hmapi.utils;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.MinecraftClient;

@ApiStatus.Internal
public class Utils {
	/**
	 * Available solely for debugging purposes.
	 */
	private static final String HYPIXEL_ADDRESS_OVERRIDE = System.getProperty("hmapi.alternateHypixelAddress", "");

	public static boolean isOnHypixel() {
		MinecraftClient client = MinecraftClient.getInstance();
		String serverAddress = client.getCurrentServerEntry() != null ? client.getCurrentServerEntry().address.toLowerCase() : "";
		String serverBrand = client.player != null && client.player.networkHandler != null && client.player.networkHandler.getBrand() != null ? client.player.networkHandler.getBrand() : "";

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
