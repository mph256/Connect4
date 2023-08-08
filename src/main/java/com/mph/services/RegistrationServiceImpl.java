package com.mph.services;

import com.mph.services.interfaces.RegistrationService;
import com.mph.services.interfaces.UserService;
import com.mph.services.interfaces.ScoreService;

import com.mph.services.exceptions.InvalidLoginFormatException;
import com.mph.services.exceptions.LoginAlreadyInUseException;
import com.mph.services.exceptions.InvalidEmailFormatException;
import com.mph.services.exceptions.EmailAlreadyInUseException;
import com.mph.services.exceptions.InvalidPasswordFormatException;
import com.mph.services.exceptions.InvalidPasswordConfirmationException;

import com.mph.beans.User;
import com.mph.beans.Score;

public class RegistrationServiceImpl implements RegistrationService {

	private UserService userService;

	private ScoreService scoreService;

	public RegistrationServiceImpl() {

		userService = new UserServiceImpl();
		scoreService = new ScoreServiceImpl();

	}

	@Override
	public User registerUser(String login, String email, String password, String passwordConfirmation) throws InvalidLoginFormatException, LoginAlreadyInUseException,
		InvalidEmailFormatException, EmailAlreadyInUseException,
		InvalidPasswordFormatException, InvalidPasswordConfirmationException {

		checkLogin(login);
		checkEmail(email);
		checkPassword(password, passwordConfirmation);

		User user = userService.createUser(login, email, password);

		Score score = scoreService.createScore(user);

		user.setScore(score);

		return user;

	}

	private void checkLogin(String login) throws InvalidLoginFormatException, LoginAlreadyInUseException {
		userService.checkNewLogin(login);
	}

	private void checkEmail(String email) throws InvalidEmailFormatException, EmailAlreadyInUseException {
		userService.checkNewEmail(email);
	}

	private void checkPassword(String password, String passwordConfirmation) throws InvalidPasswordFormatException, InvalidPasswordConfirmationException {
		userService.checkNewPassword(password, passwordConfirmation);
	}

}