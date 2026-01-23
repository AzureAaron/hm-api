package net.azureaaron.hmapi.data.rank;

import java.util.function.IntFunction;
import net.minecraft.util.ByIdMap;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a paid rank.
 */
public enum PackageRank implements RankType {
	NONE(1),
	VIP(2),
	VIP_PLUS(3),
	MVP(4),
	MVP_PLUS(5);

	@ApiStatus.Internal
	public static final IntFunction<PackageRank> BY_ID = ByIdMap.sparse(PackageRank::id, PackageRank.values(), PackageRank.NONE);

	private final int id;

	PackageRank(int id) {
		this.id = id;
	}

	@ApiStatus.Internal
	public int id() {
		return this.id;
	}
}
