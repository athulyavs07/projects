package com.brandanswers.dashboard.controller;

import java.util.Map;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brandanswers.dashboard.orchestrator.OrchestratorEngine;

@RequestMapping("/api/change")
@RestController
@Component
@CrossOrigin
public class ChangePasswordController extends BaseController {

	@Autowired
	private OrchestratorEngine engine;
	
	@PostMapping("/change-password")
	public ResponseEntity<org.json.simple.JSONObject> processResetPassword(@RequestBody Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "changePassword"));
		return ResponseEntity.ok(responseData);
	}
}
