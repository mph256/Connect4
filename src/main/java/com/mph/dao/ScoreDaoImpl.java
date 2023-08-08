package com.mph.dao;

import java.util.Set;
import java.util.HashSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import java.sql.SQLException;

import com.mph.dao.interfaces.ScoreDao;

import com.mph.beans.Score;
import com.mph.beans.User;

public class ScoreDaoImpl implements ScoreDao {

	private DaoFactory daoFactory;

	ScoreDaoImpl(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void add(Score score) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = daoFactory.getConnection();

			String sql = "INSERT INTO score (user, wins, defeats, draws, winStreak, bestWinStreak) VALUES (?, ?, ?, ?, ?, ?)";

			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, score.getUser().getLogin());
			preparedStatement.setInt(2, score.getWins());
			preparedStatement.setInt(3, score.getDefeats());
			preparedStatement.setInt(4, score.getDraws());
			preparedStatement.setInt(5, score.getWinStreak());
			preparedStatement.setInt(6, score.getBestWinStreak());

			preparedStatement.executeUpdate();

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(preparedStatement != null)
					preparedStatement.close();
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void update(Score score, Set<String> fields) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = daoFactory.getConnection();

			StringBuilder builder = new StringBuilder("UPDATE score SET ");

			int i = 1;

			for(String field: fields) {

				builder.append(field);

				if(i != fields.size()) 
					builder.append(" = ?, ");
				else 
					builder.append(" = ? ");

				i++;

			}

			builder.append("WHERE user = ?");

			String sql = builder.toString();

			preparedStatement = connection.prepareStatement(sql);

			i = 1;

			for(String field: fields) {

				switch(field) {
					case "wins":
						preparedStatement.setInt(i, score.getWins());
						break;
					case "defeats":
						preparedStatement.setInt(i, score.getDefeats());
						break;
					case "draws":
						preparedStatement.setInt(i, score.getDraws());
						break;
					case "winStreak":
						preparedStatement.setInt(i, score.getWinStreak());
						break;
					case "bestWinStreak":
						preparedStatement.setInt(i, score.getBestWinStreak());
						break;
				}

				i++;

			}

			preparedStatement.setString(i, score.getUser().getLogin());

			preparedStatement.executeUpdate();

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(preparedStatement != null)
					preparedStatement.close();
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public Score findByUser(User user) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		Score score = null;

		try {

			connection = daoFactory.getConnection();

			String sql = "SELECT wins, defeats, draws, winStreak, bestWinStreak FROM score WHERE user = ?";

			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, user.getLogin());

			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {

				score = new Score(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), 
					resultSet.getInt(4), resultSet.getInt(5), 
					user);

			}

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null)
					resultSet.close();
				if(preparedStatement != null)
					preparedStatement.close();
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}

		return score;

	}

	@Override
	public Set<Score> findAll() {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		Set<Score> scores = new HashSet<Score>();

		try {

			connection = daoFactory.getConnection();

			String sql = "SELECT user, wins, defeats, draws, winStreak, bestWinStreak FROM score";

			statement = connection.createStatement();

			resultSet = statement.executeQuery(sql);

			while(resultSet.next()) {

				Score score = new Score(resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4),
					resultSet.getInt(5), resultSet.getInt(6),
					new User(resultSet.getString(1)));

				scores.add(score);

			}

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null)
					resultSet.close();
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}

		return scores;

	}

}