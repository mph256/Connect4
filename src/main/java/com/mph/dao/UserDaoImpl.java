package com.mph.dao;

import java.util.Set;
import java.util.HashSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Date;
import java.sql.Timestamp;

import com.mph.dao.interfaces.UserDao;

import com.mph.beans.User;

public class UserDaoImpl implements UserDao {

	private DaoFactory daoFactory;

	UserDaoImpl(DaoFactory daoFactory) {
		this.daoFactory	= daoFactory;
	}

	@Override
	public void add(User user) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = daoFactory.getConnection();

			String sql = "INSERT INTO user (login, email, password, registration_date, last_connection, is_online, is_in_game) VALUES (?, ?, ?, ?, ?, ?, ?)";

			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, user.getLogin());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setDate(4, Date.valueOf(user.getRegistrationDate()));
			preparedStatement.setTimestamp(5, Timestamp.valueOf(user.getLastConnection()));
			preparedStatement.setBoolean(6, user.isOnline());
			preparedStatement.setBoolean(7, user.isInGame());

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
	public void update(User user, Set<String> fields) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = daoFactory.getConnection();

			StringBuilder builder = new StringBuilder("UPDATE user SET ");

			int i = 1;

			for(String field: fields) {

				builder.append(field.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase());

				if(i != fields.size()) 
					builder.append(" = ?, ");
				else 
					builder.append(" = ? ");

				i++;

			}

			builder.append("WHERE login = ?");

			String sql = builder.toString();

			preparedStatement = connection.prepareStatement(sql);

			i = 1;

			for(String field: fields) {

				switch(field) {
					case "email":
						preparedStatement.setString(i, user.getEmail());
						break;
					case "password":
						preparedStatement.setString(i, user.getPassword());
						break;
					case "lastConnection":
						preparedStatement.setTimestamp(i, Timestamp.valueOf(user.getLastConnection()));
						break;
					case "isOnline":
						preparedStatement.setBoolean(i, user.isOnline());
						break;
					case "isInGame":
						preparedStatement.setBoolean(i, user.isInGame());
						break;
				}

				i++;

			}

			preparedStatement.setString(i, user.getLogin());

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
	public void delete(User user) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = daoFactory.getConnection();

			String sql = "DELETE FROM user WHERE login = ?";

			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, user.getLogin());

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
	public User findByLogin(String login) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;;
		ResultSet resultSet = null;

		User user = null;

		try {

			connection = daoFactory.getConnection();

			String sql = "SELECT email, password, registration_date, last_connection, is_online, is_in_game FROM user WHERE login = ?";

			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, login);

			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {

				user = new User(login, resultSet.getString(1), resultSet.getString(2),
						resultSet.getDate(3).toLocalDate(), resultSet.getTimestamp(4).toLocalDateTime(),
						resultSet.getBoolean(5), resultSet.getBoolean(6));

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

		return user;

	}

	public User findByEmail(String email) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		User user = null;

		try {

			connection = daoFactory.getConnection();

			String sql = "SELECT login, password, registration_date, last_connection, is_online, is_in_game FROM user WHERE email = ?";

			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, email);

			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {

				user = new User(resultSet.getString(1), email, resultSet.getString(2),
					resultSet.getDate(3).toLocalDate(), resultSet.getTimestamp(4).toLocalDateTime(),
					resultSet.getBoolean(5), resultSet.getBoolean(6));

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

		return user;

	}

	@Override
	public Set<User> findAll() {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		Set<User> users =  new HashSet<User>();

		try {

			connection = daoFactory.getConnection();

			String sql = "SELECT login, email, password, registration_date, last_connection, is_online, is_in_game FROM user";

			statement = connection.createStatement();

			resultSet = statement.executeQuery(sql);

			while(resultSet.next()) {

				User user = new User(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
					resultSet.getDate(4).toLocalDate(), resultSet.getTimestamp(5).toLocalDateTime(),
					resultSet.getBoolean(6), resultSet.getBoolean(7));

				users.add(user);

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

		return users;

	}

}