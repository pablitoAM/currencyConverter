package com.pabloam.microservices.converter.provider.services;

import java.util.Map;

import com.pabloam.microservices.converter.provider.exceptions.ConversionException;

public interface ConverterServices {

	/**
	 * Converts the given response into a map
	 * 
	 * @param jsonResponse
	 *            the received response in json
	 * @return
	 */
	Map<String, Object> convert(String jsonResponse) throws ConversionException;

}
