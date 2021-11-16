package com.demo.concurrency.transaction.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.demo.concurrency.dto.TransactionDTO;
import com.demo.concurrency.entity.Client;
import com.demo.concurrency.entity.Transaction;
import com.demo.concurrency.lock.LockManager;
import com.demo.concurrency.repository.TransactionRepository;
import com.demo.concurrency.transaction.validation.IValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final ClientService clientService;
	private final List<IValidator> validators;
	private final LockService lockService;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(TransactionDTO transaction) {
		
		var lockManager = lockService.lock(transaction.getAccount());
		
		try {
			
			Client client = this.clientService.getClient(transaction.getAccount());
			
			log.info("Transaction requested: {}", transaction);
			log.info("Balance before transaction: {}", client);
	
			final Transaction ended = Transaction.builder()
				.account(transaction.getAccount())
				.deal(transaction.getDeal())
				.transactionId(UUID.randomUUID().toString())
				.type(transaction.getType())
				.build();
			
			this.executeValidators(client, ended);	
			
			client.setBalance(client.getBalance().add(ended.getSignedDeal()));
			
			log.info("Balance after transaction: {}", client);
			
			this.clientService.save(client);			
			
			this.transactionRepository.save(ended);
			
			transaction.setCreated(ended.getCreated());
			
		}
		finally {
			lockService.unlock(lockManager);			
		}	

	}
	
	private void executeValidators(Client client, Transaction transaction) {
		this.validators.forEach(v -> v.validator(client, transaction));
	}
	
	
}
