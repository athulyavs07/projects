package com.brandanswers.dashboard.orchestrator;

import java.util.List;

public class Funson {
	public Funson(String name, List<Parameter> parameters, List<Func> functions) {
		super();
		this.name = name;
		this.parameters = parameters;
		this.functions = functions;
	}

	private String name;
	private List<Parameter> parameters;
	private List<Func> functions;
	
	public String getName() {
		return name;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}
	
	public List<Func> getFunctions() {
		return functions;
	}
	
}
