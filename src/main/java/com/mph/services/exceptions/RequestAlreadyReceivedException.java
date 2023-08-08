package com.mph.services.exceptions;

public class RequestAlreadyReceivedException extends Exception {

	private static final long serialVersionUID = 1L;

	public RequestAlreadyReceivedException(String message) {
		super(message);
	}

}