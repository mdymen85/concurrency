package com.demo.concurrency.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.demo.concurrency.dto.TransactionDTO;
import com.demo.concurrency.entity.Client;
import com.demo.concurrency.entity.Transaction;
import com.demo.concurrency.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final ClientService clientService;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Transaction save(TransactionDTO transaction) throws Exception {

		Client client = this.clientService.getClient(transaction.getAccount());
		
		log.info("Transaction requested: {}", transaction);
		log.info("Balance before transaction: {}", client);
		
		
		client.setBalance(client.getBalance().add(transaction.getDeal()));
		
		log.info("Balance after transaction: {}", client);
		
		this.clientService.save(client);			
		
		final Transaction ended = this.transactionRepository.save(Transaction.builder()
				.account(transaction.getAccount())
				.deal(transaction.getDeal())
				.build());
		
		transaction.setCreated(ended.getCreated());
		
		return ended;
	}
	
	
}
