package com.demo.concurrency.transaction.validation;

import com.demo.concurrency.entity.Client;
import com.demo.concurrency.entity.Transaction;

public interface IValidator {

	void validator(Client client, Transaction transaction);
	
}
