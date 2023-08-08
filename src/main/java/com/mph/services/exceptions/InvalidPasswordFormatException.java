package com.mph.services.exceptions;

public class InvalidPasswordFormatException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidPasswordFormatException() {
		super("Votre mot de passe doit comporter entre 6 et 30 caractères.");
	}

}