package com.mph.services.exceptions;

public class InvalidLoginFormatException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidLoginFormatException() {
		super("Votre login doit comporter entre 6 et 30 caractères.");
	}

}