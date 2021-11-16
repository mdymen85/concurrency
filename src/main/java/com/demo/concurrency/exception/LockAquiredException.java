package com.demo.concurrency.exception;

public class LockAquiredException extends RuntimeException {

	public LockAquiredException() {
		super("Error in aquired lock...");
	}
	
}
