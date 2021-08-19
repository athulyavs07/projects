package com.brandanswers.dashboard.jwtFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.brandanswers.dashboard.models.User;
import com.brandanswers.dashboard.security.JwtTokenUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtFilter extends OncePerRequestFilter {
	@Autowired
	private User credential;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	private boolean isAuthorized = false;
	private final Map<String, String[]> userDetails;
	
	public JwtFilter() {
		super();
		this.userDetails = new HashMap<String, String[]>();
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		final String requestTokenHeader = request.getHeader("authorization");

		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer")) {
			String jwtToken = requestTokenHeader.substring(7);
			if (this.jwtTokenUtil.validateToken(jwtToken)) {
				try {
					Claims claim = jwtTokenUtil.getClaims(jwtToken);
					this.userDetails.put("username", new String[]{claim.get("username").toString()});
				
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							credential.getUsername(), credential.getPassword());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					this.isAuthorized = true;

				} catch (IllegalArgumentException e) {
					System.out.println("Unable to get JWT Token");
				} catch (ExpiredJwtException e) {
					System.out.println("JWT Token has expired");
				}
			}

		}

		if (!this.isAuthorized) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Sorry, You're not authorized to access this resource.");

		}

		chain.doFilter(new WrappedRequest(request,this.userDetails), response);
	}

	class WrappedRequest extends HttpServletRequestWrapper {


		Map<String, String[]> additionalParams = new HashMap<String, String[]>();
		public WrappedRequest(ServletRequest request, Map<String, String[]> additionalParams) {
			super((HttpServletRequest) request);
			this.additionalParams.putAll(super.getParameterMap());
			this.additionalParams.putAll(additionalParams);
		}

		@Override
		public String getParameter(String name) {
			if (this.additionalParams.get(name) != null) {
				return this.additionalParams.get(name)[0];
			}
			return null;
		}
		
		@Override
		public Map<String, String[]> getParameterMap() {
			return this.additionalParams;
		}
		
		@Override
		public String[] getParameterValues(String name) {
			return this.additionalParams.get(name);
		}
		
		@Override
		public Enumeration<String> getParameterNames() {
			return Collections.enumeration(this.additionalParams.keySet());
		}

	}
}