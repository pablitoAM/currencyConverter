/**
 * 
 */
package com.pabloam.microservices.converter.provider.exceptions;

/**
 * @author PabloAM
 *
 */
public class ConversionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8092863267405697212L;

	/**
	 * 
	 */
	public ConversionException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ConversionException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ConversionException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ConversionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public ConversionException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
