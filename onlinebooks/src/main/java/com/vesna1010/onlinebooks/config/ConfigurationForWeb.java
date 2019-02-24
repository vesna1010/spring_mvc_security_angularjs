package com.vesna1010.onlinebooks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
@ComponentScan(
		basePackages = { "com.vesna1010.onlinebooks" }, 
        useDefaultFilters = false, 
        includeFilters = @ComponentScan.Filter(
        		type = FilterType.ANNOTATION, 
                classes = { RestController.class, RestControllerAdvice.class }))
public class ConfigurationForWeb implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("/");
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();

		multipartResolver.setMaxUploadSize(-1);
		multipartResolver.setMaxInMemorySize(10240);

		return multipartResolver;
	}

}