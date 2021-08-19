package com.brandanswers.dashboard.orchestrator;

import javax.validation.Validation;
import javax.validation.executable.ExecutableValidator;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class ValidatorEngine {
	private ExecutableValidator validator;
	ValidatorEngine(){
		this.validator = Validation.buildDefaultValidatorFactory().getValidator().forExecutables();
	}
	
	public <T> void validate(String name,T value) {
		
	}
}
