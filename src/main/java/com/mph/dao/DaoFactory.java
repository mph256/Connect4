package com.mph.dao;

import java.util.List;

import java.sql.DriverManager;
import java.sql.Connection;

import java.sql.SQLException;

import com.mph.dao.interfaces.UserDao;
import com.mph.dao.interfaces.FriendDao;
import com.mph.dao.interfaces.FriendRequestDao;
import com.mph.dao.interfaces.ScoreDao;

import com.mph.util.Util;

public final class DaoFactory {

	private static DaoFactory instance;

	private String driver;

	private String url;

	private String username;

	private String password;

	private DaoFactory() {

		loadProperties();
		loadDriver();

	}

	public static DaoFactory getInstance() {

		if(instance == null)
			instance = new DaoFactory();

		return instance;

	}

	public UserDao getUserDao() {
		return new UserDaoImpl(this);
	}

	public FriendDao getFriendDao() {
		return new FriendDaoImpl(this);
	}

	public FriendRequestDao getFriendRequestDao() {
		return new FriendRequestDaoImpl(this);
	}

	public ScoreDao getScoreDao() {
		return new ScoreDaoImpl(this);
	}

    public Connection getConnection() throws SQLException {

        Connection connection = DriverManager.getConnection(url, username, password);

        return connection;

    }

	private void loadProperties() {

		List<String> properties = Util.getProperties(new String[]{ "db.driver", "db.url", "db.username", "db.password" });

		driver = properties.get(0);
		url = properties.get(1);
		username = properties.get(2);
		password = properties.get(3);

	}

	private void loadDriver() {

		try {
			Class.forName(driver);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}