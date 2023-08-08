package com.mph.servlets;

import java.util.Set;

import java.io.IOException;

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

import com.mph.services.interfaces.FriendRequestService;
import com.mph.services.interfaces.GameService;
import com.mph.services.interfaces.UserService;

import com.mph.services.FriendRequestServiceImpl;
import com.mph.services.GameServiceImpl;
import com.mph.services.UserServiceImpl;

import com.mph.services.exceptions.RequestAlreadySentException;
import com.mph.services.exceptions.RequestAlreadyReceivedException;

import com.mph.beans.FriendRequest;
import com.mph.beans.GameRequest;
import com.mph.beans.RequestState;
import com.mph.beans.Game;
import com.mph.beans.GameState;
import com.mph.beans.User;

public class FriendRequestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(FriendRequestServlet.class);

	private FriendRequestService friendRequestService;

	private GameService gameService;

	private UserService userService;

	@Override
	public void init() throws ServletException {

		friendRequestService = new FriendRequestServiceImpl();
		gameService = new GameServiceImpl();
		userService = new UserServiceImpl();

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

					user.setFriendRequestsReceived(friendRequestService.getFriendRequestsReceivedByUser(user));

					session.setAttribute("user", user);

					String page = request.getParameter("page");

					if("game".equals(page)) {

						Game game = gameService.getGameByUser(user);

						User player1 = game.getPlayer1();
						User player2 = game.getPlayer2();

						boolean isPlayer1 = user.equals(player1);

						if(GameState.OVER.equals(game.getState()) && (isPlayer1 || user.equals(player2))) {

							JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

							objectBuilder = refreshFriendRequestsAndPlayer(user, (isPlayer1?player2:player1));

							response.setContentType("application/json; charset=UTF-8");

							response.getWriter().write(objectBuilder.build().toString());

						} else {

							JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

							arrayBuilder = refreshFriendRequests(user);

							response.setContentType("application/json; charset=UTF-8");

							response.getWriter().write(arrayBuilder.build().toString());

						}

					} else {

						JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

						arrayBuilder = refreshFriendRequests(user);

						response.setContentType("application/json; charset=UTF-8");

						response.getWriter().write(arrayBuilder.build().toString());

					}

				}

			}

		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String contentType = request.getContentType();

		if("application/json".equals(contentType)) {

			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");

			if(user != null) {

				JsonReader reader = Json.createReader(request.getReader());
				JsonObject object = reader.readObject();

				String action = object.getString("action");
				String page = object.getString("page");

				if("send".equals(action) || "cancel".equals(action)) {

					String login = object.getString("receiver");

					if("send".equals(action)) {

						try {

							friendRequestService.sendFriendRequest(user, login);

							logger.info("Demande d'ami envoyée: " + user.getLogin() + " -> " + login);

							session.setAttribute("user", user);

						} catch(RequestAlreadySentException | RequestAlreadyReceivedException e) {
						}

					}

					if("cancel".equals(action)) {

						FriendRequest friendRequest = user.getFriendRequestsSent()
							.stream()
							.filter(x -> login.equals(x.getReceiver().getLogin()))
							.findFirst()
							.orElse(null);

						if(friendRequest != null) {

							friendRequestService.cancelFriendRequest(friendRequest);

							logger.info("Demande d'ami annulée: " + user.getLogin() + " -> " + login);

							session.setAttribute("user", user);

						}

					}

					if("game".equals(page)) {

						Game game = gameService.getGameByUser(user);

						User player1 = game.getPlayer1();
						User player2 = game.getPlayer2();

						boolean isPlayer1 = user.equals(player1);

						if(GameState.OVER.equals(game.getState()) && (isPlayer1 || user.equals(player2))) {

							JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

							objectBuilder = refreshPlayer((isPlayer1?player2:player1), user);

							response.setContentType("application/json; charset=UTF-8");

							response.getWriter().write(objectBuilder.build().toString());

						}

					}

					if("users".equals(page)) {

						JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

						arrayBuilder = refreshUsers(userService.getUsers(), user);

						response.setContentType("application/json; charset=UTF-8");

						response.getWriter().write(arrayBuilder.build().toString());

					}

				}

				if("accept".equals(action) || "decline".equals(action)) {

					String login = object.getString("sender");

					FriendRequest friendRequest = user.getFriendRequestsReceived()
						.stream()
						.filter(x -> login.equals(x.getSender().getLogin()))
						.findFirst()
						.orElse(null);

					if(friendRequest != null) {

						if("accept".equals(action)) {

							friendRequestService.acceptFriendRequest(friendRequest);

							logger.info("Demande d'ami acceptée: " + login + " -> " + user.getLogin());

							session.setAttribute("user", user);

						}

						if("decline".equals(action)) {

							friendRequestService.declineFriendRequest(friendRequest);

							logger.info("Demande d'ami déclinée: " + login + " -> " + user.getLogin());

							session.setAttribute("user", user);

						}

					}

					if("game".equals(page)) {

						Game game = gameService.getGameByUser(user);

						User player1 = game.getPlayer1();
						User player2 = game.getPlayer2();

						boolean isPlayer1 = user.equals(player1);

						if("accept".equals(action)) {

							JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

							if(GameState.OVER.equals(game.getState()) && (isPlayer1 || user.equals(player2)))
								objectBuilder = refreshFriendRequestsAndFriendsAndPlayer(user, (isPlayer1?player2:player1));
							else
								objectBuilder = refreshFriendRequestsAndFriends(user);	

							response.setContentType("application/json; charset=UTF-8");

							response.getWriter().write(objectBuilder.build().toString());

						}

						if("decline".equals(action)) {

							if(GameState.OVER.equals(game.getState()) && (isPlayer1 || user.equals(player2))) {	

								JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

								objectBuilder = refreshFriendRequestsAndPlayer(user, (isPlayer1?player2:player1));

								response.setContentType("application/json; charset=UTF-8");

								response.getWriter().write(objectBuilder.build().toString());

							} else {

								JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

								arrayBuilder = refreshFriendRequests(user);

								response.setContentType("application/json; charset=UTF-8");

								response.getWriter().write(arrayBuilder.build().toString());

							}

						}

					} else {

						if("accept".equals(action)) {

							JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

							objectBuilder = refreshFriendRequestsAndFriends(user);

							response.setContentType("application/json; charset=UTF-8");

							response.getWriter().write(objectBuilder.build().toString());

						}

						if("decline".equals(action)) {

							JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

							arrayBuilder = refreshFriendRequests(user);

							response.setContentType("application/json; charset=UTF-8");

							response.getWriter().write(arrayBuilder.build().toString());

						}

					}

				}

			}

		}

	}

	private JsonArrayBuilder refreshFriendRequests(User user) {

		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		JsonObjectBuilder friendRequestBuilder = Json.createObjectBuilder();

		for(FriendRequest friendRequest: user.getFriendRequestsReceived()) {

			if(RequestState.PENDING.equals(friendRequest.getState())) {

				User sender = friendRequest.getSender();

				JsonObjectBuilder userBuilder = Json.createObjectBuilder();

				userBuilder.add("login", sender.getLogin());
				userBuilder.add("profilePicture", sender.getProfilePicture().getName());

				friendRequestBuilder.add("sender", userBuilder);

				arrayBuilder.add(friendRequestBuilder);

			}

		}

		return arrayBuilder;

	}

	private JsonObjectBuilder refreshFriendRequestsAndPlayer(User user, User player) {

		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

		objectBuilder.add("friendRequests", refreshFriendRequests(user));
		objectBuilder.add("player", refreshPlayer(player, user));

		return objectBuilder;

	}

	private JsonObjectBuilder refreshFriendRequestsAndFriends(User user) {

		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

		objectBuilder.add("friendRequests", refreshFriendRequests(user));
		objectBuilder.add("friends", refreshFriends(user));

		return objectBuilder;

	}

	private JsonObjectBuilder refreshFriendRequestsAndFriendsAndPlayer(User user, User player) {

		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

		objectBuilder.add("friendRequests", refreshFriendRequests(user));
		objectBuilder.add("friends", refreshFriends(user));
		objectBuilder.add("player", refreshPlayer(player, user));

		return objectBuilder;

	}

	private JsonObjectBuilder refreshPlayer(User player, User user) {

		JsonObjectBuilder playerBuilder = Json.createObjectBuilder();

		playerBuilder.add("login", player.getLogin());

		boolean isFriend = (user.getFriends()).contains(player);
		boolean isRequested = false;

		playerBuilder.add("isFriend", isFriend);

		if(!isFriend) {

			for(FriendRequest friendRequest: user.getFriendRequestsSent()) {

				if(player.equals(friendRequest.getReceiver())) {

					isRequested = true;
					break;

				}

			}

		}

		playerBuilder.add("isRequested", isRequested);

		return playerBuilder;

	}

	private JsonArrayBuilder refreshFriends(User user) {

		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		JsonObjectBuilder friendBuilder = Json.createObjectBuilder();

		Set<GameRequest> gameRequests = user.getGameRequestsSent();

		for(User friend: user.getFriends()) {

			boolean isOnline = friend.isOnline();

			friendBuilder.add("login", friend.getLogin());
			friendBuilder.add("profilePicture", friend.getProfilePicture().getName());

			DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			friendBuilder.add("lastConnection", friend.getLastConnection().format(pattern));

			friendBuilder.add("isOnline", isOnline);
			friendBuilder.add("isInGame", friend.isInGame());

			boolean isRequested = false;

			if(isOnline) {

				for(GameRequest gameRequest: gameRequests) {

					if(friend.equals(gameRequest.getReceiver())) {

						isRequested = true;
						break;

					}

				}

			}

			friendBuilder.add("isRequested", isRequested);

			arrayBuilder.add(friendBuilder);

		}

		return arrayBuilder;

	}

	private JsonArrayBuilder refreshUsers(Set<User> users, User user) {

		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		JsonObjectBuilder userBuilder = Json.createObjectBuilder();

		Set<User> friends = user.getFriends();

		Set<FriendRequest> friendRequests = user.getFriendRequestsSent();

		for(User user2: users) {

			if(!user.equals(user2) && !friends.contains(user2)) {

				userBuilder.add("login", user2.getLogin());
				userBuilder.add("profilePicture", user2.getProfilePicture().getName());

				boolean isRequested = false;

				for(FriendRequest friendRequest: friendRequests) {

					if(user2.equals(friendRequest.getReceiver())) {

						isRequested = true;
						break;

					}

				}

				userBuilder.add("isRequested", isRequested);

				arrayBuilder.add(userBuilder);

			}

		}

		return arrayBuilder;

	}

}