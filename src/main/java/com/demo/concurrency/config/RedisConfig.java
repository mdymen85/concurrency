package com.demo.concurrency.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.retry.annotation.EnableRetry;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableRetry
@Slf4j
public class RedisConfig {

	@Value("${application.lockmanager.port}")
	private Integer port;
	
	@Value("${application.lockmanager.host}")
	private String serverHost;
	
	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(new RedisStandaloneConfiguration(serverHost, port));
	}
	
	@Bean
	public RedissonClient redisson() {		
		Config config = new Config();		
		config.useSingleServer().setAddress(this.getUrlRedis());
		return Redisson.create(config);
	}
	
	private String getUrlRedis() {
		StringBuilder sb = new StringBuilder("redis");
		sb.append(":");
		sb.append("//");
		sb.append(serverHost);
		sb.append(":");
		sb.append(port);
		
		log.info("URI Redis: {}.", sb.toString());
		
		return sb.toString();
	}
	
}
