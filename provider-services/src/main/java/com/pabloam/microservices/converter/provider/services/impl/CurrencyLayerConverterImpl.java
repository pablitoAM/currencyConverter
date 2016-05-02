package com.pabloam.microservices.converter.provider.services.impl;

import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
	public ConvertedResponse convert(Map<String, Object> response) throws ConvertionException {
		if (CollectionUtils.isEmpty(response)) {
			throw new ConvertionException("The response to convert cannot be null");
		}
		return null;

	}

}
