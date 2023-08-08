package com.mph.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

import javax.servlet.ServletException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mph.services.interfaces.RegistrationService;

import com.mph.services.RegistrationServiceImpl;

import com.mph.services.exceptions.InvalidLoginFormatException;
import com.mph.services.exceptions.LoginAlreadyInUseException;
import com.mph.services.exceptions.InvalidEmailFormatException;
import com.mph.services.exceptions.EmailAlreadyInUseException;
import com.mph.services.exceptions.InvalidPasswordFormatException;
import com.mph.services.exceptions.InvalidPasswordConfirmationException;

import com.mph.beans.User;

public class RegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(RegistrationServlet.class);

	private RegistrationService registrationService;

	@Override
	public void init() throws ServletException {
		registrationService = new RegistrationServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if(user == null)
			this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/registration.jsp").forward(request, response);
		else
			response.sendRedirect(request.getContextPath() + "/home");

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String login = request.getParameter("login");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String passwordConfirmation = request.getParameter("password-confirmation");

		try {

			User user = registrationService.registerUser(login, email, password, passwordConfirmation);

			logger.info("Inscription utilisateur: " + login);

			HttpSession session = request.getSession();
			session.setAttribute("user", user);
	
			Cookie cookie = new Cookie("login", login);
			cookie.setMaxAge(60 * 60 * 24 * 7);
			response.addCookie(cookie);

			response.sendRedirect(request.getContextPath() + "/home");

		} catch (InvalidLoginFormatException | LoginAlreadyInUseException
			| InvalidEmailFormatException | EmailAlreadyInUseException
			| InvalidPasswordFormatException | InvalidPasswordConfirmationException e) {

			request.setAttribute("error", e.getMessage());

			this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/registration.jsp").forward(request, response);

		}

	}

}