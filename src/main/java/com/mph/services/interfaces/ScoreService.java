package com.mph.services.interfaces;

import java.util.Set;

import com.mph.beans.Score;
import com.mph.beans.User;

public interface ScoreService {

	/**
	 * Creates a new score for a user.
	 *
	 * @param user the user
	 *
	 * @return the new score created
	 */
	public Score createScore(User user);

	/**
	 * Updates an existing score.
	 *
	 * <br>
	 * The score given in argument must already be up to date.
	 * <br>
	 * The list of fields indicates which fields need to be updated in the database.
	 *
	 * @param score the score to update
	 * @param fields the list of score fields to update
	 */
	public void updateScore(Score score, Set<String> fields);

	public Score getScoreByUser(User user);

	public Set<Score> getScores();

}