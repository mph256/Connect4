package com.mph.services.exceptions;

public class RequestAlreadySentException extends Exception {

	private static final long serialVersionUID = 1L;

	public RequestAlreadySentException(String message) {
		super(message);
	}

}