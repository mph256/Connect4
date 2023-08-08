package com.mph.servlets;

import java.io.File;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;
import javax.servlet.http.Part;

import javax.servlet.ServletException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mph.services.interfaces.UserService;
import com.mph.services.interfaces.FriendRequestService;
import com.mph.services.interfaces.GameRequestService;

import com.mph.services.UserServiceImpl;
import com.mph.services.FriendRequestServiceImpl;
import com.mph.services.GameRequestServiceImpl;

import com.mph.services.exceptions.InvalidEmailFormatException;
import com.mph.services.exceptions.EmailAlreadyInUseException;
import com.mph.services.exceptions.InvalidPasswordFormatException;
import com.mph.services.exceptions.InvalidPasswordConfirmationException;

import com.mph.beans.User;

import com.mph.util.Util;

public class ProfileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(ProfileServlet.class);

	private UserService userService;

	private FriendRequestService friendRequestService;

	private GameRequestService gameRequestService;

	@Override
	public void init() throws ServletException {

		userService = new UserServiceImpl();
		friendRequestService = new FriendRequestServiceImpl();
		gameRequestService = new GameRequestServiceImpl();

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if(user == null)
			response.sendRedirect(request.getContextPath() + "/connection");
		else
			this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);

	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if(user != null) {

			String action = request.getParameter("action");

			if("delete".equals(action)) {

				session.invalidate();

				userService.deleteUser(user);

				logger.info("Suppression utilisateur: " + user.getLogin());

				friendRequestService.cancelFriendRequests(user.getFriendRequestsSent());
				friendRequestService.declineFriendRequests(user.getFriendRequestsReceived());

				gameRequestService.cancelGameRequests(user.getGameRequestsSent());
				gameRequestService.declineGameRequests(user.getGameRequestsReceived());

				Cookie[] cookies = request.getCookies();

				if(cookies != null) {

					for(Cookie cookie: cookies) {

						if("login".equals(cookie.getName())) 
							cookie.setMaxAge(0);

					}

				}

				response.sendRedirect(request.getContextPath() + "/connection");

			} 

			if("update".equals(action)){

				String email = request.getParameter("email");
				String password = request.getParameter("password");
				String passwordConfirmation = request.getParameter("password-confirmation");

				Part part = request.getPart("profile-picture");

				try {

					if(part.getSize() > 0) {

						user.getProfilePicture().delete();

						File profilePicture = Util.updateProfilePicture(part, user.getLogin());

						user.setProfilePicture(profilePicture);

					}

					userService.updateUser(user, email, password, passwordConfirmation);

					logger.info("Mise à jour utilisateur: " + user.getLogin());

					session.setAttribute("user", user);

				} catch(InvalidEmailFormatException | EmailAlreadyInUseException
					| InvalidPasswordFormatException | InvalidPasswordConfirmationException e) {

					request.setAttribute("error", e.getMessage());

				}

				this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);

			}

		}

	}

}