package com.mph.services;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.mph.services.interfaces.ConnectionService;
import com.mph.services.interfaces.UserService;
import com.mph.services.interfaces.FriendService;
import com.mph.services.interfaces.FriendRequestService;
import com.mph.services.interfaces.ScoreService;

import com.mph.services.exceptions.InvalidLoginException;
import com.mph.services.exceptions.InvalidPasswordException;

import com.mph.beans.User;
import com.mph.beans.GameRequest;

public class ConnectionServiceImpl implements ConnectionService {

	private UserService userService;

	private FriendService friendService;

	private FriendRequestService friendRequestService;

	private ScoreService scoreService;

	public ConnectionServiceImpl() {

		userService = new UserServiceImpl();
		friendService = new FriendServiceImpl();
		friendRequestService = new FriendRequestServiceImpl();
		scoreService = new ScoreServiceImpl();

	}

	@Override
	public User connectUser(String login, String password) throws InvalidLoginException, InvalidPasswordException {

		User user = userService.getUserByLogin(login);

		checkLogin(user);
		checkPassword(user, password);

		updateUser(user);

		Set<User> friends = friendService.getFriendsByUser(user);
		Set<User> onlineFriends = friends.stream().filter(User::isOnline).collect(Collectors.toSet());

		user.setFriends(friends);
		user.setOnlineFriends(onlineFriends);

		onlineFriends.forEach(x -> x.addOnlineFriend(user));

		user.setFriendRequestsSent(friendRequestService.getFriendRequestsSentByUser(user));
		user.setFriendRequestsReceived(friendRequestService.getFriendRequestsReceivedByUser(user));

		user.setGameRequestsSent(new HashSet<GameRequest>());
		user.setGameRequestsReceived(new HashSet<GameRequest>());

		user.setScore(scoreService.getScoreByUser(user));

		return user;

	}

	private void checkLogin(User user) throws InvalidLoginException {
		userService.checkLogin(user);
	}

	private void checkPassword(User user, String password) throws InvalidPasswordException {
		userService.checkPassword(user, password);
	}

	private void updateUser(User user) {

		Set<String> fields = new HashSet<String>();

		fields.add("lastConnection");
		user.setLastConnection(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

		fields.add("isOnline");
		user.setOnline(true);

		userService.updateUser(user, fields);

	}

}