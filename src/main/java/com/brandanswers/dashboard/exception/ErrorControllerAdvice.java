package com.brandanswers.dashboard.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.brandanswers.dashboard.orchestrator.OrchestratorException;

@ControllerAdvice
@ResponseBody
public class ErrorControllerAdvice extends ResponseEntityExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(ErrorControllerAdvice.class);

	@ExceptionHandler()
	public static ResponseEntity<?> handleGenericException(Exception e) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("timestamp", new Date().toString());
		map.put("message", e.getMessage());
		map.put("error", "true");
		logger.error(e.getMessage(), e);
		return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(OrchestratorException.class)
	public static ResponseEntity<?> handleRuntimeException(OrchestratorException e) {
		HttpStatus statusCode= generateHttpStatusCode(e.getErrorCode());
		Throwable error = Optional.ofNullable(e.getError()).map(x -> x.getCause()).orElse(e);
		Map<String, String> map = new HashMap<String, String>();
		map.put("timestamp", new Date().toString());
		if (e instanceof OrchestratorException) {
			OrchestratorException err = (OrchestratorException) error;
			map.put("errorCode", err.getErrorCode());
			statusCode= generateHttpStatusCode(err.getErrorCode());
		}
		map.put("message", error.getMessage());
		map.put("error", "true");
		logger.error(error.getMessage(), error);
		return new ResponseEntity<>(map, statusCode);
	}
	
	private static HttpStatus generateHttpStatusCode(String errorCode) {
		HttpStatus statusCode= HttpStatus.INTERNAL_SERVER_ERROR;
		switch(errorCode) {
		case "ORC-0500": 
			statusCode = HttpStatus.UNAUTHORIZED;
			break;
		}
		return statusCode;
		
	}

	@ExceptionHandler(HttpStatusCodeException.class)
	public static ResponseEntity<?> handleHttpException(HttpStatusCodeException e) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("timestamp", new Date().toString());
		map.put("message", e.getMessage());
		map.put("error", "true");
		logger.error(e.getMessage(), e);
		return new ResponseEntity<>(map, e.getStatusCode());
	}
}