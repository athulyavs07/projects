package com.brandanswers.dashboard.websecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.brandanswers.dashboard.jwtFilter.JwtFilter;
import com.brandanswers.dashboard.security.JwtAuthenticationEntryPoint;
@Component
@Configuration
@EnableWebSecurity
public class WebSecurity  extends WebSecurityConfigurerAdapter{
	@Autowired
    private CustomUserDetailService userDetailsService;
	
	@Autowired
	  private JwtAuthenticationEntryPoint unauthorizedHandler;

	  private JwtFilter jwtFilter;

	  public WebSecurity(JwtFilter jwtFilter) {
	    this.jwtFilter = jwtFilter;

	  }
	  @Bean
	  public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	  } 
	  
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER) 
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
		.exceptionHandling().authenticationEntryPoint(unauthorizedHandler);
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(org.springframework.security.config.annotation.web.builders.WebSecurity web)
			throws Exception {
		 web.ignoring().antMatchers("/**","/resources/**","/static/**","/*.json","/*.txt","/api/v1/log","/signin","/api/v1/test", "/login", "/*.ico", "/*.jpeg/", "/*.png", "/*.GIF", "/*.js.map",
			    "/*.css.map","/*.css", "/*.html", "/*.js", "/assets/**", "/docs/**","/api/reset/**");
		
	}
	
}
