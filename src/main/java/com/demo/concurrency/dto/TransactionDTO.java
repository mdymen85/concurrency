package com.demo.concurrency.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.demo.concurrency.entity.Transaction.TransactionType;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class TransactionDTO {

	private String account;	
	private TransactionType type;
	private BigDecimal deal;
	private Instant created;
	
}
