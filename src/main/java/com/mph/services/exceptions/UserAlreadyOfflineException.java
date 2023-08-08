package com.mph.services.exceptions;

public class UserAlreadyOfflineException extends Exception {

	private static final long serialVersionUID = 1L;

	public UserAlreadyOfflineException() {
		super("Ce joueur est déjà en hors ligne.");
	}

}