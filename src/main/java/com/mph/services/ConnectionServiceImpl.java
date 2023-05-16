package com.mph.services;

import com.mph.services.interfaces.ConnectionService;

import com.mph.dao.DaoFactory;
import com.mph.dao.interfaces.ConnectionDao;

import com.mph.beans.User;

import com.mph.services.exceptions.InvalidLoginException;
import com.mph.services.exceptions.InvalidPasswordException;

public class ConnectionServiceImpl implements ConnectionService {

	private ConnectionDao connectionDao;

	public ConnectionServiceImpl() {
		connectionDao = DaoFactory.getInstance().getConnectionDao();
	}

	@Override
	public void connectUser(String login, String password) throws InvalidLoginException, InvalidPasswordException {

		checkLogin(login);
		checkPassword(login, password);

		User user = new User(login);
		connectionDao.connectUser(user);

	}

	private void checkLogin(String login) throws InvalidLoginException {
		if(!isRegisteredUser(login))
			throw new InvalidLoginException("Login incorrect.");
	}

	private void checkPassword(String login, String password) throws InvalidPasswordException {
		if(!isCorrectPassword(login, password))
			throw new InvalidPasswordException("Mot de passe incorrect.");
	}

	private boolean isRegisteredUser(String login) {
		return connectionDao.isRegisteredUser(login);
	}

	private boolean isCorrectPassword(String login, String password) {
		return connectionDao.isCorrectPassword(login, password);
	}

}