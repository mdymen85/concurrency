package com.demo.concurrency.transaction.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.demo.concurrency.lock.LockManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class LockService {

	private final RedisTemplate<String, String> redisTemplate;
	
	public void lock(String account) {
		log.info("Trying to lock account {}", account);
		new LockManager(redisTemplate, account).lock();
	}
	
	public void unlock(String account) {
		log.info("Trying to unlock account {}", account);
		new LockManager(redisTemplate, account).unlock();
	}
	
}
