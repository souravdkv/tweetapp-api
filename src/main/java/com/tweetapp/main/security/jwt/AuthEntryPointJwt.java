package com.tweetapp.main.security.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		String unautharizedErrorMessage = String.format("%s %s", "Unauthorized error:", authException.getMessage());
		logger.error(unautharizedErrorMessage);

		String unauthorizedUrlMessage = String.format("%s %s", "Unauthorized URL:", request.getRequestURI());
		logger.error(unauthorizedUrlMessage);
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error:" + " " + "Unauthorized");
	}
}