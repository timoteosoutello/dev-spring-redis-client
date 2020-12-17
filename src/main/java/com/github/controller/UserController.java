package com.github.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.config.RedisConfig;
import com.github.model.User;
import com.github.repository.UserRepository;
import com.querydsl.core.types.Predicate;

@RestController
@RequestMapping("/v1/users")
public class UserController {
	private UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/pageable")
	public Page<User> query(@SortDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@GetMapping("/querydsl")
	public Page<User> query(@QuerydslPredicate(root = User.class) Predicate predicate,
			@SortDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
		return userRepository.findAll(predicate, pageable);
	}

	@GetMapping("/cache")
	public ResponseEntity<User> queryCache(@QuerydslPredicate(root = User.class) Predicate predicate,
			@SortDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
		// populateCache();
		ResponseEntity<User> response = new ResponseEntity<>(getFromCache("1"), HttpStatus.OK);
		return response;
	}

	@Cacheable(cacheNames = RedisConfig.USER_CONTROLLER, key = "#id")
	public User getFromCache(String id) {
		User user = new User();
		user.setId(id);
		return user;
	}

	@CachePut(cacheNames = RedisConfig.USER_CONTROLLER)
	public User populateCache() {
		User user = new User();
		user.setId("1");
		return user;
	}

	@Cacheable(cacheNames = RedisConfig.USER_CONTROLLER)
	private User calculateCache(String id) {
		List<User> list = new ArrayList<>();
		User user = new User();
		user.setId(id);
		list.add(user);
		User user2 = new User();
		user2.setId("2");
		list.add(user2);
		return user;
	}
}
