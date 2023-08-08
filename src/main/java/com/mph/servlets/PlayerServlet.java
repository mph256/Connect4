package com.mph.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.servlet.ServletException;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import com.mph.services.interfaces.UserService;

import com.mph.services.UserServiceImpl;

import com.mph.beans.User;
import com.mph.beans.FriendRequest;

public class PlayerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private UserService userService;

	@Override
	public void init() throws ServletException {
		userService = new UserServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String contentType = request.getContentType();

		if("application/json".equals(contentType)) {

			JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");

			if(user != null) {

				User player = userService.getUserByLogin(request.getParameter("login"));

				objectBuilder = refreshPlayer(player, user);

			}

			response.setContentType("application/json; charset=UTF-8");

			response.getWriter().write(objectBuilder.build().toString());

		}

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

}