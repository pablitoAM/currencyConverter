package com.pabloam.microservices.converter.provider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pabloam.microservices.converter.provider.ProviderServicesApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProviderServicesApplication.class)
@WebAppConfiguration
public class ProviderServicesApplicationTests {

	@Test
	public void contextLoads() {
	}

}
