package com.mph.services;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import com.mph.services.interfaces.GameRequestService;
import com.mph.services.interfaces.GameService;
import com.mph.services.interfaces.UserService;

import com.mph.services.exceptions.UserAlreadyOfflineException;
import com.mph.services.exceptions.UserAlreadyInGameException;
import com.mph.services.exceptions.RequestAlreadySentException;
import com.mph.services.exceptions.RequestAlreadyReceivedException;

import com.mph.beans.Game;
import com.mph.beans.GameRequest;
import com.mph.beans.RequestState;
import com.mph.beans.User;

public class GameRequestServiceImpl implements GameRequestService {

	private GameService gameService;

	private UserService userService;

	public GameRequestServiceImpl() {

		gameService = new GameServiceImpl();
		userService = new UserServiceImpl();

	}

	@Override
	public void sendGameRequest(User sender, String login) throws UserAlreadyOfflineException, UserAlreadyInGameException,
		RequestAlreadySentException, RequestAlreadyReceivedException {

		User receiver = userService.getUserByLogin(login);

		checkReceiver(receiver);
		checkRequestsSent(sender, receiver);
		checkRequestsReceived(sender, receiver);

		GameRequest gameRequest = new GameRequest(sender, receiver);

		sender.addGameRequestSent(gameRequest);
		receiver.addGameRequestReceived(gameRequest);

	}

	@Override
	public void cancelGameRequest(GameRequest gameRequest) {

		gameRequest.setState(RequestState.CANCELED);

		gameRequest.getSender().removeGameRequestSent(gameRequest);
		gameRequest.getReceiver().removeGameRequestReceived(gameRequest);

	}

	@Override
	public Game acceptGameRequest(GameRequest gameRequest) {

		Game game = null;

		gameRequest.setState(RequestState.ACCEPTED);

		game = gameService.joinGame(gameRequest.getSender(), gameRequest.getReceiver());

		gameRequest.setGame(game);

		User receiver = gameRequest.getReceiver();

		receiver.removeGameRequestReceived(gameRequest);

		cancelGameRequests(receiver.getGameRequestsSent());
		declineGameRequests(receiver.getGameRequestsReceived());

		return game;

	}

	@Override
	public void declineGameRequest(GameRequest gameRequest) {

		gameRequest.setState(RequestState.DECLINED);

		gameRequest.getSender().removeGameRequestSent(gameRequest);
		gameRequest.getReceiver().removeGameRequestReceived(gameRequest);

	}

	@Override
	public void cancelGameRequests(Set<GameRequest> gameRequests) {
		gameRequests.forEach(x -> cancelGameRequest(x));
	}

	@Override
	public void declineGameRequests(Set<GameRequest> gameRequests) {
		gameRequests.forEach(x -> declineGameRequest(x));
	}

	@Override
	public Set<GameRequest> getGameRequestsReceivedByUser(User user) {

		return new LinkedHashSet<GameRequest>(
			user.getGameRequestsReceived()
			.stream()
			.sorted((x, y) -> (x.getSender()).compareTo(y.getSender()))
			.collect(Collectors.toSet())
		);

	}

	private void checkReceiver(User user) throws UserAlreadyOfflineException, UserAlreadyInGameException {

		if(!user.isOnline())
			throw new UserAlreadyOfflineException();

		if(user.isInGame())
			throw new UserAlreadyInGameException();

	}

	private void checkRequestsSent(User sender, User receiver) throws RequestAlreadySentException {

		if(requestAlreadySent(sender, receiver))
			throw new RequestAlreadySentException("Vous avez déjà envoyé une demande de jeu à ce joueur.");

	}

	private void checkRequestsReceived(User sender, User receiver) throws RequestAlreadyReceivedException {

		if(requestAlreadyReceived(sender, receiver))
			throw new RequestAlreadyReceivedException("Ce joueur vous a déjà envoyé une demande de jeu.");

	}

	private boolean requestAlreadySent(User sender, User receiver) {

		for(GameRequest gameRequest: sender.getGameRequestsSent()) {

			if(receiver.equals(gameRequest.getReceiver()))
				return true;

		}

		return false;

	}

	private boolean requestAlreadyReceived(User sender, User receiver) {

		for(GameRequest gameRequest: sender.getGameRequestsReceived()) {

			if(receiver.equals(gameRequest.getSender()))
				return true;

		}

		return false;

	}

}