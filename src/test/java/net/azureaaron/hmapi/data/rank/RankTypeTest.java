package net.azureaaron.hmapi.data.rank;

import java.util.Comparator;

import org.junit.jupiter.api.Assertions;

public class RankTypeTest {

	//Test Effective Rank

	void testNoPackageRankIsEffectiveRank() {
		Assertions.assertEquals(RankType.getEffectiveRank(PlayerRank.NORMAL, PackageRank.NONE, MonthlyPackageRank.NONE), PackageRank.NONE);
	}

	void testVipIsEffectiveRank() {
		Assertions.assertEquals(RankType.getEffectiveRank(PlayerRank.NORMAL, PackageRank.VIP, MonthlyPackageRank.NONE), PackageRank.VIP);
	}

	void testVipPlusIsEffectiveRank() {
		Assertions.assertEquals(RankType.getEffectiveRank(PlayerRank.NORMAL, PackageRank.VIP_PLUS, MonthlyPackageRank.NONE), PackageRank.VIP_PLUS);
	}

	void testMvpIsEffectiveRank() {
		Assertions.assertEquals(RankType.getEffectiveRank(PlayerRank.NORMAL, PackageRank.MVP, MonthlyPackageRank.NONE), PackageRank.MVP);
	}

	void testMvpPlusIsEffectiveRank() {
		Assertions.assertEquals(RankType.getEffectiveRank(PlayerRank.NORMAL, PackageRank.MVP_PLUS, MonthlyPackageRank.NONE), PackageRank.MVP_PLUS);
	}

	void testMvpPlusPlusIsEffectiveRank() {
		Assertions.assertEquals(RankType.getEffectiveRank(PlayerRank.NORMAL, PackageRank.MVP_PLUS, MonthlyPackageRank.SUPERSTAR), MonthlyPackageRank.SUPERSTAR);
	}

	void testYouTubeRankIsEffectiveRank() {
		Assertions.assertEquals(RankType.getEffectiveRank(PlayerRank.YOUTUBER, PackageRank.MVP_PLUS, MonthlyPackageRank.NONE), PlayerRank.YOUTUBER);
	}

	void testGameMasterIsEffectiveRank() {
		Assertions.assertEquals(RankType.getEffectiveRank(PlayerRank.GAME_MASTER, PackageRank.MVP_PLUS, MonthlyPackageRank.SUPERSTAR), PlayerRank.GAME_MASTER);
	}

	void testAdminIsEffectiveRank() {
		Assertions.assertEquals(RankType.getEffectiveRank(PlayerRank.ADMIN, PackageRank.NONE, MonthlyPackageRank.SUPERSTAR), PlayerRank.ADMIN);
	}

	//Test Comparisons

	void testNormalPlayerRankEqualToNoMonthlyPackageRank() {
		Assertions.assertTrue(RankType.compare(PlayerRank.NORMAL, MonthlyPackageRank.NONE) == 0, "PlayerRank#NORMAL was not equal to MonthlyPackageRank#NONE");
	}

	void testNoPackageRankAboveNoMonthlyPackageRank() {
		Assertions.assertTrue(RankType.compare(PackageRank.NONE, MonthlyPackageRank.NONE) > 0, "PackageRank#NONE was not above MonthlyPackageRank#NONE");
	}

	void testVipBelowMvpPlus() {
		Assertions.assertTrue(RankType.compare(PackageRank.VIP, PackageRank.MVP_PLUS) < 0, "VIP was not below MVP+");
	}

	void testMvpPlusIsSameAsMvpPlus() {
		Assertions.assertTrue(RankType.compare(PackageRank.MVP_PLUS, PackageRank.MVP_PLUS) == 0, "MVP+ was not the same as MVP+? wtf");
	}

	void testMvpPlusPlusIsHigherThanMvpPlus() {
		Assertions.assertTrue(RankType.compare(MonthlyPackageRank.SUPERSTAR, PackageRank.MVP_PLUS) > 0, "MVP++ was not above MVP+");
	}

	void testYouTuberBelowAdmin() {
		Assertions.assertTrue(RankType.compare(PlayerRank.YOUTUBER, PlayerRank.ADMIN) < 0, "YOUTUBER was not below ADMIN");
	}

	void testGameMasterAboveVipPlusAsMethodReference() {
		Comparator<RankType> comparator = RankType::compare;

		Assertions.assertTrue(comparator.compare(PlayerRank.GAME_MASTER, PackageRank.VIP_PLUS) > 0, "Game Master was not above VIP+");
	}

	void testAdminAboveMvpPlus() {
		Assertions.assertTrue(RankType.compare(PlayerRank.ADMIN, PackageRank.MVP_PLUS) > 0, "ADMIN was not above MVP+");
	}
}
