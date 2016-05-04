/**
 * 
 */
package com.pabloam.microservices.converter.user.exception;

/**
 * @author PabloAM
 *
 */
public class UserServicesException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3869878310653677332L;

	/**
	 * 
	 */
	public UserServicesException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UserServicesException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UserServicesException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UserServicesException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public UserServicesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
