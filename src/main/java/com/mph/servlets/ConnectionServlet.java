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

import com.mph.services.interfaces.ConnectionService;

import com.mph.services.ConnectionServiceImpl;

import com.mph.services.exceptions.InvalidLoginException;
import com.mph.services.exceptions.InvalidPasswordException;

import com.mph.beans.User;

public class ConnectionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(ConnectionServlet.class);

	private ConnectionService connectionService;

	@Override
	public void init() throws ServletException {
		connectionService = new ConnectionServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if(user == null) {

			Cookie[] cookies = request.getCookies();

			if(cookies != null) {

				for(Cookie cookie: cookies) {

					if("login".equals(cookie.getName())) 
						request.setAttribute("login", cookie.getValue());

				}

			}

			this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/connection.jsp").forward(request, response);

		} else
			response.sendRedirect(request.getContextPath() + "/home");

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String login = request.getParameter("login");
		String password = request.getParameter("password");

		try {

			User user = connectionService.connectUser(login, password);

			logger.info("Connection utilisateur: " + login);

			HttpSession session = request.getSession();
			session.setAttribute("user", user);

			Cookie cookie = new Cookie("login", login);
			cookie.setMaxAge(60 * 60 * 24 * 7);
			response.addCookie(cookie);

			response.sendRedirect(request.getContextPath() + "/home");

		} catch(InvalidLoginException | InvalidPasswordException e) {

			request.setAttribute("error", e.getMessage());

			this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/connection.jsp").forward(request,  response);

		}

	}

}