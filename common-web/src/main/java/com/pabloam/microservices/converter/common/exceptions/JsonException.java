/**
 * 
 */
package com.pabloam.microservices.converter.common.exceptions;

/**
 * @author Pablo
 *
 */
public class JsonException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8824573341253064600L;

	/**
	 * 
	 */
	public JsonException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public JsonException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public JsonException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public JsonException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public JsonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
