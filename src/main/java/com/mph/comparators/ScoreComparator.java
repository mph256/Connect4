package com.mph.comparators;

import java.util.Comparator;

import com.mph.beans.Score;

public class ScoreComparator implements Comparator<Score> {

	@Override
	public int compare(Score score1, Score score2) {

		double ratio1 = (score1.getDefeats() == 0)?score1.getWins():((double)score1.getWins()/(double)score1.getDefeats());
		double ratio2 = (score2.getDefeats() == 0)?score2.getWins():((double)score2.getWins()/(double)score2.getDefeats());

		if(ratio1 == ratio2) {

			int wins1 = score1.getWins();
			int wins2 = score2.getWins();

			if(score1.getWins() == score2.getWins()) {

				int defeats1 = score1.getDefeats();
				int defeats2 = score2.getDefeats();

				if(score1.getDefeats() == score2.getDefeats()) {

					int bestWinStreak1 = score1.getBestWinStreak();
					int bestWinStreak2 = score2.getBestWinStreak();

					if(bestWinStreak1 == bestWinStreak2)
						return Integer.compare(score2.getDraws(), score1.getDraws());
					else
						return Integer.compare(bestWinStreak2, bestWinStreak1);

				} else
					return Integer.compare(defeats1, defeats2);

			} else
				return Integer.compare(wins2, wins1);

		} else
			return Double.compare(ratio2, ratio1);

	}

}