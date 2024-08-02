package net.azureaaron.hmapi.data.rank;

import java.util.function.IntFunction;

import net.minecraft.util.function.ValueLists;

/**
 * Represents a paid rank.
 */
public enum PackageRank {
	NONE(1),
	VIP(2),
	VIP_PLUS(3),
	MVP(4),
	MVP_PLUS(5);

	public static final IntFunction<PackageRank> BY_ID = ValueLists.createIdToValueFunction(PackageRank::id, PackageRank.values(), PackageRank.NONE);
	private final int id;

	PackageRank(int id) {
		this.id = id;
	}

	public int id() {
		return this.id;
	}
}
