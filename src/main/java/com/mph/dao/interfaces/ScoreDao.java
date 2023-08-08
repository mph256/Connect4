package com.mph.dao.interfaces;

import java.util.Set;

import com.mph.beans.Score;
import com.mph.beans.User;

public interface ScoreDao {

	public void add(Score score);

	public void update(Score score, Set<String> fields);

	public Score findByUser(User user);

	public Set<Score> findAll();

}