package com.demo.concurrency.service;

import org.springframework.stereotype.Service;

import com.demo.concurrency.entity.Client;
import com.demo.concurrency.repository.ClientRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {
	
	private final ClientRepository clientRepository; 
	
	public Client getClient(String account) throws Exception {
		
		log.info("Trying to load {} account", account);
		
		return clientRepository.findByAccountPessimisticWrite(account)
				.orElseThrow(() -> new Exception());
	}
	
	public Client save(Client client) {
		
		log.info("Saving... {}", client);
		
		return this.clientRepository.save(client);
	}
	
}
