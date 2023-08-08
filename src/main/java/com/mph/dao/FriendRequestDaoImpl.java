package com.mph.dao;

import java.util.Set;
import java.util.HashSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

import com.mph.dao.interfaces.FriendRequestDao;

import com.mph.beans.FriendRequest;
import com.mph.beans.RequestState;
import com.mph.beans.User;

public class FriendRequestDaoImpl implements FriendRequestDao {

	private DaoFactory daoFactory;

	FriendRequestDaoImpl(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void add(FriendRequest friendRequest) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = daoFactory.getConnection();

			String sql = "INSERT INTO user_user (user1_login, user2_login, confirmed) VALUES (?, ?, ?)";

			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, friendRequest.getSender().getLogin());
			preparedStatement.setString(2, friendRequest.getReceiver().getLogin());
			preparedStatement.setBoolean(3, false);

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
	public void update(FriendRequest friendRequest) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = daoFactory.getConnection();

			if(RequestState.ACCEPTED.equals(friendRequest.getState())) {

				String sql = "UPDATE user_user SET confirmed = ? WHERE user1_login = ? AND user2_login = ?";

				preparedStatement = connection.prepareStatement(sql);

				preparedStatement.setBoolean(1, true);
				preparedStatement.setString(2, friendRequest.getSender().getLogin());
				preparedStatement.setString(3, friendRequest.getReceiver().getLogin());

				preparedStatement.executeUpdate();

			}

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
	public void delete(FriendRequest friendRequest) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = daoFactory.getConnection();

			String sql = "DELETE FROM user_user WHERE user1_login = ? AND user2_login = ? AND confirmed = ?";

			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, friendRequest.getSender().getLogin());
			preparedStatement.setString(2, friendRequest.getReceiver().getLogin());
			preparedStatement.setBoolean(3, false);

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
	public Set<FriendRequest> findBySender(User user) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		Set<FriendRequest> friendRequestsSent = new HashSet<FriendRequest>();

		try {

			connection = daoFactory.getConnection();

			String sql = "SELECT user2_login FROM user_user WHERE user1_login = ? AND confirmed = ?";

			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, user.getLogin());
			preparedStatement.setBoolean(2, false);

			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {

				friendRequestsSent.add(new FriendRequest(
					user,
					new User(resultSet.getString(1))));

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

		return friendRequestsSent;

	}

	@Override
	public Set<FriendRequest> findByReceiver(User user) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		Set<FriendRequest> friendRequestsReceived = new HashSet<FriendRequest>();

		try {

			connection = daoFactory.getConnection();

			String sql = "SELECT user1_login FROM user_user WHERE user2_login = ? AND confirmed = ?";

			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, user.getLogin());
			preparedStatement.setBoolean(2, false);

			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {

				friendRequestsReceived.add(new FriendRequest(
					new User(resultSet.getString(1)),
					user));

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

		return friendRequestsReceived;

	}

}