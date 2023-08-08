package com.mph.beans;

import java.util.Objects;

import java.io.Serializable;

public class Score implements Serializable {

	private static final long serialVersionUID = 1L;

	private int wins;

	private int defeats;

	private int draws;

	private int winStreak;

	private int bestWinStreak;

	private User user;

	public Score() {
	}

	public Score(int wins, int defeats, int draws, int winStreak, int bestWinStreak, User user) {

		this.wins = wins;
		this.defeats = defeats;
		this.draws = draws;

		this.winStreak = winStreak;
		this.bestWinStreak = bestWinStreak;

		this.user = user;

	}

	@Override
	public int hashCode() {
		return Objects.hash(user);
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;

		if(obj == null)
			return false;

		if(getClass() != obj.getClass())
			return false;

		Score other = (Score) obj;

		return Objects.equals(user, other.user);

	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getDefeats() {
		return defeats;
	}

	public void setDefeats(int defeats) {
		this.defeats = defeats;
	}

	public int getDraws() {
		return draws;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}

	public int getWinStreak() {
		return winStreak;
	}

	public void setWinStreak(int winStreak) {
		this.winStreak = winStreak;
	}

	public int getBestWinStreak() {
		return bestWinStreak;
	}

	public void setBestWinStreak(int bestWinStreak) {
		this.bestWinStreak = bestWinStreak;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}