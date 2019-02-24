package com.vesna1010.onlinebooks.initializer;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import com.vesna1010.onlinebooks.config.ConfigurationForDatabase;
import com.vesna1010.onlinebooks.config.ConfigurationForSecurity;
import com.vesna1010.onlinebooks.config.ConfigurationForWeb;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { ConfigurationForDatabase.class, ConfigurationForSecurity.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { ConfigurationForWeb.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}
