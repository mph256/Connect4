package com.mph.services;

import java.util.regex.Pattern;

import com.mph.services.interfaces.RegistrationService;

import com.mph.dao.DaoFactory;
import com.mph.dao.interfaces.RegistrationDao;

import com.mph.beans.User;

import com.mph.services.exceptions.InvalidLoginFormatException;
import com.mph.services.exceptions.LoginAlreadyInUseException;
import com.mph.services.exceptions.InvalidEmailFormatException;
import com.mph.services.exceptions.EmailAlreadyInUseException;
import com.mph.services.exceptions.InvalidPasswordConfirmationException;
import com.mph.services.exceptions.InvalidPasswordFormatException;

public class RegistrationServiceImpl implements RegistrationService {
	
	private static final String EMAIL_REGEX = "^(?=.{1,64}@)[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)*@[^-][a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{2,})$";

	private RegistrationDao registrationDao;

	public RegistrationServiceImpl() {
		registrationDao = DaoFactory.getInstance().getRegistrationDao();
	}

	@Override
	public void registerUser(String login, String email, String password, String passwordConfirmation)
			throws InvalidLoginFormatException, LoginAlreadyInUseException,
			InvalidEmailFormatException, EmailAlreadyInUseException,
			InvalidPasswordFormatException, InvalidPasswordConfirmationException {

		checkLogin(login);
		checkEmail(email);
		checkPassword(password, passwordConfirmation);

		User user = new User(login, password, email);
		registrationDao.registerUser(user);

	}

	private void checkLogin(String login) throws InvalidLoginFormatException, LoginAlreadyInUseException {
		if(login.length() < 6 || login.length() > 30)
			throw new InvalidLoginFormatException("Votre login doit comporter entre 6 et 30 caractères.");
		if(isLoginAlreadyInUse(login))
			throw new LoginAlreadyInUseException("Ce login est déjà utilisé. Veuillez en choisir un autre.");
	}

	private void checkEmail(String email) throws InvalidEmailFormatException, EmailAlreadyInUseException {
		if(!isEmail(email))
			throw new InvalidEmailFormatException("Adresse email invalide.");
		if(isEmailAlreadyInUse(email))
			throw new EmailAlreadyInUseException("Cet email est déjà utilisé. Veuillez en choisir un autre.");
	}

	private void checkPassword(String password, String passwordConfirmation) throws InvalidPasswordFormatException, InvalidPasswordConfirmationException {
		if(password.length() < 6 || password.length() > 30)
			throw new InvalidPasswordFormatException("Votre mot de passe doit comporter entre 6 et 30 caractères.");
		if(!password.equals(passwordConfirmation))
			throw new InvalidPasswordConfirmationException("Les mots de passe ne correspondent pas.");
	}

	private boolean isLoginAlreadyInUse(String login) {
		return registrationDao.isLoginAlreadyInUse(login);
	}

	private boolean isEmail(String email) {
		return Pattern.matches(EMAIL_REGEX, email);
	}

	private boolean isEmailAlreadyInUse(String email) {
		return registrationDao.isEmailAlreadyInUse(email);
	}

}