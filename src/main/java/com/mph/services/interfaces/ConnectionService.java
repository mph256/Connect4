package com.mph.services.interfaces;

import com.mph.services.exceptions.InvalidLoginException;
import com.mph.services.exceptions.InvalidPasswordException;

import com.mph.beans.User;

public interface ConnectionService {

	/**
	 * Connects an existing user.
	 * 
	 * <br>
	 * Updates his last connection date.
	 * 
	 * @param login the login entered
	 * @param password the password entered
	 * 
	 * @return the connected user
	 * 
	 * @throws InvalidLoginException if no user with this login is found
	 * @throws InvalidPasswordException if the entered password does not match the one registered for this login
	 */
	public User connectUser(String login, String password) throws InvalidLoginException, InvalidPasswordException;

}