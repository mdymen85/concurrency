package com.demo.concurrency.exception;

public class TimeoutLockException extends RuntimeException {
	
	public TimeoutLockException() {
		super("Timeout when trying to lock...");
	}

}
