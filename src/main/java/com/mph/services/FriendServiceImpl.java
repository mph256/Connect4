package com.mph.services;

import java.util.Set;
import java.util.TreeSet;

import com.mph.services.interfaces.FriendService;
import com.mph.services.interfaces.UserService;
import com.mph.services.interfaces.GameRequestService;

import com.mph.dao.DaoFactory;
import com.mph.dao.interfaces.FriendDao;

import com.mph.beans.User;
import com.mph.beans.GameRequest;

public class FriendServiceImpl implements FriendService {

	private UserService userService;

	private GameRequestService gameRequestService;

	private FriendDao friendDao;

	public FriendServiceImpl() {

		userService = new UserServiceImpl();
		gameRequestService = new GameRequestServiceImpl();

		friendDao = DaoFactory.getInstance().getFriendDao();

	}

	@Override
	public void removeFriend(User user, User friend) {

		friendDao.delete(user, friend);

		user.removeFriend(friend);
		user.removeOnlineFriend(friend);

		if(friend.isOnline()) {

			friend.removeFriend(user);
			friend.removeOnlineFriend(user);

			GameRequest gameRequest = friend.getGameRequestsSent()
				.stream()
				.filter(x -> user.equals(x.getReceiver()))
				.findFirst()
				.orElse(null);

			if(gameRequest != null)
				gameRequestService.declineGameRequest(gameRequest);

			gameRequest = friend.getGameRequestsReceived()
				.stream()
				.filter(x -> user.equals(x.getSender()))
				.findFirst()
				.orElse(null);

			if(gameRequest != null)
				gameRequestService.cancelGameRequest(gameRequest);

		}

	}

	@Override
	public Set<User> getFriendsByUser(User user) {

		Set<User> friends = new TreeSet<User>();

		Set<User> onlineUsers = userService.getOnlineUsers();

		for(User friend: friendDao.findByUser(user)) {

			if(onlineUsers.contains(friend))
				friends.add(userService.getUserByLogin(friend.getLogin()));
			else
				friends.add(friend);

		}

		return friends;

	}

}