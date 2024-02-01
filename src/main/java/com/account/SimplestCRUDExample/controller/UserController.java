package com.account.SimplestCRUDExample.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.SimplestCRUDExample.model.User;
import com.account.SimplestCRUDExample.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	// @PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get")
	// all users
	public List<User> getAllUsers() {
		return this.userRepository.findAll();
	}

	@GetMapping("/{id}")
	public Optional<User> getUserById(@PathVariable("id") Long id) {
		return this.userRepository.findById(id);
	}

	@PostMapping("/add")
	public User add(@RequestBody User user) {
		return this.userRepository.save(user);
	}

}
