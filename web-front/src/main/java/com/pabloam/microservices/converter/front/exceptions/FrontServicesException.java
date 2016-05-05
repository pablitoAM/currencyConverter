/**
 * 
 */
package com.pabloam.microservices.converter.front.exceptions;

/**
 * @author PabloAM
 *
 */
public class FrontServicesException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3771029348859108523L;

	/**
	 * 
	 */
	public FrontServicesException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public FrontServicesException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public FrontServicesException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FrontServicesException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public FrontServicesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
