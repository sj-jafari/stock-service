package com.stocks.exceptions;

import com.stocks.domain.Stock;

/**
 * The exception class indicating a given {@link Stock} does not exist.
 * 
 * @author Jalal
 * @since 20190403
 * @version 1.0
 */
public class StockNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	public static final String message = "The requested stock does not exist!";

	public StockNotFoundException() {
		super(message);
	}
}
