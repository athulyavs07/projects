package com.brandanswers.dashboard.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.brandanswers.dashboard.orchestrator.OrchestratorEngine;
import com.brandanswers.dashboard.service.FilesStorageService;

@RequestMapping("/api/RAD")
@RestController
@Component
@CrossOrigin
public class RADController extends BaseController {
	
	@Autowired
	private OrchestratorEngine engine;

	@GetMapping("/searchRADDetails")
	public ResponseEntity<?> searchEmployee(@RequestParam Map<String, String> customQuery) throws ParseException {
		JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "searchRADDetails"));
		return ResponseEntity.ok(responseData);
	}
	@GetMapping("/totalRAD")
	public ResponseEntity<org.json.simple.JSONObject> selectTotalRAD(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "totalRAD"));
		return ResponseEntity.ok(responseData);
	}
	@GetMapping("/radInternCount")
	public ResponseEntity<org.json.simple.JSONObject> radInternCount(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "radInternCount"));
		return ResponseEntity.ok(responseData);
	}
	@GetMapping("/roleStatusBar")
	public ResponseEntity<org.json.simple.JSONObject> roleStatusBar(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "roleStatusBar"));
		return ResponseEntity.ok(responseData);
	}
	@PostMapping(value = "/insertRAD", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> insertEmployee(@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestPart(value = "resume", required = false) MultipartFile resume,
			@RequestPart(value = "idproofattachment", required = false) MultipartFile idproofattachment,
			@RequestParam Map<String, String> customQuery) throws ParseException, IOException {
		org.json.JSONObject message = new org.json.JSONObject();
		String filePath = "";
		String resumePath = "";
		String idproofPath = "";
		String encodedString="";
		String encodedStringID="";
		System.out.println(file.getSize());
		if (!file.isEmpty() && (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png"))  && file.getSize()<=1024*1024) {
			filePath=FilesStorageService.save(file);
			byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
		     encodedString = Base64.getEncoder().encodeToString(fileContent);
		     System.out.println(encodedString);
			message.put(file.getOriginalFilename(), "Uploaded the file successfully");
		}
		System.out.println(resume.getSize());
		if (!resume.isEmpty() && (resume.getContentType().equals("application/pdf") || resume.getContentType().equals("application/docx"))&& resume.getSize()<=1024*1024) {
			resumePath=FilesStorageService.save(resume);
			message.put(resume.getOriginalFilename(), "Uploaded the file successfully");
		}
		System.out.println(idproofattachment.getSize());
		if (!idproofattachment.isEmpty() && (idproofattachment.getContentType().equals("image/jpeg") || idproofattachment.getContentType().equals("image/png"))&& idproofattachment.getSize()<=1024*1024  ) {
			idproofPath=FilesStorageService.save(idproofattachment);
			byte[] fileContentID = FileUtils.readFileToByteArray(new File(idproofPath));
		    encodedStringID = Base64.getEncoder().encodeToString(fileContentID);
			message.put(idproofattachment.getOriginalFilename(), "Uploaded the file successfully");
		}
		org.json.JSONObject params = getParams(customQuery);
		
		params.put("photo", encodedString);
		params.put("resumePath", resumePath);
		params.put("idproofPath", encodedStringID);
		org.json.JSONObject responseData = this.engine.processRequest(params, "insertRAD");
		message.put("Response", responseData);
		JSONObject message1 = convertToSimpleJsonObject(message);
		return ResponseEntity.ok(message1);
	}
	@GetMapping("/department")
	public ResponseEntity<?> departmentList(@RequestParam Map<String, String> customQuery)
			throws ParseException {
		JSONObject responseData = convertToSimpleJsonObject(this.engine.processRequest(getParams(customQuery),"departmentList"));
		return ResponseEntity.ok(responseData);
	}
	@GetMapping("/role")
	public ResponseEntity<?> roleList(@RequestParam Map<String, String> customQuery)
			throws ParseException {
		JSONObject responseData = convertToSimpleJsonObject(this.engine.processRequest(getParams(customQuery),"roleList"));
		return ResponseEntity.ok(responseData);
	}
	@GetMapping("/roleDescription")
	public ResponseEntity<?> roleDescriptionList(@RequestParam Map<String, String> customQuery)
			throws ParseException {
		JSONObject responseData = convertToSimpleJsonObject(this.engine.processRequest(getParams(customQuery),"roleDescriptionList"));
		return ResponseEntity.ok(responseData);
	}
	@GetMapping("/id")
	public ResponseEntity<?> searchIndividualRAD(@RequestParam Map<String, String> customQuery) throws ParseException {
		JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "searchIndividualRAD"));
		return ResponseEntity.ok(responseData);
	}
	@PutMapping(value = "/id", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> updateRAD(@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestPart(value = "resume", required = false) MultipartFile resume,
			@RequestPart(value = "idproofattachment", required = false) MultipartFile idproofattachment,
			@RequestParam Map<String, String> customQuery) throws ParseException, IOException {
		org.json.JSONObject message = new org.json.JSONObject();
		String filePath = "";
		String resumePath = "";
		String idproofPath = "";
		String encodedString="";
		String encodedStringID="";
		System.out.println(file.getSize());
		if (!file.isEmpty() && (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png"))  && file.getSize()<=1024*1024) {
			filePath=FilesStorageService.save(file);
			byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
		     encodedString = Base64.getEncoder().encodeToString(fileContent);
			message.put(file.getOriginalFilename(), "Uploaded the file successfully");
		}
		System.out.println(resume.getSize());
		if (!resume.isEmpty() && (resume.getContentType().equals("application/pdf") || resume.getContentType().equals("application/docx"))&& resume.getSize()<=1024*1024) {
			resumePath=FilesStorageService.save(resume);
			message.put(resume.getOriginalFilename(), "Uploaded the file successfully");
		}
		System.out.println(idproofattachment.getSize());
		if (!idproofattachment.isEmpty() && (idproofattachment.getContentType().equals("image/jpeg") || idproofattachment.getContentType().equals("image/png"))&& idproofattachment.getSize()<=1024*1024  ) {
			idproofPath=FilesStorageService.save(idproofattachment);
			byte[] fileContentID = FileUtils.readFileToByteArray(new File(idproofPath));
		   encodedStringID = Base64.getEncoder().encodeToString(fileContentID);
			message.put(idproofattachment.getOriginalFilename(), "Uploaded the file successfully");
		}
		org.json.JSONObject params = getParams(customQuery);
		
		params.put("photo", encodedString);
		params.put("resumePath", resumePath);
		params.put("idproofPath", encodedStringID);
		org.json.JSONObject responseData = this.engine.processRequest(params, "updateRAD");
		message.put("Response", responseData);
		JSONObject message1 = convertToSimpleJsonObject(message);
		return ResponseEntity.ok(message1);

	}
	@PutMapping("/delete/id")
	public ResponseEntity<org.json.simple.JSONObject> deleteEmployeeDetails(@RequestParam Map<String, String> customQuery)
			throws ParseException {

		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "updateStatus"));
		return ResponseEntity.ok(responseData);
	}
	@GetMapping("/resume/id")
	public ResponseEntity<InputStreamResource> getResume(@RequestParam Map<String, String> customQuery)
			throws ParseException, FileNotFoundException {

        org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "getResume"));
       String f=  (String) responseData.get("file");
       File file = new File(f);
        String fileName=responseData.get("fileName").toString();
        HttpHeaders headers = new HttpHeaders();      
        headers.add("content-disposition", "inline;filename=" +fileName);
        InputStreamResource resource = null;
        if(file.isFile()) {
         resource = new InputStreamResource(new FileInputStream(file));
              }
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);

        
    }

	@GetMapping("/miscellaneous")
	public ResponseEntity<?> miscellaneous(@RequestParam Map<String, String> customQuery)
			throws ParseException {
		JSONObject responseData = convertToSimpleJsonObject(this.engine.processRequest(getParams(customQuery),"miscellaneous"));
		return ResponseEntity.ok(responseData);
	}

}
