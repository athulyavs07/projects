package com.brandanswers.dashboard.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brandanswers.dashboard.funson.PropertyHelper;
import com.brandanswers.dashboard.orchestrator.OrchestratorEngine;
import com.brandanswers.dashboard.orchestrator.OrchestratorException;

@RequestMapping("/api/reset")
@RestController
@Component
@CrossOrigin
public class ResetController extends BaseController {
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private OrchestratorEngine engine;

	 @Autowired
	    private PropertyHelper ph;
	 private static final String TOKEN_IS_INVALID = "ERR-0002";
	 private static final String TOKEN_IS_NOT_VERIFIED = "ERR-0003";
	 private static final String EMAIL_IS_INVALID = "ERR-0004";
	 
	@PostMapping("/forget-password")
	public ResponseEntity<org.json.simple.JSONObject> resetPassword(@RequestBody Map<String, String> customQuery)
			throws ParseException, UnsupportedEncodingException, MessagingException {
		String username = customQuery.get("username");
		String token = UUID.randomUUID().toString();
		JSONObject obj = new JSONObject();
		System.out.println(username);
		obj.put("username", username);
		org.json.simple.JSONObject response = convertToSimpleJsonObject(this.engine.processRequest(obj, "emailVerification"));
		if (response.get("Response").equals("Email is invalid")) {
			throw new OrchestratorException(String.format("EMAIL IS INVALID"), EMAIL_IS_INVALID);
		} else {
			obj.put("token", token);
			org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
					this.engine.processRequest(obj, "forgotPassword"));

			String resetLink = "http://3.85.61.180:8080" + "/reset?token=" + token;
			sendEmail(username, resetLink);
			return ResponseEntity.ok(responseData);
		}

	}

	public void sendEmail(String recipientEmail, String link) throws UnsupportedEncodingException, MessagingException {
	
    	String from =ph.getEmailId();
    	String port = ph.getSmtpPort();
    	String password = ph.getPassword();
    	 String subject = "Here's the link to reset your password";
        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(from, password);

            }

        });
       

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
           
            // Set Subject: header field
            message.setSubject(subject);
            String body ="<p>Hello,</p>"
                    + "<p>You have requested to reset your password.</p>"
                    + "<p>Click the link below to change your password:</p>"
                    + "<p><a href=\"" + link + "\">Change my password</a></p>"
                    + "<br>"
                    + "<p>Ignore this email if you do remember your password, "
                    + "or you have not made the request.</p>";

            // Now set the actual message
            message.setContent(
            		body,
                  "text/html");
            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
	@PostMapping("/reset-password")
	public ResponseEntity<org.json.simple.JSONObject> processResetPassword(@RequestBody Map<String, String> customQuery)
			throws ParseException, UnsupportedEncodingException, MessagingException {
		org.json.simple.JSONObject response = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "validateToken"));
		if (response.get("Response").equals("Token is invalid")) {
			throw new OrchestratorException(String.format("TOKEN IS INVALID"), TOKEN_IS_INVALID);
		}
		else if (response.get("Response").equals("Not Verified")) {
			throw new OrchestratorException(String.format("TOKEN IS NOT VERIFIED"), TOKEN_IS_NOT_VERIFIED);
		}
		else {	
		org.json.simple.JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "resetPassword"));
		return ResponseEntity.ok(responseData);
	}
	}


}
