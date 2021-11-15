package com.demo.concurrency.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.concurrency.transaction.service.LockService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LockManagerController {
	
	private final LockService lockService;

	@RequestMapping(method = RequestMethod.DELETE, path = "/api/v1/lock/{account}")
	public void lock(@PathVariable String account) {
		log.info("Unlocking account {}.", account);
		lockService.unlock(account);
	}
}
