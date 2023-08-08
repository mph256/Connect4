package com.mph.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.servlet.ServletException;

import com.mph.beans.User;

public class HomeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if(user == null)
			response.sendRedirect(request.getContextPath() + "/connection");
		else
			this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response);

	}

}