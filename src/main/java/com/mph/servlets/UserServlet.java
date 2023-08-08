package com.mph.servlets;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.servlet.ServletException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import com.mph.services.interfaces.UserService;

import com.mph.services.UserServiceImpl;

import com.mph.beans.User;
import com.mph.beans.FriendRequest;

public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private UserService userService;

	@Override
	public void init() throws ServletException {
		userService = new UserServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		String contentType = request.getContentType();

		if("application/json".equals(contentType)) {

			if(user != null) {

				String url = request.getRequestURL().toString();
				String page = url.substring(url.lastIndexOf("/") + 1);

				if("users".equals(page)) {

					JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

					arrayBuilder = refreshUsers(userService.getUsers(), user);

					response.setContentType("application/json; charset=UTF-8");

					response.getWriter().write(arrayBuilder.build().toString());

				} else {

					JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

					objectBuilder = refreshUser(user);

					response.setContentType("application/json; charset=UTF-8");

					response.getWriter().write(objectBuilder.build().toString());

				}

			}

		} else {

			if(user == null)
				response.sendRedirect(request.getContextPath() + "/connection");
			else {

				Set<User> friends = user.getFriends();

				Set<User> users = new TreeSet<User>(userService.getUsers()
					.stream()
					.filter(x -> !user.equals(x) && !friends.contains(x))
					.collect(Collectors.toSet())
				);

				request.setAttribute("users", users);

				this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/users.jsp").forward(request, response);

			}

		}

	}

	private JsonArrayBuilder refreshUsers(Set<User> users, User user) {

		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		JsonObjectBuilder userBuilder = Json.createObjectBuilder();

		Set<User> friends = user.getFriends();

		Set<FriendRequest> friendRequests = user.getFriendRequestsSent();

		for(User user2: users) {

			if(!user.equals(user2) && !friends.contains(user2) 
				&& (user.getFriendRequestsReceived().stream().filter(x -> user2.equals(x.getSender())).collect(Collectors.toList()).size() == 0)) {

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

	private JsonObjectBuilder refreshUser(User user) {

		JsonObjectBuilder userBuilder = Json.createObjectBuilder();

		userBuilder.add("login", user.getLogin());
		userBuilder.add("isInGame", user.isInGame());

		return userBuilder;

	}

}