package com.brandanswers.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.brandanswers.dashboard.models.User;
import com.brandanswers.dashboard.models.JwtResponse;
import com.brandanswers.dashboard.orchestrator.OrchestratorEngine;
import com.brandanswers.dashboard.security.JwtTokenUtil;

@RestController
@Component
@CrossOrigin
public class AuthController extends BaseController {
	@Autowired
	  private AuthenticationManager authenticationManager;
	 @Autowired
	    private JwtTokenUtil jwtTokenUtil;
	 @Autowired
		private OrchestratorEngine engine;
	 
		@PostMapping("/signin")
		public ResponseEntity<?> signin(@RequestBody User credential)  {
			Authentication authentication =  authenticationManager.authenticate(
			            new UsernamePasswordAuthenticationToken(credential.getUsername(), credential.getPassword()));

			   String jwt = jwtTokenUtil.generateToken(authentication);
			   
			   return ResponseEntity.ok(new JwtResponse(jwt));
			
		}
		


}
