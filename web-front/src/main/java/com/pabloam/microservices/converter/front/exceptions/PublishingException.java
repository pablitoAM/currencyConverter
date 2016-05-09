/**
 * 
 */
package com.pabloam.microservices.converter.front.exceptions;

/**
 * @author PabloAM
 *
 */
public class PublishingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3411195767901096596L;

	/**
	 * 
	 */
	public PublishingException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public PublishingException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public PublishingException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PublishingException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public PublishingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
