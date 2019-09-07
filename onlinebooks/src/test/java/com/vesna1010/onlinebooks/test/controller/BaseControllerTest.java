package com.vesna1010.onlinebooks.test.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.vesna1010.onlinebooks.config.ConfigurationForDatabase;
import com.vesna1010.onlinebooks.config.ConfigurationForSecurity;
import com.vesna1010.onlinebooks.config.ConfigurationForWeb;
import com.vesna1010.onlinebooks.test.BaseTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ConfigurationForWeb.class, ConfigurationForSecurity.class,
		ConfigurationForDatabase.class })
@WebAppConfiguration
public abstract class BaseControllerTest extends BaseTest {

	@Autowired
	protected WebApplicationContext context;
	protected MockMvc mockMvc;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(context)
			.apply(springSecurity())
			.build();
	}

}
