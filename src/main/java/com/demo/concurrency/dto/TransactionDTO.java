package com.demo.concurrency.dto;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class TransactionDTO {

	private String account;	
	private BigDecimal deal;
	private Instant created;
	
}
