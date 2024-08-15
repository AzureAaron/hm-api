package net.azureaaron.hmapi.data.rank;

import org.jetbrains.annotations.NotNull;

/**
 * Allows for a common type between all rank-related enums, also contains utility methods
 * for handling the three {@code RankType} enums.
 */
public sealed interface RankType permits MonthlyPackageRank, PackageRank, PlayerRank {

	/**
	 * Allows for determining a player's "effective rank", the logic is as follows:<br><Br>
	 * 
	 * If the player has a YouTuber or staff rank then this method returns the {@code baseRank}.<br>
	 * If the player's {@code baseRank} is {@link PlayerRank#NORMAL} and the {@code monthlyPackageRank} is not
	 * {@link MonthlyPackageRank#NONE} then this method returns the {@code monthlyPackageRank}.<br><br>
	 * 
	 * Otherwise the {@code packageRank} is returned.
	 * 
	 * @param baseRank           the player's base rank.
	 * @param packageRank        the player's paid rank.
	 * @param monthlyPackageRank the player's monthly subscription rank.
	 * 
	 * @return a {@code RankType} instance representing the player's effective rank
	 */
	@NotNull
	static RankType getEffectiveRank(@NotNull PlayerRank baseRank, @NotNull PackageRank packageRank, @NotNull MonthlyPackageRank monthlyPackageRank) {
		return switch (baseRank) {
			//Staff ranks take precedence
			case PlayerRank.YOUTUBER, PlayerRank.GAME_MASTER, PlayerRank.ADMIN -> baseRank;

			case PlayerRank.NORMAL -> {
				yield switch (monthlyPackageRank) {
					case MonthlyPackageRank.NONE -> packageRank;
					case MonthlyPackageRank.SUPERSTAR -> monthlyPackageRank;
				};
			}
		};
	}

	/**
	 * Enables comparisons between two {@code RankTypes}. This can also be used as a {@link java.util.Comparator Comparator}
	 * via a method reference ({@code RankType::compare}).<br><br>
	 * 
	 * The ranks are compared in ascending order:<br>
	 * {@link PlayerRank#NORMAL}, {@link MonthlyPackageRank#NONE} (equal to eachother)<br>
	 * {@link PackageRank#NONE}<br>
	 * {@link PackageRank#VIP}<br>
	 * {@link PackageRank#VIP_PLUS}<br>
	 * {@link PackageRank#MVP}<br>
	 * {@link PackageRank#MVP_PLUS}<br>
	 * {@link MonthlyPackageRank#SUPERSTAR}<br>
	 * {@link PlayerRank#YOUTUBER}<br>
	 * {@link PlayerRank#GAME_MASTER}<br>
	 * {@link PlayerRank#ADMIN}
	 * 
	 * @see {@link java.util.Comparator Comparator}
	 * @see {@link java.util.Comparator#compare(Object, Object)}
	 */
	static int compare(@NotNull RankType first, @NotNull RankType second) {
		return Integer.compare(getRankWeight(first), getRankWeight(second));
	}

	/**
	 * Applies a weighting to each rank depending on its place in Hypixel's rank hierarchy allowing for
	 * each {@code RankType} enum constant to be ordered relative to another.
	 */
	private static int getRankWeight(@NotNull RankType rank) {
		return switch (rank) {
			case PlayerRank.NORMAL, MonthlyPackageRank.NONE -> -1; //Weighted less because these are more placeholders and cannot be returned as an effective rank.
			case PackageRank.NONE -> 0;
			case PackageRank.VIP -> 1;
			case PackageRank.VIP_PLUS -> 2;
			case PackageRank.MVP -> 3;
			case PackageRank.MVP_PLUS -> 4;
			case MonthlyPackageRank.SUPERSTAR -> 5;
			case PlayerRank.YOUTUBER -> 6;
			case PlayerRank.GAME_MASTER -> 7;
			case PlayerRank.ADMIN -> 8;

			default -> throw new IllegalArgumentException("Unexpected value: " + rank);
		};
	}
}
