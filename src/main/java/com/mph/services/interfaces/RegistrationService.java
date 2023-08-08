package com.mph.services.interfaces;

import com.mph.services.exceptions.InvalidLoginFormatException;
import com.mph.services.exceptions.LoginAlreadyInUseException;
import com.mph.services.exceptions.InvalidEmailFormatException;
import com.mph.services.exceptions.EmailAlreadyInUseException;
import com.mph.services.exceptions.InvalidPasswordFormatException;
import com.mph.services.exceptions.InvalidPasswordConfirmationException;

import com.mph.beans.User;

public interface RegistrationService {

	/**
	 * Registers a new user.
	 * 
	 * @param login the login entered
	 * @param email the email entered
	 * @param password the password entered
	 * @param passwordConfirmation the password confirmation entered
	 * 
	 * @return the new user created
	 * 
	 * @throws InvalidLoginFormatException if the format of the entered login is not correct
	 * @throws LoginAlreadyInUseException if the entered login is already in use
	 * @throws InvalidEmailFormatException if the format of the entered email is not correct
	 * @throws EmailAlreadyInUseException if the entered email is already in use
	 * @throws InvalidPasswordFormatException if the format of the entered password is not correct
	 * @throws InvalidPasswordConfirmationException if the entered passwords do not match
	 */
	public User registerUser(String login, String email, String password, String passwordConfirmation) throws InvalidLoginFormatException, LoginAlreadyInUseException,
		InvalidEmailFormatException, EmailAlreadyInUseException,
		InvalidPasswordFormatException, InvalidPasswordConfirmationException;

}