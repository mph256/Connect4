package com.mph.services.exceptions;

public class EmailAlreadyInUseException extends Exception {

	private static final long serialVersionUID = 1L;

	public EmailAlreadyInUseException() {
		super("Cet email est déjà utilisé. Veuillez en choisir un autre.");
	}

}