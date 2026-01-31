package com.example.demo.config;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler  {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	JwtService jwtService;
	


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		 logger.info("Successful authentication");
	        logger.info(authentication.toString());
	        
	        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
	        
	        String registrationId = "unknown";
	        if (authentication instanceof OAuth2AuthenticationToken token) {
	            registrationId = token.getAuthorizedClientRegistrationId();
	        }

	        logger.info("registrationId:" + registrationId);
	        logger.info("user:" + oAuth2User.getAttributes().toString());
	        
	        

	        String email = oAuth2User.getAttribute("email");
	        String name = oAuth2User.getAttribute("name");
	        String picture = oAuth2User.getAttribute("picture");

	        // 🔥 Generate JWT
	        String token = jwtService.generateToken(email);

	        // 🔥 Redirect to frontend with data
	        String redirectUrl = String.format(
	                "http://localhost:5173/oauth-success?token=%s&email=%s&name=%s&picture=%s",
	                token, email, name, picture
	        );

	        response.sendRedirect(redirectUrl);

		
	}

}
