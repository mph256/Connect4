package com.mph.servlets;

//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import javax.servlet.http.HttpSession;

import java.io.IOException;

import com.mph.services.interfaces.RegistrationService;
import com.mph.services.RegistrationServiceImpl;

import com.mph.services.exceptions.InvalidLoginFormatException;
import com.mph.services.exceptions.LoginAlreadyInUseException;
import com.mph.services.exceptions.InvalidEmailFormatException;
import com.mph.services.exceptions.EmailAlreadyInUseException;
import com.mph.services.exceptions.InvalidPasswordFormatException;
import com.mph.services.exceptions.InvalidPasswordConfirmationException;

//@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	RegistrationService registrationService;

	@Override
	public void init() throws ServletException {
		registrationService = new RegistrationServiceImpl();
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		String login = (String) session.getAttribute("login");

		if(login == null)
			this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/registration.jsp").forward(request, response);
		else
			response.sendRedirect(request.getContextPath() + "/home");

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String login = request.getParameter("login");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String passwordConfirmation = request.getParameter("passwordConfirmation");

		try {

			registrationService.registerUser(login, email, password, passwordConfirmation);

			HttpSession session = request.getSession();
			session.setAttribute("login", login);

			response.sendRedirect(request.getContextPath() + "/home");

		} catch (InvalidLoginFormatException | LoginAlreadyInUseException
				| InvalidEmailFormatException | EmailAlreadyInUseException
				| InvalidPasswordFormatException | InvalidPasswordConfirmationException e) {

			request.setAttribute("error", e.getMessage());

			this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/registration.jsp").forward(request, response);

		}

	}

}