package com.mph.services.interfaces;

import java.util.Set;

import com.mph.services.exceptions.UserAlreadyOfflineException;
import com.mph.services.exceptions.UserAlreadyInGameException;
import com.mph.services.exceptions.RequestAlreadySentException;
import com.mph.services.exceptions.RequestAlreadyReceivedException;

import com.mph.beans.Game;
import com.mph.beans.GameRequest;
import com.mph.beans.User;

public interface GameRequestService {

	/**
	 * Sends a game request to a friend.
	 *
	 * @param sender the user sending the friend request
	 * @param receiver the user receiving the friend request
	 *
	 * @throws UserAlreadyOfflineException if the requested user is already offline
	 * @throws UserAlreadyInGameException if the requested user is already in game
	 * @throws RequestAlreadySentException if the user sending the request has already sent a game request to this user
	 * @throws RequestAlreadyReceivedException if the user sending the request has already received a game request from this user
	 */
	public void sendGameRequest(User sender, String receiver) throws UserAlreadyOfflineException, UserAlreadyInGameException,
		RequestAlreadySentException, RequestAlreadyReceivedException;

	/**
	 * Cancels a game request sent.
	 *
	 * @param gameRequest the game request to cancel
	 */
	public void cancelGameRequest(GameRequest gameRequest);

	/**
	 * Accepts a game request received.
	 *
	 * <br>
	 * Cancels his game requests sent and declines his other game requests received.
	 *
	 * @param gameRequest the game request to accept
	 *
	 * @return the new game created
	 */
	public Game acceptGameRequest(GameRequest gameRequest);

	/**
	 * Declines a game request received.
	 *
	 * @param gameRequest the game request to decline
	 */
	public void declineGameRequest(GameRequest gameRequest);

	/**
	 * Cancels a set of game requests sent.
	 *
	 * @param gameRequests the game requests to cancel
	 */
	public void cancelGameRequests(Set<GameRequest> gameRequests);

	/**
	 * Declines a set of game requests received.
	 *
	 * @param gameRequests the game requests to decline
	 */
	public void declineGameRequests(Set<GameRequest> gameRequests);

	public Set<GameRequest> getGameRequestsReceivedByUser(User user);

}