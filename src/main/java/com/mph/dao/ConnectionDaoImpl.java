package com.mph.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Timestamp;

import java.time.LocalDateTime;

import com.mph.dao.interfaces.ConnectionDao;

import com.mph.beans.User;

public class ConnectionDaoImpl implements ConnectionDao {

	private DaoFactory daoFactory;

	public ConnectionDaoImpl(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public boolean isRegisteredUser(String login) {

		boolean isRegisteredUser = false;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {

			connection = daoFactory.getConnection();

			String sql = "SELECT COUNT(*) FROM user WHERE login = ?";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, login);

			resultSet = preparedStatement.executeQuery();

			resultSet.next();

			isRegisteredUser = (resultSet.getInt(1) > 0);

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

		return isRegisteredUser;

	}

	@Override
	public boolean isCorrectPassword(String login, String password) {

		boolean isCorrectPassword = false;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {

			connection = daoFactory.getConnection();

			String sql = "SELECT password FROM user WHERE login = ?";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, login);

			resultSet = preparedStatement.executeQuery();

			resultSet.next();

			isCorrectPassword = password.equals(resultSet.getString(1));

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

		return isCorrectPassword;
	}

	@Override
	public void connectUser(User user) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = daoFactory.getConnection();

			String sql = "UPDATE user SET last_connection = ?, is_online = ? WHERE login = ?";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
			preparedStatement.setBoolean(2, true);
			preparedStatement.setString(3, user.getLogin());

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

}