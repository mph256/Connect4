package com.mph.services.interfaces;

import java.util.Set;

import com.mph.beans.Game;
import com.mph.beans.User;

public interface GameService {

	/**
	 * Adds a user to the first ranked pending game found as player 2, or as player 1 in a new game if no game is found.
	 *
	 * <br>
	 * The new ranked game is then put on hold until another player joins it.
	 *
	 * @param user the user looking for a ranked game
	 *
	 * @return the game found or the new game created
	 */
	public Game joinGame(User user);

	/**
	 * Creates a new casual game.
	 *
	 * <br>
	 * This method is called when a user accepts a game request from a friend.
	 *
	 * @param user1 the user who sent the game request
	 * @param user2 the user who accepted the game request
	 *
	 * @return the new game created
	 */
	public Game joinGame(User user1, User user2);

	/**
	 * Adds a user to a game's spectator list.
	 *
	 * @param user the user looking for a game as spectator
	 * @param gameId the selected game
	 *
	 * @return the updated game
	 */
	public Game observeGame(long gameId, User user);

	/**
	 * Removes a connected user from a game.
	 *
	 * <br>
	 * If this user is one of the 2 players, ends the game.
	 * <br>
	 * If the game is over, updates the players score if the game is a ranked game and removes it from the list of observable games.
	 *
	 * @param user the user
	 * @param gameId the game
	 */
	public void leaveGame(long gameId, User user);

	/**
	 * Removes a disconnected user from a game.
	 *
	 * <br>
	 * If this user is one of the 2 players, ends the game.
	 * <br>
	 * If the game is over updates the players score if the game is a ranked game and removes it from the list of observable games.
	 *
	 * @param gameId the game
	 */
	public void leaveGame(long gameId);

	/**
	 * Drops a token in a column.
	 *
	 * <br>
	 * If after that the game is over, updates the players score if the game is a ranked game and removes it from the list of observable games.
	 * <br>
	 * Otherwise increments the game turn and restarts the timer.
	 *
	 * @param user the user
	 * @param gameId the game
	 * @param column the column
	 *
	 * @return the updated game
	 */
	public Game dropToken(long gameId, int column, User user);

	/**
	 * Rotates the board of a game to the left.
	 *
	 * <br>
	 * If after that the game is over, updates the players score if the game is a ranked game and removes it from the list of observable games.
	 * <br>
	 * Otherwise increments the game turn and restarts the timer.
	 *
	 * @param user the user
	 * @param gameId the game
	 *
	 * @return the updated game
	 */
	public Game rotateLeft(long gameId, User user);

	/**
	 * Rotates the board of a game to the right.
	 *
	 * <br>
	 * If after that the game is over, updates the players score if the game is a ranked game and removes it from the list of observable games.
	 * <br>
	 * Otherwise increments the game turn and restarts the timer.
	 *
	 * @param user the user
	 * @param gameId the game
	 *
	 * @return the updated game
	 */
	public Game rotateRight(long gameId, User user);

	public Game getGameById(long gameId);

	public Game getGameByUser(User user);

	public Set<Game> getGames();

}