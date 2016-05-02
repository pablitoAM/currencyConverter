package com.pabloam.microservices.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserServicesApplication.class)
@WebAppConfiguration
public class UserServicesApplicationTests {

	@Test
	public void contextLoads() {
	}

}
