package com.brandanswers.dashboard.orchestrator;

import java.util.ArrayList;
import java.util.List;

public class Func {

	private String type = "method";
	private String className;
	private String name;
	private String queryName;
	private String mapper;
	private List<String> params;
	private List<Func> functions;

	public String getType() {
		return type;
	}
	
	public String getClassName() {
		return className;
	}

	public String getName() {
		return name;
	}

	public String getQueryName() {
		return queryName;
	}
	
	public String getMapper() {
		return mapper;
	}

	public List<String> getParams() {
		return params;
	}
	
	public List<Func> getfunctions() {
		return functions;
	}

	public void addFunctions(Func function) {
		this.functions.add(function);
	}
	
	public Func(String type, String className, String name, String queryName, List<String> params, String mapper) {
		super();
		this.type = type;
		this.className = className;
		this.name = name;
		this.queryName = queryName;
		this.params = params;
		this.mapper = mapper;
		
		if(this.type.equals("future")) {
			this.functions = new ArrayList<Func>();
		}
		
		if (this.type.equals("funson") && this.className != null) {
			throw new OrchestratorException(String.format("Funson and Method cannot configured simultaneously - %s",this.name));
		}
		
		if(this.type.equals("method") && (this.className == null || this.name == null)) {
			throw new OrchestratorException(String.format("Funson  Method require class name and method name - %s",this.name));
		}
		
	}

}