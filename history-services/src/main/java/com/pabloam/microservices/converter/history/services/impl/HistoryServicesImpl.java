/**
 * Copyright (c) 2016 Molenaar Strategie BV.
 * Created: 3 May 2016 15:52:08 Author: Pablo
 */

package com.pabloam.microservices.converter.history.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pabloam.microservices.converter.history.repositories.CurrencyQueryRepository;

/**
 * @author Pablo
 *
 */
@Service
public class HistoryServicesImpl {

	@Autowired
	CurrencyQueryRepository currencyQueryRepository;

}
