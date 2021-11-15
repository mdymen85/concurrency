package com.demo.concurrency.transaction.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.concurrency.entity.Client;
import com.demo.concurrency.exception.ClientNotFoundException;
import com.demo.concurrency.repository.ClientJdbcLockRepository;
import com.demo.repository.IClientRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ClientService {
	
	private final IClientRepository clientRepository; 
	
	public Client getClient(String account) {
		
		log.info("Trying to load {} account", account);
		
		return clientRepository.findByAccount(account)
				.orElseThrow(() -> new ClientNotFoundException());
	}
	
	public Client save(Client client) {
		
		log.info("Saving... {}", client);
		
		return this.clientRepository.save(client);
	}
	
}
