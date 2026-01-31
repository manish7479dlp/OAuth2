package com.example.demo.controllers;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.models.User;
import com.example.demo.repos.UserRepo;
import com.example.demo.services.JwtService;
import com.example.demo.services.UserDetailsPrincipal;
import com.example.demo.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@GetMapping("/test")
	public ResponseEntity<String> test(Authentication authentication) {

	    UserDetailsPrincipal principal = (UserDetailsPrincipal) authentication.getPrincipal();

	    return ResponseEntity.ok("Logged in user: " + principal.getUsername());
	}


	@GetMapping
	public ResponseEntity<List<User>> user() {
	    return new ResponseEntity<List<User>>(userService.getAllUsers(), HttpStatus.OK);
	}

	@PostMapping("register")
	public ResponseEntity<User> register(@RequestBody User user) {
		System.out.println(user.toString());
	    user =  userService.registerUser(user);
	    System.out.println(user.toString());
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}

	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody User user){
		
		
		
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));

		if(authentication.isAuthenticated()) {
			String token = jwtService.generateToken(user.getUserName());
			
			return new ResponseEntity(Map.of("token", token), HttpStatus.OK);
			
		}
		else
			return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);

	}
	




}
