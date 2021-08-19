package com.brandanswers.dashboard.orchestrator;

import org.apache.commons.validator.GenericValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BeanConfig {

	@Bean
	public GenericValidator genValidator() {
		return new GenericValidator();
	}
	
	@Bean
	@Scope("singleton")
	public FutureExecutor getFutureExecutor() {
		return new FutureExecutor();
	}
}