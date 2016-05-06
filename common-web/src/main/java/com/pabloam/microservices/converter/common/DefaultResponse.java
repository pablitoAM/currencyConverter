package com.pabloam.microservices.converter.common;

import java.io.Serializable;

public class DefaultResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8707618124792281758L;

	/**
	 * The status of the response
	 */
	private ResponseStatus status;

	/**
	 * The content of the response
	 */
	private Object payload;

	public ResponseStatus getStatus() {
		return status;
	}

	public DefaultResponse setStatus(ResponseStatus status) {
		this.status = status;
		return this;
	}

	public Object getPayload() {
		return payload;
	}

	public DefaultResponse setPayload(Object payload) {
		this.payload = payload;
		return this;
	}

}
