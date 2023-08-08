package com.mph.servlets;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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

import com.mph.services.interfaces.FriendService;

import com.mph.services.FriendServiceImpl;

import com.mph.beans.GameRequest;
import com.mph.beans.RequestState;
import com.mph.beans.User;

public class FriendServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(FriendServlet.class);

	private FriendService friendService;

	@Override
	public void init() throws ServletException {
		friendService = new FriendServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String contentType = request.getContentType();

		if("application/json".equals(contentType)) {

			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");

			if(user != null) {

				Set<User> friends = friendService.getFriendsByUser(user);
				Set<User> onlineFriends = new TreeSet<User>(
					friends
					.stream()
					.filter(User::isOnline)
					.collect(Collectors.toSet())
				);

				user.setFriends(friends);
				user.setOnlineFriends(onlineFriends);

				session.setAttribute("user", user);

				arrayBuilder = refreshFriends(user);

			}

			response.setContentType("application/json; charset=UTF-8");

			response.getWriter().write(arrayBuilder.build().toString());

		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String contentType = request.getContentType();

		if("application/json".equals(contentType)) {

			JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");

			if(user != null) {

				JsonReader reader = Json.createReader(request.getReader());
				JsonObject object = reader.readObject();

				String action = object.getString("action");

				if("remove".equals(action)) {

					String login = object.getString("friend");

					 User friend = user.getFriends()
						.stream()
						.filter(x -> login.equals(x.getLogin()))
						.findFirst()
						.orElse(null);

					 if(friend != null) {

						friendService.removeFriend(user, friend);

						logger.info("Suppression d'ami: " + user.getLogin() + " -> " + login);

						session.setAttribute("user", user);

					 }

					objectBuilder = refreshFriendsAndGameRequests(user);

				}

			}

			response.setContentType("application/json: charset=UTF-8");

			response.getWriter().write(objectBuilder.build().toString());

		}

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

	private JsonObjectBuilder refreshFriendsAndGameRequests(User user) {

		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

		objectBuilder.add("friends", refreshFriends(user));

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

		objectBuilder.add("gameRequests", arrayBuilder);

		return objectBuilder;

	}

}