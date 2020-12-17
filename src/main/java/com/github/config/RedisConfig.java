package com.github.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@EnableCaching
public class RedisConfig {
	public static final String USER_CONTROLLER = "com.github.controller.UserController";

	@ConditionalOnProperty(name = "spring.redis.enabled", havingValue = "true")
	@Bean
	RedissonClient redisson(@Value("classpath:/redisson.yaml") final Resource configFile) throws IOException {
		final Config config = Config.fromYAML(configFile.getInputStream());
		return Redisson.create(config);
	}

	@ConditionalOnBean(RedissonClient.class)
	@Bean
	CacheManager cacheManager(final RedissonClient redissonClient) {
		final Map<String, CacheConfig> config = new HashMap<>();
		final CacheConfig cacheExpiration = new CacheConfig(TimeUnit.MINUTES.toMillis(60),
				TimeUnit.MINUTES.toMillis(30));
		config.put(USER_CONTROLLER, cacheExpiration);
		return new RedissonSpringCacheManager(redissonClient, config);
	}
}
