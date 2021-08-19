package com.brandanswers.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.RegistrationBean;
import org.springframework.context.annotation.Bean;

import com.brandanswers.dashboard.jwtFilter.JwtFilter;


@SpringBootApplication
public class DashboardBrandanswersIncApplication{

	public static void main(String[] args) {
		SpringApplication.run(DashboardBrandanswersIncApplication.class, args);
	}
	
	@Bean
	public RegistrationBean jwtAuthFilterRegister(JwtFilter filter) {
		FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<JwtFilter>(filter);
		registrationBean.setEnabled(false);
		return registrationBean;
	}

}
