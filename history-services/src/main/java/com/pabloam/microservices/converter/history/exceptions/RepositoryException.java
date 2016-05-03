/**
 * Copyright (c) 2016 Molenaar Strategie BV.
 * Created: 3 May 2016 17:43:52 Author: Pablo
 */

package com.pabloam.microservices.converter.history.exceptions;

/**
 * @author Pablo
 *
 */
public class RepositoryException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3114975769890218901L;

	/**
	 * 
	 */
	public RepositoryException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public RepositoryException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public RepositoryException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public RepositoryException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public RepositoryException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
