package com.brandanswers.dashboard.controller;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.brandanswers.dashboard.orchestrator.OrchestratorEngine;
import com.brandanswers.dashboard.service.FilesStorageService;

@RequestMapping("/api/overview")
@RestController
@Component
@CrossOrigin
public class OverviewController extends BaseController{
	
	@Autowired
	private OrchestratorEngine engine;
	
	@GetMapping("/noOfProjects")
	public ResponseEntity<org.json.simple.JSONObject> noOfProjects(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "noOfProjects"));
		return ResponseEntity.ok(responseData);
	}
	
	@GetMapping("/activeFinishedProjects")
	public ResponseEntity<org.json.simple.JSONObject> activeFinishedProjects(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "activeFinishedProjects"));
		return ResponseEntity.ok(responseData);
	}
	@GetMapping("/selectClientDetails")
	public ResponseEntity<org.json.simple.JSONObject> selectClientDetails(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "selectClientDetails"));
		return ResponseEntity.ok(responseData);
	}
	@GetMapping("/totalRAD")
	public ResponseEntity<org.json.simple.JSONObject> selectTotalRAD(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "totalRAD"));
		return ResponseEntity.ok(responseData);
	}
	@GetMapping("/activeInactiveRAD")
	public ResponseEntity<org.json.simple.JSONObject> selectactiveInactiveRAD(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "activeInactiveRAD"));
		return ResponseEntity.ok(responseData);
	}
	@GetMapping("/totalEmployees")
	public ResponseEntity<org.json.simple.JSONObject> totalEmployees(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "totalEmployees"));
		return ResponseEntity.ok(responseData);
	} 
	
	@PostMapping("/insertSalary")
	public ResponseEntity<org.json.simple.JSONObject> insertSalary(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "insertSalary"));
		return ResponseEntity.ok(responseData);
	}
	
	@GetMapping("/projectDetails")
	public ResponseEntity<org.json.simple.JSONObject> projectDetails(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "projectDetails"));
		return ResponseEntity.ok(responseData);
	}
	@GetMapping("/revenueDetails")
	public ResponseEntity<org.json.simple.JSONObject> revenueDetails(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "revenueDetails"));
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
	
	@PostMapping(value = "/updateEmployee", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<org.json.simple.JSONObject> updateEmployee(@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestParam Map<String, String> customQuery)
			throws ParseException {
			org.json.JSONObject message = new org.json.JSONObject();
			String filePath = "";
			if (file!=null && !file.isEmpty() && (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) ) {
				filePath=FilesStorageService.save(file);
				message.put(file.getOriginalFilename(), "Uploaded the file successfully");
			}
			org.json.JSONObject params = getParams(customQuery);
			params.put("attachment", filePath);
			org.json.JSONObject responseData = this.engine.processRequest(params, "updateEmployee");
			message.put("Response", responseData);
			JSONObject message1 = convertToSimpleJsonObject(message);
			return ResponseEntity.ok(message1);

		}
	
	@DeleteMapping("/deleteEmployeeDetails")
	public ResponseEntity<org.json.simple.JSONObject> deleteEmployeeDetails(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "deleteEmployeeDetails"));
		return ResponseEntity.ok(responseData);
	}
	
	@PostMapping("/insertClients")
	public ResponseEntity<org.json.simple.JSONObject> insertClients(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "insertClients"));
		return ResponseEntity.ok(responseData);
	}
	
	@DeleteMapping("/deleteClientDetails")
	public ResponseEntity<org.json.simple.JSONObject> deleteClientDetails(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "deleteClientDetails"));
		return ResponseEntity.ok(responseData);
	}
	
	@PostMapping("/updateClients")
	public ResponseEntity<org.json.simple.JSONObject> updateClients(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "updateClients"));
		return ResponseEntity.ok(responseData);
	}
	@PostMapping("/insertProjects")
	public ResponseEntity<org.json.simple.JSONObject> insertProjects(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "insertProjects"));
		return ResponseEntity.ok(responseData);
	}
	@PostMapping("/updateProjects")
	public ResponseEntity<org.json.simple.JSONObject> updateProjects(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "updateProjects"));
		return ResponseEntity.ok(responseData);
	}
	@PutMapping("/updateSalary")
	public ResponseEntity<org.json.simple.JSONObject> updateSalary(@RequestBody Map<String, String> customQuery)
			throws ParseException {
		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "updateSalary"));
		return ResponseEntity.ok(responseData);
	}
	@GetMapping("/projects")
	public ResponseEntity<?> projects(@RequestParam Map<String, String> customQuery)
			throws ParseException {
		JSONObject responseData = convertToSimpleJsonObject(this.engine.processRequest(getParams(customQuery),"projects"));
		return ResponseEntity.ok(responseData);
	}
	
}
