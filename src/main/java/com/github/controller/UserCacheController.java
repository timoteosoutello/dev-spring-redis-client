package com.github.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.model.User;
import com.github.service.UserService;

@RestController
@RequestMapping("/v1/users/cache")
public class UserCacheController {
	private UserService userService;

	@Autowired
	public UserCacheController(final UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<List<User>> post(@RequestHeader("id") String userId) {
		userService.addToCache(userId);
		return new ResponseEntity<>(userService.getListFromCache(), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<User>> get(@RequestHeader("id") String userId) {
		Optional<String> optional = Optional.ofNullable(userId);
		if (optional.isPresent() && !userId.equals("0")) {
			return new ResponseEntity<>(userService.getFromCacheById(userId), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(userService.getListFromCache(), HttpStatus.OK);
		}
	}

	@DeleteMapping
	public ResponseEntity<List<User>> delete(@RequestHeader("id") String userId) {
		userService.cleanCache(userId);
		return new ResponseEntity<>(userService.getListFromCache(), HttpStatus.OK);
	}

}
