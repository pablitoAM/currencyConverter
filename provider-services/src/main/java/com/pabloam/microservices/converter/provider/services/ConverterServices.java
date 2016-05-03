package com.pabloam.microservices.converter.provider.services;

import com.pabloam.microservices.converter.common.ConvertedResponse;
import com.pabloam.microservices.converter.provider.exceptions.ConvertionException;

public interface ConverterServices {

	/**
	 * Converts the given response into a converted response
	 * 
	 * @param jsonResponse
	 *            the received response in json
	 * @return
	 */
	ConvertedResponse convert(String jsonResponse) throws ConvertionException;

}
