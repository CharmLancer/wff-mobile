package com.wff.exception;

public class ApplicationServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public ApplicationServiceException(String message, String cause) {
		super(message, new Throwable(message));
	}
}
