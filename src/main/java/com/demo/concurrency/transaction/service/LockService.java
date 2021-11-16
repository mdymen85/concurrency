package com.demo.concurrency.transaction.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.demo.concurrency.lock.LockManager;
import com.demo.concurrency.lock.RetryLock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class LockService {

	private final RedisTemplate<String, String> redisTemplate;
	private final RetryLock retryLock;
	
	public LockManager lock(String account) {
		log.info("Trying to lock account {}", account);
		var lockManager = new LockManager(redisTemplate, account, retryLock);
		lockManager.lock();
		return lockManager;
	}
	
	public void unlock(LockManager lockManager) {
		log.info("Trying to unlock account {}", lockManager.getAccount());
		lockManager.unlock();
	}

	public void unsafeUnlock(String account) {
		new LockManager(redisTemplate, account, retryLock).unsafeUnlock();
		
	}
	
}
