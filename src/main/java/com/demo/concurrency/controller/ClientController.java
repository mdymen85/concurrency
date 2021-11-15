package com.demo.concurrency.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.concurrency.entity.Client;
import com.demo.concurrency.redis.RedisService;
import com.demo.concurrency.transaction.service.ClientService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ClientController {

	private final ClientService clientService;
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/api/v1/client/{account}")
	public Client getClient(@PathVariable String account) throws InterruptedException {
		
		log.info("Requested to load information of the account {} received", account);	
		
		return clientService.getClient(account);
	}
	
}
