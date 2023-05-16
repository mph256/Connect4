package com.mph.services;

import com.mph.services.interfaces.DisconnectionService;

import com.mph.dao.DaoFactory;
import com.mph.dao.interfaces.DisconnectionDao;

import com.mph.beans.User;

public class DisconnectionServiceImpl implements DisconnectionService {

	private DisconnectionDao disconnectionDao;

	public DisconnectionServiceImpl() {
		disconnectionDao = DaoFactory.getInstance().getDisconnectionDao();
	}

	@Override
	public void disconnectUser(String login) {

		User user = new User(login);
		disconnectionDao.disconnectUser(user);

	}

}