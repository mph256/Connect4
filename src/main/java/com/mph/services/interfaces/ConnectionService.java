package com.mph.services.interfaces;

import com.mph.services.exceptions.InvalidLoginException;
import com.mph.services.exceptions.InvalidPasswordException;

public interface ConnectionService {

	public void connectUser(String login, String password) throws InvalidLoginException, InvalidPasswordException;

}