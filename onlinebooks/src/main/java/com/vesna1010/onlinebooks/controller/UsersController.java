package com.vesna1010.onlinebooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vesna1010.onlinebooks.model.User;
import com.vesna1010.onlinebooks.service.UserService;

@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UserService service;

	@GetMapping
	public Page<User> findUsersByPage(
			@PageableDefault(page = 0, size = 10, sort = "username", direction = Direction.ASC) Pageable pageable) {
		return service.findUsersByPage(pageable);
	}

	@GetMapping("/new")
	public User user() {
		return new User();
	}

	@PostMapping
	public String saveUser(@RequestBody User user) {
		return "Saved " + service.saveUser(user);
	}

	@DeleteMapping("/{username}")
	public void deleteUserByUsername(@PathVariable String username) {
		service.deleteUserByUsername(username);
	}

	@PutMapping("/{username}")
	public void disableUserByUsername(@PathVariable String username) {
		service.disableUserByUsername(username);
	}

}