package com.mph.services.interfaces;

import com.mph.services.exceptions.InvalidLoginFormatException;
import com.mph.services.exceptions.LoginAlreadyInUseException;
import com.mph.services.exceptions.InvalidEmailFormatException;
import com.mph.services.exceptions.EmailAlreadyInUseException;
import com.mph.services.exceptions.InvalidPasswordFormatException;
import com.mph.services.exceptions.InvalidPasswordConfirmationException;

public interface RegistrationService {

	public void registerUser(String login, String email, String password, String passwordConfirmation)
			throws InvalidLoginFormatException, LoginAlreadyInUseException,
			InvalidEmailFormatException, EmailAlreadyInUseException,
			InvalidPasswordFormatException, InvalidPasswordConfirmationException;

}