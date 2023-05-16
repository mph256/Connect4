package com.mph.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mph.dao.interfaces.DisconnectionDao;

import com.mph.beans.User;

public class DisconnectionDaoImpl implements DisconnectionDao {

	private DaoFactory daoFactory;

	public DisconnectionDaoImpl(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void disconnectUser(User user) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = daoFactory.getConnection();

			String sql = "UPDATE user SET is_online = ? WHERE login = ?";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setBoolean(1, false);
			preparedStatement.setString(2, user.getLogin());

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