/**
 * 
 */
package com.pabloam.microservices.converter.provider.exceptions;

/**
 * @author PabloAM
 *
 */
public class BadResponseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6550145221716183763L;

	/**
	 * 
	 */
	public BadResponseException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public BadResponseException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public BadResponseException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public BadResponseException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public BadResponseException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
