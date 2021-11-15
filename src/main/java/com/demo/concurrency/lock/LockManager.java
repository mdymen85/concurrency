package com.demo.concurrency.lock;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LockManager implements Lock, Closeable {
	
	@Value("${application.lockmanager.release-timeout:5000}")
	private Long releaseTimeout;

	private RedisTemplate<String,String> redisTemplate;
	private String account;
	private Long actualMillis = System.currentTimeMillis();
	
	public LockManager(RedisTemplate<String,String> redisTemplate, String account) {
		this.redisTemplate = redisTemplate;
		this.account = account;
	}
	
	@Override
	public void lock() {
		
		log.info("Aquired lock {}", account);
		
		if (!redisTemplate.hasKey(account)) {
			redisTemplate.opsForValue().set(account, account);
			log.info("Account {} locked.", account);
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!redisTemplate.hasKey(account)) {
					return;
				}
				
				if (System.currentTimeMillis() > actualMillis + 5000) {
					log.info("Lock account {} timeout!!!", account);
					this.unlock();
					return;
				}
			}			
		}
		
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tryLock(long arg0, TimeUnit arg1) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void unlock() {
		log.info("Releasing account lock {}", account);
		this.redisTemplate.delete(account);		
	}

	@Override
	public void close() throws IOException {
		this.unlock();
		
	}

}
