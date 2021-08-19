package com.brandanswers.dashboard.controller;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.brandanswers.dashboard.orchestrator.OrchestratorEngine;
import com.brandanswers.dashboard.service.FilesStorageService;

@RequestMapping("/api/employee")
@RestController
@Component
@CrossOrigin
public class EmployeeController extends BaseController {

	@Autowired
	private OrchestratorEngine engine;

	@GetMapping("/searchEmployee")
	public ResponseEntity<?> searchEmployee(@RequestParam Map<String, String> customQuery) throws ParseException {
		JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "searchEmployee"));
		return ResponseEntity.ok(responseData);
	}

	@PostMapping(value = "/insertEmployee", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> insertEmployee(@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestParam Map<String, String> customQuery) throws ParseException {
		org.json.JSONObject message = new org.json.JSONObject();
		String filePath = "";
		if (!file.isEmpty() && (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) ) {
			filePath=FilesStorageService.save(file);
			message.put(file.getOriginalFilename(), "Uploaded the file successfully");
		}
		org.json.JSONObject params = getParams(customQuery);
		params.put("attachment", filePath);
		org.json.JSONObject responseData = this.engine.processRequest(params, "insertEmployee");
		message.put("Response", responseData);
		JSONObject message1 = convertToSimpleJsonObject(message);
		return ResponseEntity.ok(message1);

	}
}
