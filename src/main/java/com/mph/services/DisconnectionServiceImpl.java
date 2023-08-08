package com.mph.services;

import java.util.Set;
import java.util.HashSet;

import com.mph.services.interfaces.DisconnectionService;
import com.mph.services.interfaces.UserService;
import com.mph.services.interfaces.GameRequestService;

import com.mph.beans.User;

public class DisconnectionServiceImpl implements DisconnectionService {

	private UserService userService;

	private GameRequestService gameRequestService;

	public DisconnectionServiceImpl() {

		userService = new UserServiceImpl();
		gameRequestService = new GameRequestServiceImpl();

	}

	@Override
	public void disconnectUser(User user) {

		updateUser(user);

		user.getOnlineFriends().forEach(x -> x.removeOnlineFriend(user));

		gameRequestService.cancelGameRequests(user.getGameRequestsSent());
		gameRequestService.declineGameRequests(user.getGameRequestsReceived());

	}

	private void updateUser(User user) {

		Set<String> fields = new HashSet<String>();

		if(user.isInGame()) {

			fields.add("isInGame");
			user.setInGame(false);

		}

		fields.add("isOnline");
		user.setOnline(false);

		userService.updateUser(user, fields);

	}

}