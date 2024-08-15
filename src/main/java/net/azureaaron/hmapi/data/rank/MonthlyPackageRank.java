package net.azureaaron.hmapi.data.rank;

import java.util.function.IntFunction;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.util.function.ValueLists;

/**
 * Represents a monthly subscription rank, only relevant for MVP++ right now.
 */
public enum MonthlyPackageRank implements RankType {
	NONE(1),
	/**
	 * MVP++
	 */
	SUPERSTAR(2);

	@ApiStatus.Internal
	public static final IntFunction<MonthlyPackageRank> BY_ID = ValueLists.createIdToValueFunction(MonthlyPackageRank::id, MonthlyPackageRank.values(), MonthlyPackageRank.NONE);

	private final int id;

	MonthlyPackageRank(int id) {
		this.id = id;
	}

	@ApiStatus.Internal
	public int id() {
		return this.id;
	}
}
