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

@RequestMapping("/api/company")
@RestController
@Component
@CrossOrigin
public class CompanyController extends BaseController{

	@Autowired
	private OrchestratorEngine engine;

	@GetMapping("/searchTotalEmployee")
	public ResponseEntity<JSONObject> searchTotalEmployee(@RequestParam Map<String, String> customQuery)
			throws ParseException {
		JSONObject responseData = convertToSimpleJsonObject(this.engine.processRequest(getParams(customQuery),"searchTotalEmployee"));
		return ResponseEntity.ok(responseData);
	}
	@GetMapping("/searchTotalTerminatedEmployee")
	public ResponseEntity<JSONObject> searchTotalTerminatedEmployee(@RequestParam Map<String, String> customQuery)
			throws ParseException {
		JSONObject responseData = convertToSimpleJsonObject(this.engine.processRequest(getParams(customQuery),"searchTotalTerminatedEmployee"));
		return ResponseEntity.ok(responseData);
	}
}
