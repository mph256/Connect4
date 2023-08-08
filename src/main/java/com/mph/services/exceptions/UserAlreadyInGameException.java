package com.mph.services.exceptions;

public class UserAlreadyInGameException extends Exception {

	private static final long serialVersionUID = 1L;

	public UserAlreadyInGameException() {
		super("Ce joueur est déjà en jeu.");
	}

}