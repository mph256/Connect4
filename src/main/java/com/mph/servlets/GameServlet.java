package com.mph.servlets;

import java.util.Set;

import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.servlet.ServletException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mph.services.interfaces.GameService;
import com.mph.services.interfaces.GameRequestService;

import com.mph.services.GameServiceImpl;
import com.mph.services.GameRequestServiceImpl;

import com.mph.beans.Game;
import com.mph.beans.GameState;
import com.mph.beans.GameType;
import com.mph.beans.Square;
import com.mph.beans.Token;
import com.mph.beans.User;
import com.mph.beans.FriendRequest;

public class GameServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(GameServlet.class);

	private GameService gameService;

	private GameRequestService gameRequestService;

	@Override
	public void init() throws ServletException {

		gameService = new GameServiceImpl();
		gameRequestService = new GameRequestServiceImpl();

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String contentType = request.getContentType();

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		String url = request.getRequestURL().toString();
		String page = url.substring(url.lastIndexOf("/") + 1);

		if("game".equals(page)) {

			if("application/json".equals(contentType)) {

				JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

				long gameId = Long.parseLong(request.getParameter("id"));

				Game game = gameService.getGameById(gameId);

				if(user != null) {

					session.setAttribute("user", user);

					objectBuilder = refreshGame(game, user);

				}

				response.setContentType("application/json; charset=UTF-8");

				response.getWriter().write(objectBuilder.build().toString());

			} else {

				if(user == null)
					response.sendRedirect(request.getContextPath() + "/connection");
				else {

					Game game;

					String gameId = request.getParameter("id");

					if(gameId == null) {

						boolean isPageRefresh = user.isInGame();

						game = gameService.joinGame(user);

						gameId = String.valueOf(game.getId());

						if(!isPageRefresh) {

							if(game.getPlayer2() == null)
								logger.info("Partie " + gameId + " en attente | Joueur 1: " + user.getLogin());
							else
								logger.info("Partie " + gameId + " démarrée | Joueur 2: " + user.getLogin());

						}

						response.sendRedirect(request.getContextPath() + "/game?id=" + gameId);

					} else {

						game = gameService.getGameById(Long.parseLong(gameId));

						if(!user.equals(game.getPlayer1()) && !user.equals(game.getPlayer2()) && !game.getSpectators().contains(user)) {

							gameService.observeGame(Long.parseLong(gameId), user);

							logger.info("Partie " + gameId + " : | Nouveau spectateur: " + user.getLogin());

						}

						gameRequestService.cancelGameRequests(user.getGameRequestsSent());
						gameRequestService.declineGameRequests(user.getGameRequestsReceived());

						session.setAttribute("user", user);

						request.setAttribute("game", game);

						this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/game.jsp").forward(request, response);

					}

				}

			}

		} else if("games".equals(page)) {

			if("application/json".equals(contentType)) {

				JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

				String state = request.getParameter("state");

				if("running".equals(state))
					arrayBuilder = refreshGames(gameService.getGames());

				response.setContentType("application/json; charset=UTF-8");

				response.getWriter().write(arrayBuilder.build().toString());

			} else {

				if(user == null)
					response.sendRedirect(request.getContextPath() + "/connection");
				else {

					request.setAttribute("games", gameService.getGames());

					this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/games.jsp").forward(request, response);

				}

			}

		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String contentType = request.getContentType();

		if("application/json".equals(contentType)) {

			JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

			JsonReader reader = Json.createReader(request.getReader());
			JsonObject object = reader.readObject();

			long gameId = Long.parseLong(object.getString("gameId"));

			Game game = gameService.getGameById(gameId);

			if(game != null) {

				HttpSession session = request.getSession();
				User user = (User) session.getAttribute("user");

				String action = object.getString("action");

				if("leaveGame".equals(action)) {

					GameState oldState =  game.getState();

					if(user == null)
						gameService.leaveGame(gameId);
					else
						gameService.leaveGame(gameId, user);

					if((GameState.PENDING.equals(oldState)) || GameState.RUNNING.equals(oldState)) {

						GameState state =  game.getState();

						if(GameState.CANCELED.equals(state))
							logger.info("Partie " + gameId + " annulée");

						if(GameState.OVER.equals(game.getState())) {

							User winner = game.getWinner();

							if(winner != null)
								logger.info("Partie " + gameId + " terminée | Vainqueur: " + game.getWinner().getLogin());
							else
								logger.info("Partie " + gameId + " terminée | Match nul");

						}

					}

				} else {

					if(GameState.RUNNING.equals(game.getState())) {

						if("dropToken".equals(action)) {

							int column = Integer.parseInt(object.getString("column"));

							game = gameService.dropToken(gameId, column, user);

						}

						if("rotateLeft".equals(action))
							game = gameService.rotateLeft(gameId, user);

						if("rotateRight".equals(action))
							game = gameService.rotateRight(gameId, user);

						objectBuilder = refreshGame(game, user);

					}

				}

			}

			response.setContentType("application/json; charset=UTF-8");

			response.getWriter().write(objectBuilder.build().toString());

		}

	}

	private JsonObjectBuilder refreshGame(Game game, User user) {

		JsonObjectBuilder gameBuilder = Json.createObjectBuilder();

		User player1 = game.getPlayer1();
		User player2 = game.getPlayer2();

		GameState state =  game.getState();

		Square[][] squares = game.getBoard().getSquares();

		JsonObjectBuilder playerBuilder = Json.createObjectBuilder();

		playerBuilder.add("login", player1.getLogin());
		playerBuilder.add("winStreak", player1.getScore().getWinStreak());

		if(user.equals(player2)) {

			boolean isFriend = (user.getFriends()).contains(player1);

			playerBuilder.add("isFriend", isFriend);

			if(!isFriend) {

				boolean isRequested = false;

				for(FriendRequest friendRequest: user.getFriendRequestsSent()) {

					if(player1.equals(friendRequest.getReceiver())) {

						isRequested = true;
						break;

					}

				}

				playerBuilder.add("isRequested", isRequested);

			}

		}

		gameBuilder.add("player1", playerBuilder);

		if(player2 != null) {

			playerBuilder.add("login", player2.getLogin());
			playerBuilder.add("profilePicture", player2.getProfilePicture().getName());
			playerBuilder.add("winStreak", player2.getScore().getWinStreak());

			if(user.equals(player1)) {

				boolean isFriend = (user.getFriends()).contains(player2);

				playerBuilder.add("isFriend", isFriend);

				if(!isFriend) {

					boolean isRequested = false;

					for(FriendRequest friendRequest: user.getFriendRequestsSent()) {

						if(player2.equals(friendRequest.getReceiver())) {

							isRequested = true;
							break;

						}

					}

					playerBuilder.add("isRequested", isRequested);

				}

			}

		} else
			playerBuilder.add("login", "");

		gameBuilder.add("player2", playerBuilder);

		gameBuilder.add("state", state.toString());

		if(GameState.OVER.equals(state)) {

			User winner = game.getWinner();

			if(winner != null)
				gameBuilder.add("winner", winner.getLogin());
			else
				gameBuilder.add("winner", "");

		}

		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

		for(int i = 0; i < squares.length; i++) {

			JsonArrayBuilder rowBuilder = Json.createArrayBuilder();

			for(int j = 0; j < squares[i].length; j++) {

				Token token = squares[i][j].getToken();

				rowBuilder.add((token == null)?"":token.getColor().toString());

			}

			arrayBuilder.add(rowBuilder);

		}

		gameBuilder.add("squares", arrayBuilder);

		gameBuilder.add("turn", game.getTurn());
		gameBuilder.add("timer", game.getTimer());

		return gameBuilder;

	}

	private JsonArrayBuilder refreshGames(Set<Game> games) {

		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		JsonObjectBuilder gameBuilder = Json.createObjectBuilder();

		for(Game game: games) {

			LocalDateTime start = game.getStart();

			if(start != null) {

				gameBuilder.add("id", game.getId());

				GameType type = game.getType();

				gameBuilder.add("type", GameType.RANKED.equals(type)?"Classée":"Amicale");

				User player1 = game.getPlayer1();
				User player2 = game.getPlayer2();

				JsonObjectBuilder playerBuilder = Json.createObjectBuilder();

				playerBuilder.add("login", player1.getLogin());
				playerBuilder.add("profilePicture", player1.getProfilePicture().getName());

				gameBuilder.add("player1", playerBuilder);

				playerBuilder.add("login", player2.getLogin());
				playerBuilder.add("profilePicture", player2.getProfilePicture().getName());

				gameBuilder.add("player2", playerBuilder);

				DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
				gameBuilder.add("start", start.format(pattern));

				arrayBuilder.add(gameBuilder);

			}

		}

		return arrayBuilder;

	}

}