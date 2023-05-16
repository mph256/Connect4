package com.mph.servlets;

//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import javax.servlet.http.HttpSession;

import java.io.IOException;

import com.mph.services.interfaces.ConnectionService;
import com.mph.services.ConnectionServiceImpl;

import com.mph.services.exceptions.InvalidLoginException;
import com.mph.services.exceptions.InvalidPasswordException;

//@WebServlet("/connection")
public class ConnectionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ConnectionService connectionService;

	@Override
	public void init() throws ServletException {
		connectionService = new ConnectionServiceImpl();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		String login = (String) session.getAttribute("login");

		if(login == null)
			this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/connection.jsp").forward(request, response);
		else
			response.sendRedirect(request.getContextPath() + "/home");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String login = request.getParameter("login");
		String password = request.getParameter("password");

		try {

			connectionService.connectUser(login, password);

			HttpSession session = request.getSession();
			session.setAttribute("login", login);

			response.sendRedirect(request.getContextPath() + "/home");

		} catch(InvalidLoginException | InvalidPasswordException e) {

			request.setAttribute("error", e.getMessage());

			this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/connection.jsp").forward(request,  response);

		}

	}

}