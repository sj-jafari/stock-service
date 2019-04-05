package com.stocks.exceptions;

/**
 * This exception occurs when login info is not correct.
 * 
 * @author Jalal
 * @since 20190403
 */
public class InvalidCredentialsException extends Exception {
	private static final long serialVersionUID = 1L;
	private static final String message = "Invalid username or password!";

	public InvalidCredentialsException() {
		super(message);
	}
}
