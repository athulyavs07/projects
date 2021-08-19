//Handles runtime exceptions

package com.brandanswers.dashboard.orchestrator;


public class OrchestratorException extends RuntimeException {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Throwable error = null;
	private String errorCode="";

	public OrchestratorException(String msg) {
		super(msg);
	}
	
	public OrchestratorException(String msg,String errorCode) {
		super(msg);
		this.errorCode = errorCode;
	}

	public OrchestratorException(String msg, Exception e) {
		super(msg, e);
		this.error = e;
	}
	
	public OrchestratorException(String msg,String errorCode, Exception e) {
		super(msg, e);
		this.error = e;
		this.errorCode = errorCode;
	}

	public OrchestratorException(String msg, ReflectiveOperationException e) {
		super(msg, e);
		this.error = e;
	}

	public Throwable getError() {
		return error;
	}
	
	public String getErrorCode() {
		return this.errorCode;
	}
}
