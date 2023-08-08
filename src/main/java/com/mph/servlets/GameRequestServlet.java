package com.mph.servlets;

import java.util.Set;

import java.io.IOException;

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

import com.mph.services.interfaces.GameRequestService;

import com.mph.services.GameRequestServiceImpl;

import com.mph.services.exceptions.UserAlreadyOfflineException;
import com.mph.services.exceptions.UserAlreadyInGameException;
import com.mph.services.exceptions.RequestAlreadySentException;
import com.mph.services.exceptions.RequestAlreadyReceivedException;

import com.mph.beans.GameRequest;
import com.mph.beans.RequestState;
import com.mph.beans.Game;
import com.mph.beans.User;

public class GameRequestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(GameRequestServlet.class);

	private GameRequestService gameRequestService;

	@Override
	public void init() throws ServletException {
		gameRequestService = new GameRequestServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String contentType = request.getContentType();

		if("application/json".equals(contentType)) {

			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");

			if(user != null) {

				String state = request.getParameter("state");

				if("pending".equals(state)) {

					JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

					user.setGameRequestsReceived(gameRequestService.getGameRequestsReceivedByUser(user));

					session.setAttribute("user", user);

					arrayBuilder = refreshGameRequests(user);

					response.setContentType("application/json; charset=UTF-8");

					response.getWriter().write(arrayBuilder.build().toString());

				}

				if("accepted".equals(state)) {

					JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

					GameRequest gameRequest = user.getGameRequestsSent()
						.stream()
						.filter(x -> RequestState.ACCEPTED.equals(x.getState()))
						.findFirst()
						.orElse(null);

					if(gameRequest != null) {

						Game game = gameRequest.getGame();

						long gameId = game.getId();
						String receiver = gameRequest.getReceiver().getLogin();

						logger.info("Partie " + gameId + " démarrée | Joueur 1: " + user.getLogin() + " | Joueur 2: " + receiver);

						user.removeGameRequestSent(gameRequest);

						gameRequestService.cancelGameRequests(user.getGameRequestsSent());
						gameRequestService.declineGameRequests(user.getGameRequestsReceived());

						session.setAttribute("user", user);

						objectBuilder.add("gameId", gameId);
						objectBuilder.add("receiver", receiver);

					}

					response.setContentType("application/json; charset=UTF-8");

					response.getWriter().write(objectBuilder.build().toString());

				}

			}

		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if(user != null) {

			String contentType = request.getContentType();

			if("application/json".equals(contentType)) {

				JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

				JsonReader reader = Json.createReader(request.getReader());
				JsonObject object = reader.readObject();

				String action = object.getString("action");

				if("send".equals(action) || "cancel".equals(action)) {

					String login = object.getString("receiver");

					if("send".equals(action)) {

						try {

							gameRequestService.sendGameRequest(user, login);

							logger.info("Demande de jeu envoyée: " + user.getLogin() + " -> " + login);

							session.setAttribute("user", user);

						} catch(UserAlreadyOfflineException | UserAlreadyInGameException | RequestAlreadySentException | RequestAlreadyReceivedException e) {
						}

					}

					if("cancel".equals(action)) {

						GameRequest gameRequest = user.getGameRequestsSent()
							.stream()
							.filter(x -> login.equals(x.getReceiver().getLogin()))
							.findFirst()
							.orElse(null);

						if(gameRequest != null) {

							gameRequestService.cancelGameRequest(gameRequest);

							logger.info("Demande de jeu annulée: " + user.getLogin() + " -> " + login);

							session.setAttribute("user", user);

						}

					}

					arrayBuilder = refreshOnlineFriends(user);

				}

				if("decline".equals(action)) {

					String login = object.getString("sender");

					GameRequest gameRequest = user.getGameRequestsReceived()
						.stream()
						.filter(x -> login.equals(x.getSender().getLogin()))
						.findFirst()
						.orElse(null);

					if(gameRequest != null) {

						gameRequestService.declineGameRequest(gameRequest);

						logger.info("Demande de jeu déclinée: " + login + " -> " + user.getLogin());

						session.setAttribute("user", user);

					}

					arrayBuilder = refreshGameRequests(user);

				}

				response.setContentType("application/json; charset=UTF-8");

				response.getWriter().write(arrayBuilder.build().toString());

			} else {

				String login = request.getParameter("request-game-sender");

				GameRequest gameRequest = user.getGameRequestsReceived()
					.stream()
					.filter(x -> login.equals(x.getSender().getLogin()))
					.findFirst()
					.orElse(null);

				if(gameRequest != null) {

					Game game = gameRequestService.acceptGameRequest(gameRequest);

					logger.info("Demande de jeu acceptée: " + login + " -> " + user.getLogin());

					session.setAttribute("user", user);

					response.sendRedirect(request.getContextPath() + "/game?id=" + game.getId());

				} else
					this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response);

			}

		}

	}

	private JsonArrayBuilder refreshGameRequests(User user) {

		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		JsonObjectBuilder gameRequestBuilder = Json.createObjectBuilder();

		for(GameRequest gameRequest: user.getGameRequestsReceived()) {

			if(RequestState.PENDING.equals(gameRequest.getState())) {

				User sender = gameRequest.getSender();

				JsonObjectBuilder userBuilder = Json.createObjectBuilder();

				userBuilder.add("login", sender.getLogin());
				userBuilder.add("profilePicture", sender.getProfilePicture().getName());

				gameRequestBuilder.add("sender", userBuilder);

				arrayBuilder.add(gameRequestBuilder);

			}

		}

		return arrayBuilder;

	}

	private JsonArrayBuilder refreshOnlineFriends(User user ) {

		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		JsonObjectBuilder friendBuilder = Json.createObjectBuilder();

		Set<GameRequest> gameRequests = user.getGameRequestsSent();

		for(User friend: user.getOnlineFriends()) {

			friendBuilder.add("login", friend.getLogin());
			friendBuilder.add("profilePicture", friend.getProfilePicture().getName());
			friendBuilder.add("isOnline", true);
			friendBuilder.add("isInGame", friend.isInGame());

			boolean isRequested = false;

			for(GameRequest gameRequest: gameRequests) {

				if((RequestState.PENDING.equals(gameRequest.getState())) && (friend.equals(gameRequest.getReceiver()))) {

					isRequested = true;
					break;

				}

			}

			friendBuilder.add("isRequested", isRequested);

			arrayBuilder.add(friendBuilder);

		}

		return arrayBuilder;

	}

}