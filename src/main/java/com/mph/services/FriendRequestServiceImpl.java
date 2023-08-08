package com.mph.services;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import com.mph.services.interfaces.FriendRequestService;
import com.mph.services.interfaces.UserService;

import com.mph.services.exceptions.RequestAlreadySentException;
import com.mph.services.exceptions.RequestAlreadyReceivedException;

import com.mph.dao.DaoFactory;
import com.mph.dao.interfaces.FriendRequestDao;

import com.mph.beans.FriendRequest;
import com.mph.beans.RequestState;
import com.mph.beans.User;

public class FriendRequestServiceImpl implements FriendRequestService {

	private UserService userService;

	private FriendRequestDao friendRequestDao;

	public FriendRequestServiceImpl() {

		userService = new UserServiceImpl();

		friendRequestDao = DaoFactory.getInstance().getFriendRequestDao();

	}

	@Override
	public void sendFriendRequest(User sender, String login) throws RequestAlreadySentException, RequestAlreadyReceivedException {

		User receiver = userService.getUserByLogin(login);

		checkRequestsSent(sender, receiver);
		checkRequestsReceived(sender, receiver);

		FriendRequest friendRequest = new FriendRequest(sender, receiver);

		friendRequestDao.add(friendRequest);

		sender.addFriendRequestSent(friendRequest);

		if(receiver.isOnline())
			receiver.addFriendRequestReceived(friendRequest);

	}

	@Override
	public void cancelFriendRequest(FriendRequest friendRequest) {

		friendRequest.setState(RequestState.CANCELED);

		friendRequestDao.delete(friendRequest);

		friendRequest.getSender().removeFriendRequestSent(friendRequest);

		User receiver = friendRequest.getReceiver();

		if(receiver.isOnline())
			receiver.removeFriendRequestReceived(friendRequest);

	}

	@Override
	public void acceptFriendRequest(FriendRequest friendRequest) {

		friendRequest.setState(RequestState.ACCEPTED);

		friendRequestDao.update(friendRequest);

		User sender = friendRequest.getSender();
		User receiver = friendRequest.getReceiver();

		if(sender.isOnline()) {

			sender.removeFriendRequestSent(friendRequest);
			sender.addFriend(receiver);
			sender.addOnlineFriend(receiver);

			receiver.addOnlineFriend(sender);

		}

		receiver.removeFriendRequestReceived(friendRequest);
		receiver.addFriend(sender);

	}

	@Override
	public void declineFriendRequest(FriendRequest friendRequest) {

		friendRequest.setState(RequestState.DECLINED);

		friendRequestDao.delete(friendRequest);

		User sender = friendRequest.getSender();

		if(sender.isOnline())
			sender.removeFriendRequestSent(friendRequest);

		friendRequest.getReceiver().removeFriendRequestReceived(friendRequest);

	}

	@Override
	public void cancelFriendRequests(Set<FriendRequest> friendRequests) {
		friendRequests.forEach(x -> cancelFriendRequest(x));
	}

	@Override
	public void declineFriendRequests(Set<FriendRequest> friendRequests) {
		friendRequests.forEach(x -> declineFriendRequest(x));
	}

	@Override
	public Set<FriendRequest> getFriendRequestsSentByUser(User user) {

		return new LinkedHashSet<FriendRequest>(
			friendRequestDao.findBySender(user)
			.stream()
			.map(x -> { x.setReceiver(userService.getUserByLogin(x.getReceiver().getLogin())); return x; })
			.collect(Collectors.toSet())
		);

	}

	@Override
	public Set<FriendRequest> getFriendRequestsReceivedByUser(User user) {

		return new LinkedHashSet<FriendRequest>(
			friendRequestDao.findByReceiver(user)
			.stream()
			.map(x -> { x.setSender(userService.getUserByLogin(x.getSender().getLogin())); return x; })
			.sorted((x, y) -> (x.getSender()).compareTo(y.getSender()))
			.collect(Collectors.toSet())
		);

	}

	private void checkRequestsSent(User sender, User receiver) throws RequestAlreadySentException {

		if(requestAlreadySent(sender, receiver))
			throw new RequestAlreadySentException("Vous avez déjà envoyé une demande d'ami à ce joueur.");

	}

	private void checkRequestsReceived(User sender, User receiver) throws RequestAlreadyReceivedException {

		if(requestAlreadyReceived(sender, receiver))
			throw new RequestAlreadyReceivedException("Ce joueur vous a déjà envoyé une demande d'ami.");

	}

	private boolean requestAlreadySent(User sender, User receiver) {

		for(FriendRequest friendRequest: sender.getFriendRequestsSent()) {

			if(receiver.equals(friendRequest.getReceiver()))
				return true;

		}

		return false;

	}

	private boolean requestAlreadyReceived(User sender, User receiver) {

		for(FriendRequest friendRequest: sender.getFriendRequestsReceived()) {

			if(receiver.equals(friendRequest.getSender()))
				return true;

		}

		return false;

	}

}