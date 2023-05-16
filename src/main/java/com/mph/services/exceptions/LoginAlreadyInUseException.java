package com.mph.services.exceptions;

public class LoginAlreadyInUseException extends Exception {

	public LoginAlreadyInUseException(String message) {
		super(message);
	}

}