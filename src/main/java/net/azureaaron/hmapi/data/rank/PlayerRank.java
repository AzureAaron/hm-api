package net.azureaaron.hmapi.data.rank;

import java.util.function.IntFunction;

import net.minecraft.util.function.ValueLists;

/**
 * Represents a base rank type.
 */
public enum PlayerRank {
	NORMAL(1),
	YOUTUBER(2),
	GAME_MASTER(3),
	ADMIN(4);

	public static final IntFunction<PlayerRank> BY_ID = ValueLists.createIdToValueFunction(PlayerRank::id, PlayerRank.values(), PlayerRank.NORMAL);
	private final int id;

	PlayerRank(int id) {
		this.id = id;
	}

	public int id() {
		return this.id;
	}
}
