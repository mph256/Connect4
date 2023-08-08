package com.mph.services;

import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import com.mph.services.interfaces.ScoreService;

import com.mph.dao.DaoFactory;
import com.mph.dao.interfaces.ScoreDao;

import com.mph.beans.Score;
import com.mph.beans.User;

import com.mph.comparators.ScoreComparator;

public class ScoreServiceImpl implements ScoreService {

	private ScoreDao scoreDao;

	public ScoreServiceImpl() {
		scoreDao = DaoFactory.getInstance().getScoreDao();
	}

	public Score createScore(User user) {

		Score score = new Score(0, 0, 0, 0, 0, user);

		scoreDao.add(score);

		return score;

	}

	public void updateScore(Score score, Set<String> fields) {
		scoreDao.update(score, fields);
	}

	public Score getScoreByUser(User user) {
		return scoreDao.findByUser(user);
	}

	@Override
	public Set<Score> getScores() {

		ScoreComparator comparator = new ScoreComparator();

		List<Score> scores = scoreDao.findAll()
			.stream()
			.sorted((x, y) -> comparator.compare(x, y))
			.collect(Collectors.toList());

		return new LinkedHashSet<Score>(scores);

	}

}