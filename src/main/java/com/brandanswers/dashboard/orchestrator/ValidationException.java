package com.brandanswers.dashboard.orchestrator;

import java.util.List;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1072986036040881105L;
	private List<String> errorMessage;

	public ValidationException(String msg,List<String> errorMessage) {
		super(msg);
		this.errorMessage = errorMessage;
	}
	

	public ValidationException(String msg, Exception e,List<String> errorMessage) {
		super(msg, e);
		this.errorMessage = errorMessage;
	}


	public List<String> getErrorMessage() {
		return errorMessage;
	}
	
	

}