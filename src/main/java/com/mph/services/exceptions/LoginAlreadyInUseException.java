package com.mph.services.exceptions;

public class LoginAlreadyInUseException extends Exception {

	private static final long serialVersionUID = 1L;

	public LoginAlreadyInUseException() {
		super("Ce login est déjà utilisé. Veuillez en choisir un autre.");
	}

}