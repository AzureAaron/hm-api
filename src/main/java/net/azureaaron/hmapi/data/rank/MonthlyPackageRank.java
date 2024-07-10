package net.azureaaron.hmapi.data.rank;

import java.util.function.IntFunction;

import net.minecraft.util.function.ValueLists;

public enum MonthlyPackageRank {
	NONE(1),
	/**
	 * MVP++
	 */
	SUPERSTAR(2);

	public static final IntFunction<MonthlyPackageRank> BY_ID = ValueLists.createIdToValueFunction(MonthlyPackageRank::id, MonthlyPackageRank.values(), MonthlyPackageRank.NONE);
	private final int id;

	MonthlyPackageRank(int id) {
		this.id = id;
	}

	public int id() {
		return this.id;
	}
}
