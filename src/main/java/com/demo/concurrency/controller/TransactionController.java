package com.demo.concurrency.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.demo.concurrency.dto.TransactionDTO;
import com.demo.concurrency.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;
	
	@RequestMapping(path = "/api/v1/transaction", method = RequestMethod.POST)
	@ResponseStatus(code = HttpStatus.CREATED)
	public TransactionDTO insert(@RequestBody TransactionDTO transaction) throws Exception {
		transactionService.save(transaction);		
		return transaction;
	}
	
}
