package com.demo.concurrency.transaction.validation;

import org.springframework.stereotype.Service;

import com.demo.concurrency.entity.Client;
import com.demo.concurrency.entity.Transaction;
import com.demo.concurrency.exception.NotEnoughBalanceException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class Balance implements IValidator {
	
	@Override
	public void validator(Client client, Transaction transaction) {
		
		log.info("Checking if the account {} has enough balance {} for the transaction {}.", client.getAccount(), client.getBalance(), transaction.getDeal());
		
		if (!transaction.isEnoughBalance(client.getBalance())) {
			throw new NotEnoughBalanceException();
		}
		
	}

}
