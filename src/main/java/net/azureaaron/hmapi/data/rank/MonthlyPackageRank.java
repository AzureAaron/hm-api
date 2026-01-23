package net.azureaaron.hmapi.data.rank;

import java.util.function.IntFunction;
import net.minecraft.util.ByIdMap;
import org.jetbrains.annotations.ApiStatus;

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
	public static final IntFunction<MonthlyPackageRank> BY_ID = ByIdMap.sparse(MonthlyPackageRank::id, MonthlyPackageRank.values(), MonthlyPackageRank.NONE);

	private final int id;

	MonthlyPackageRank(int id) {
		this.id = id;
	}

	@ApiStatus.Internal
	public int id() {
		return this.id;
	}
}
