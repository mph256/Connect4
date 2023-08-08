package com.mph.services.exceptions;

public class InvalidEmailFormatException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidEmailFormatException() {
		super("Adresse email invalide.");
	}

}