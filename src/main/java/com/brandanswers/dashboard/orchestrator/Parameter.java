package com.brandanswers.dashboard.orchestrator;

import java.util.List;

public class Parameter {
	private String name;
	private String type;
	private boolean isNullable;
	private List<String> validations;
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public boolean getIsNullable() {
		return isNullable;
	}
	public List<String> getValidations() {
		return validations;
	}
	public Parameter(String name, String type,boolean isNullable,List<String> validations) {
		super();
		this.name = name;
		this.type = type;
		this.isNullable = isNullable;
		this.validations = validations;
	}
	
	
}
