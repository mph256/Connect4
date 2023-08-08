package com.mph.servlets;

import java.util.Set;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.servlet.ServletException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import com.mph.services.interfaces.ScoreService;

import com.mph.services.ScoreServiceImpl;

import com.mph.beans.Score;
import com.mph.beans.User;

public class ScoreServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ScoreService scoreService;

	@Override
	public void init() throws ServletException {
		scoreService = new ScoreServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String contentType = request.getContentType();

		if("application/json".equals(contentType)) {

			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

			arrayBuilder = refreshScores(scoreService.getScores());

			response.setContentType("application/json; charset=UTF-8");

			response.getWriter().write(arrayBuilder.build().toString());

		} else {

			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");

			if(user == null)
				response.sendRedirect(request.getContextPath() + "/connection");
			else {

				request.setAttribute("scores", scoreService.getScores());

				this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/scores.jsp").forward(request, response);

			}

		}

	}

	private JsonArrayBuilder refreshScores(Set<Score> scores) {

		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		JsonObjectBuilder scoreBuilder = Json.createObjectBuilder();

		int i = 1;

		for(Score score: scores) {

			scoreBuilder.add("rank", i);

			JsonObjectBuilder userBuilder = Json.createObjectBuilder();

			User user = score.getUser();

			userBuilder.add("login", user.getLogin());
			userBuilder.add("profilePicture", user.getProfilePicture().getName());

			scoreBuilder.add("user", userBuilder);

			scoreBuilder.add("ratio", ((score.getDefeats() == 0)?score.getWins():(((double)score.getWins())/((double)score.getDefeats()))));
			scoreBuilder.add("wins", score.getWins());
			scoreBuilder.add("defeats", score.getDefeats());
			scoreBuilder.add("draws", score.getDraws());
			scoreBuilder.add("bestWinStreak", score.getBestWinStreak());

			arrayBuilder.add(scoreBuilder);

			i++;

		}

		return arrayBuilder;

	}

}