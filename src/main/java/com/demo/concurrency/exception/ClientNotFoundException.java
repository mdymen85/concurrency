package com.demo.concurrency.exception;

public class ClientNotFoundException extends RuntimeException {

	public ClientNotFoundException() {
		super("Client not found");
	}
	
}
