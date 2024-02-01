package com.account.SimplestCRUDExample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.SimplestCRUDExample.repository.UserRepository;

@RestController
@RequestMapping("/transactions")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;

}
