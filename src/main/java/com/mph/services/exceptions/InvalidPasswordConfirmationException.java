package com.mph.services.exceptions;

public class InvalidPasswordConfirmationException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidPasswordConfirmationException() {
		super("Les mots de passe ne correspondent pas.");
	}

}