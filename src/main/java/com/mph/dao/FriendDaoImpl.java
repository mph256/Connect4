package com.mph.dao;

import java.util.Set;
import java.util.HashSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

import com.mph.dao.interfaces.FriendDao;

import com.mph.beans.User;

public class FriendDaoImpl implements FriendDao {

	private DaoFactory daoFactory;

	FriendDaoImpl(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void delete(User user, User friend) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = daoFactory.getConnection();

			String sql = "DELETE FROM user_user WHERE (user1_login = ? AND user2_login = ?) "
				+ "OR (user2_login = ? AND user1_login = ?) AND confirmed = ?";

			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, user.getLogin());
			preparedStatement.setString(2, friend.getLogin());
			preparedStatement.setString(3, user.getLogin());
			preparedStatement.setString(4, friend.getLogin());
			preparedStatement.setBoolean(5, true);

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
	public Set<User> findByUser(User user) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		Set<User> friends = new HashSet<User>();

		try {

			connection = daoFactory.getConnection();

			String sql = "SELECT login, last_connection, is_online, is_in_game FROM user WHERE login IN ("
					+ "(SELECT user2_login AS login FROM user_user WHERE user1_login = ? AND confirmed = ?) "
					+ "UNION ALL (SELECT user1_login AS login FROM user_user WHERE user2_login = ? AND confirmed = ?)"
					+ ")";

			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, user.getLogin());
			preparedStatement.setBoolean(2, true);
			preparedStatement.setString(3, user.getLogin());
			preparedStatement.setBoolean(4, true);

			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {

				friends.add(new User(resultSet.getString(1),
					resultSet.getTimestamp(2).toLocalDateTime(),
					resultSet.getBoolean(3), resultSet.getBoolean(4)));

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

		return friends;

	}

}