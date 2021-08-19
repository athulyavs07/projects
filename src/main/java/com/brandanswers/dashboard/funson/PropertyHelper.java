package com.brandanswers.dashboard.funson;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@Scope("singleton")
public class PropertyHelper {
	
	@Value("${send_email}")
	private String emailAccess;
	
	@Value("${email_id}")
	private String emailId;
	
	@Value("${password}")
	private String password;
	
	@Value("${smtp_port}")
	private String smtpPort;

	@Value("${doc_folder}")
	private String docFolder;
	
	public String getSmtpPort() {
		return smtpPort;
	}
	public String getEmailAccess() {
		return emailAccess;
	}
	public String getEmailId() {
		return emailId;
	}
	public String getDocFolder() {
		return docFolder;
	}
	public String getPassword() {
		return password;
	}

}
