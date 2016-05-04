package com.pabloam.microservices.converter.user.exception;

/**
 * @author Pablo
 *
 */
public class RegistrationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2923627302630262733L;

	/**
	 * 
	 */
	public RegistrationException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public RegistrationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public RegistrationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RegistrationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public RegistrationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
