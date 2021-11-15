package com.demo.concurrency.exception;

public class NotEnoughBalanceException extends RuntimeException{

	public NotEnoughBalanceException() {
		super("The client does not have enough balance to do the operation.");
	}
	
}
