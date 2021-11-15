package com.demo.concurrency.redis;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisService implements Lock {

	private final RedisTemplate<String,String> redisTemplate;
	//private final Lock lock;
	
	@Resource(name="redisTemplate")
	private ListOperations<String, String> listOps;
	
	
	
	private static ThreadLocal<String> threadLocalValue = new ThreadLocal<String>();
	
	public void add(String account) throws InterruptedException {	
		
		String tAccount = threadLocalValue.get();
		
		threadLocalValue.set(account);
		
		boolean hasKey = redisTemplate.hasKey(account);
		
		if (!hasKey) {
			redisTemplate.opsForValue().set(account, account);			
		} else {
			lock(account);
		}
		
		//redisTemplate.opsForValue().setIfAbsent(account, account, 1, TimeUnit.SECONDS);
//		boolean hasKey = redisTemplate.hasKey(account);
//		
//		if (hasKey) {
//			
//	//		boolean loock = redisTemplate.opsForValue().setIfAbsent("account", account);
//			
//		//	listOps.leftPush("account", account);
//			
//			//loock = redisTemplate.opsForValue().setIfAbsent("account", account);
//			
//						
//		//	lock.tryLock(120, TimeUnit.MINUTES);	
//	
//			
//		} 
//		lock.lock();		
		System.out.println("NO LOCKOU");
		
	}
	
	private void lock(String account) {
		while (true) {
			if (!redisTemplate.hasKey(account)) {
				return;
			}
		}
	}

	@Override
	public void lock() {

		
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
		// TODO Auto-generated method stub
		
	}

	public void unlock(String account) {
		this.redisTemplate.delete(account);
		
	}
}
