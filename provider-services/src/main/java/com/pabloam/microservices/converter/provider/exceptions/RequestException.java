/**
 * 
 */
package com.pabloam.microservices.converter.provider.exceptions;

/**
 * @author Pablo
 *
 */
public class RequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4980562106354866027L;

	/**
	 * 
	 */
	public RequestException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public RequestException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public RequestException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public RequestException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public RequestException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
