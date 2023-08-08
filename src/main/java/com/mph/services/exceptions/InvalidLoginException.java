package com.mph.services.exceptions;

public class InvalidLoginException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidLoginException() {
		super("Login incorrect.");
	}

}