package com.brandanswers.dashboard.websecurity;
import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.brandanswers.dashboard.orchestrator.OrchestratorEngine;
import com.brandanswers.dashboard.orchestrator.OrchestratorException;

@Service
public class CustomUserDetailService implements UserDetailsService {
	 @Autowired
		private OrchestratorEngine engine;
	 private static final String BAD_CREDENTIALS = "ERR-0004";
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		JSONObject obj = new JSONObject(); 
		obj.put("username", username);
		org.json.JSONObject responseData =this.engine.processRequest(obj, "getUser");
		if(responseData.isEmpty()) {
			throw new OrchestratorException(String.format("Bad Credentials"), BAD_CREDENTIALS);
		}
        User userItem =  new User(responseData.get("username").toString(),responseData.get("password").toString(),true, true, true, true, new ArrayList<>());
        return  userItem;
	}

}