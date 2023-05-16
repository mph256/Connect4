package com.mph.dao;

import java.util.List;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import com.mph.util.Util;

import com.mph.dao.interfaces.RegistrationDao;
import com.mph.dao.interfaces.ConnectionDao;
import com.mph.dao.interfaces.DisconnectionDao;

public final class DaoFactory {

	private String driver;

	private String url;

	private String username;

	private String password;

	private static DaoFactory instance;

	private DaoFactory() {
		loadProperties();
		loadDriver();
	}

	public static DaoFactory getInstance() {
		if(instance == null)
			instance = new DaoFactory();
		return instance;
	}

	public RegistrationDao getRegistrationDao() {
		return new RegistrationDaoImpl(this);
	}

	public ConnectionDao getConnectionDao() {
		return new ConnectionDaoImpl(this);
	}

	public DisconnectionDao getDisconnectionDao() {
		return new DisconnectionDaoImpl(this);
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