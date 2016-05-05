/**
 * 
 */
package com.pabloam.microservices.converter.front.exceptions;

/**
 * @author PabloAM
 *
 */
public class BadCredentialsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5352051568114946112L;

	/**
	 * 
	 */
	public BadCredentialsException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public BadCredentialsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public BadCredentialsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BadCredentialsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public BadCredentialsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
