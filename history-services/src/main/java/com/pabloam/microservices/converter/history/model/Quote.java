package com.pabloam.microservices.converter.history.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class Quote implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6542722225749286129L;

	@Id
	@JsonSerialize(using = ToStringSerializer.class)
	private String id;

	private String currency;

	private Double value;

	public String getId() {
		return id;
	}

	public Quote() {

	}

	public Quote(String currency, Double value) {
		this.currency = currency;
		this.value = value;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

}
