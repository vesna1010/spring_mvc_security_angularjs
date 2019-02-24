package com.vesna1010.onlinebooks.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class ConfigurationForSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;
	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	@Autowired
	private AccessDeniedHandler accessDeniedHandler;
	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;

	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.jdbcAuthentication()
				.usersByUsernameQuery("SELECT USERNAME, PASSWORD, ENABLED FROM USERS WHERE USERNAME=?")
				.authoritiesByUsernameQuery("SELECT USERNAME, AUTHORITY FROM USERS WHERE USERNAME=?")
				.dataSource(dataSource).passwordEncoder(passwordEncoder);
	}

	@Override
	public void configure(WebSecurity security) throws Exception {
		security.ignoring().antMatchers("/static/**");
	}

	@Override
	protected void configure(HttpSecurity security) throws Exception {
		security.authorizeRequests()
		            .antMatchers(HttpMethod.GET, "/authenticated").permitAll()
				    .antMatchers(HttpMethod.GET, "/authorities").permitAll()
				    .antMatchers(HttpMethod.GET, "/languages").permitAll()
				    .antMatchers(HttpMethod.GET, "/categories").permitAll()
				    .antMatchers(HttpMethod.GET, "/books").permitAll()
				    .antMatchers(HttpMethod.GET, "/books/title/**").permitAll()
				    .antMatchers(HttpMethod.GET, "/books/author/**").permitAll()
				    .antMatchers(HttpMethod.GET, "/books/category/**").permitAll()
				    .antMatchers(HttpMethod.GET, "/books/language/**").permitAll()
				    .antMatchers(HttpMethod.GET, "/books/download/**").permitAll()
				    .antMatchers("/users/**").hasAuthority("ADMIN")
				    .antMatchers("/categories/**").authenticated()
				    .antMatchers("/authors/**").authenticated()
				    .antMatchers("/books/**").authenticated()
				.and()
				.formLogin()
				    .failureHandler(authenticationFailureHandler)
				    .successHandler(authenticationSuccessHandler)
				.and()
				.logout()
				    .logoutSuccessHandler(logoutSuccessHandler)
				.and()
				.exceptionHandling()
				    .accessDeniedHandler(accessDeniedHandler)
				    .authenticationEntryPoint(authenticationEntryPoint)
				.and()
				.csrf()
				    .disable();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new AuthenticationEntryPoint() {

			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
			}

		};
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new AuthenticationFailureHandler() {

			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {

				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
			}

		};
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new AuthenticationSuccessHandler() {

			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				response.setStatus(HttpServletResponse.SC_OK);
			}

		};
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new AccessDeniedHandler() {

			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
					AccessDeniedException exception) throws IOException, ServletException {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, exception.getMessage());
			}

		};
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new LogoutSuccessHandler() {

			@Override
			public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				response.setStatus(HttpServletResponse.SC_OK);
			}

		};
	}

}
