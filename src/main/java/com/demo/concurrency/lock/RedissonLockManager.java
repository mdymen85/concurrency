package com.demo.concurrency.lock;

import java.util.function.Consumer;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonLockManager {

	private final RedissonClient redissonClient;
	
	public <T> void lockAndProcess(Consumer<T> method, T object, String account) {				
		
		log.info("Trying to acquired lock for account {}.", account);
		
		var lock = redissonClient.getFairLock(account);				
		
		lock.lock();
		
		log.info("Lock for account {} acquired.", account);
		
		method.accept(object);
		
		lock.unlock();		
		
		log.info("Unlocking account {}.", account);
		
	}
	
	
}
