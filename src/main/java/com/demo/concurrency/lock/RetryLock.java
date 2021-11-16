package com.demo.concurrency.lock;

import java.util.Random;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.demo.concurrency.exception.LockAquiredException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RetryLock {

	@Retryable(value = LockAquiredException.class , maxAttemptsExpression = "${retry.maxAttemps:15}"
			, backoff = @Backoff(delayExpression = "${retry.maxDelay:1000}"))
	public boolean tryLock(RedisTemplate<String, String> redisTemplate, String account) {
		Random r = new Random();
		int v = r.nextInt(2000);
		log.info("Random number {}", v);
		try {
			Thread.sleep((long)v);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("Trying to acquire lock account {}.", account);
		boolean hasKey = redisTemplate.hasKey(account);
		if (hasKey) {
			log.info("Not possible to acquired lock account {}.", account);
			throw new LockAquiredException();
		}
		return true;
	}
	
	
}
