package com.mph.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Date;
import java.sql.Timestamp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.mph.dao.interfaces.RegistrationDao;

import com.mph.beans.User;

public class RegistrationDaoImpl implements RegistrationDao {

	private DaoFactory daoFactory;

	public RegistrationDaoImpl(DaoFactory daoFactory) {
		this.daoFactory	= daoFactory;
	}

	@Override
	public boolean isLoginAlreadyInUse(String login) {

		boolean isLoginAlreadyInUse = true;

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

			isLoginAlreadyInUse = (resultSet.getInt(1) > 0);

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

		return isLoginAlreadyInUse;

	}

	@Override
	public boolean isEmailAlreadyInUse(String email) {

		boolean isEmailAlreadyInUse = true;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {

			connection = daoFactory.getConnection();

			String sql = "SELECT COUNT(*) FROM user WHERE email = ?";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, email);

			resultSet = preparedStatement.executeQuery();

			resultSet.next();

			isEmailAlreadyInUse = (resultSet.getInt(1) > 0);

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

		return isEmailAlreadyInUse;
	}

	@Override
	public void registerUser(User user) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = daoFactory.getConnection();

			String sql = "INSERT INTO user (login, email, password, profile_picture, registration_date, last_connection, is_online, is_in_game) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, user.getLogin());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setBlob(4, new FileInputStream(user.getProfilPicture().getPath()));
			preparedStatement.setDate(5, Date.valueOf(user.getRegistrationDate()));
			preparedStatement.setTimestamp(6, Timestamp.valueOf(user.getLastConnection()));
			preparedStatement.setBoolean(7, user.isOnline());
			preparedStatement.setBoolean(8, user.isInGame());

			preparedStatement.executeUpdate();

		} catch(FileNotFoundException | SQLException e) {
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