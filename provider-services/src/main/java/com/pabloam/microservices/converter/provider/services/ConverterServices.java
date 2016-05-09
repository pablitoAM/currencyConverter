package com.pabloam.microservices.converter.provider.services;

import java.util.Map;

import com.pabloam.microservices.converter.provider.exceptions.ConversionException;

public interface ConverterServices {

	/**
	 * Converts the given response into a standard response
	 * 
	 * @param response
	 *            the original response received
	 * @return
	 */
	Map<String, Object> convert(Map<String, Object> response) throws ConversionException;

}
