package com.demo.concurrency.lock;

import java.io.Closeable;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import com.demo.concurrency.exception.TimeoutLockException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Deprecated
public class LockManager implements Lock, Closeable {
	
	@Value("${application.lockmanager.release-timeout:5000}")
	private Long releaseTimeout;

	private RedisTemplate<String,String> redisTemplate;
	private String account;
	private RetryLock retryLock;
	private String lockId;
	
	public LockManager(RedisTemplate<String,String> redisTemplate, String account, RetryLock retryLock) {
		this.redisTemplate = redisTemplate;
		this.account = account;
		this.retryLock = retryLock;
		this.lockId = UUID.randomUUID().toString();
		log.info("Lock id {}.", lockId);
	}	
	
	@Override
	public void lock() {
		
		log.info("Trying to lock account {}", account);
		
		Boolean isLocked = redisTemplate.hasKey(account);
		
		log.info("Account {} is locked? {}", account, isLocked);
		
		if (isLocked) {
			isLocked = this.tryLock();
			
			if (isLocked) {
				log.info("Locking account {} successfully...", account);
				this.locking();
			}
			return;			
		}

		this.locking();
	}
	
	private void locking() {
		redisTemplate.delete(account);
		redisTemplate.opsForValue().setIfAbsent(account, lockId, 10, TimeUnit.SECONDS);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean tryLock() {	    
		return retryLock.tryLock(redisTemplate, account);				
	}

	@Override
	public boolean tryLock(long arg0, TimeUnit arg1) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void unlock() {
		log.info("Releasing account lock {}", account);
		String candidateId = redisTemplate.opsForValue().get(account);
		if (!lockId.equals(candidateId)) {
			throw new RuntimeException(); 
		}
		this.redisTemplate.delete(account);		
	}
	
	public void unsafeUnlock() {
		this.redisTemplate.delete(account);
	}

	@Override
	public void close() throws IOException {
		this.unlock();
		
	}
	
	public String getAccount() {
		return this.account;
	}

}
