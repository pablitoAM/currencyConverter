package com.pabloam.microservices.converter.provider.services.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.pabloam.microservices.converter.common.ConvertedResponse;
import com.pabloam.microservices.converter.provider.exceptions.ConvertionException;
import com.pabloam.microservices.converter.provider.services.ConverterServices;

@Service
@Profile("currencylayer")
public class CurrencyLayerConverterImpl implements ConverterServices {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pabloam.microservices.converter.provider.services.ConverterServices#
	 * convert(java.lang.String)
	 */
	@Override
	public ConvertedResponse convert(String jsonResponse) throws ConvertionException {
		return null;
	}

}
