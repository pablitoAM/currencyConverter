/**
 * 
 */
package com.pabloam.microservices.converter.history.exceptions;

/**
 * @author PabloAM
 *
 */
public class HistoryServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4727313312339940313L;

	/**
	 * 
	 */
	public HistoryServiceException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public HistoryServiceException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public HistoryServiceException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public HistoryServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public HistoryServiceException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
