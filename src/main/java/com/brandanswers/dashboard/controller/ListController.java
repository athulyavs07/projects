package com.brandanswers.dashboard.controller;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brandanswers.dashboard.orchestrator.OrchestratorEngine;

@RequestMapping("/api/data")
@RestController
@Component
@CrossOrigin
public class ListController extends BaseController{
	@Autowired
	private OrchestratorEngine engine;
	
	@GetMapping("/getUser")
	public ResponseEntity<?> searchCustomerName(@RequestParam Map<String, String> customQuery)
			throws ParseException {
		JSONObject responseData = convertToSimpleJsonObject(this.engine.processRequest(getParams(customQuery),"getUser"));
		return ResponseEntity.ok(responseData);
	}
	

}
