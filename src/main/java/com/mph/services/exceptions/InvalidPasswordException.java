package com.mph.services.exceptions;

public class InvalidPasswordException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidPasswordException() {
		super("Mot de passe incorrect.");
	}

}