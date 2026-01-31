package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.models.User;
import com.example.demo.repos.UserRepo;

@Service
public class UserService {
  
  @Autowired
  UserRepo userRepo;
  
  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;
  
  public User registerUser(User user) {
	  try {
		  user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		   return userRepo.save(user); 
	  } catch (DataIntegrityViolationException e) {
	        System.out.println("Error: userName already exists.");
	  }
	  return null;
  }
  public List<User> getAllUsers() {
	  return userRepo.findAll();
  }
}
