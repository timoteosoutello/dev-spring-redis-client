package com.github.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.github.config.RedisConfig;
import com.github.model.User;

@Service
public class UserService {
	private List<User> users;

	@Autowired
	public UserService() {
		this.users = new ArrayList<>(0);
	}

	@Cacheable(cacheNames = RedisConfig.USER_CRUD_LIST, key = "#id")
	public List<User> getFromCacheById(String id) {
		return users.stream().filter(p -> p.getId().equals(id)).collect(Collectors.toList());
	}

	@CachePut(cacheNames = RedisConfig.USER_CRUD_LIST, key = "#id")
	public List<User> addToCache(String id) {
		User user = new User(id);
		users.add(user);
		return users;
	}

	@Cacheable(cacheNames = RedisConfig.USER_CRUD_LIST)
	public List<User> getListFromCache() {
		return null;
	}

	@CacheEvict(cacheNames = RedisConfig.USER_CRUD_LIST, key = "#id")
	public void cleanCache(String id) {
		users.removeIf(p -> p.getId().equals(id));
	}
}
