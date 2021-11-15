package com.demo.concurrency.entity;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
@Entity
@Table(name = "transaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID", nullable = false, unique = true)
	private Long id;
	
	@Column(name="ACCOUNT", nullable = false, unique = true)
	private String account;
	
	@Column(name="TRANSACTION_ID", nullable = false)
	private String transactionId;
	
	@Column(name="TRANSACTION_TYPE", nullable = false)	
	@Enumerated(EnumType.STRING)
	private TransactionType type;
	
	@Column(name="DEAL", nullable = false)
	private BigDecimal deal;
	
	@Column(name="CREATED", nullable = false)
	@CreationTimestamp
	private Instant created;
	
	public enum TransactionType {
		CREDIT, DEBIT
	}
	
	public boolean isEnoughBalance(BigDecimal clientBalance) {		
		return isDebit() ? 
				this.deal.compareTo(clientBalance) >= 0 ? 
						false : true 
			    : true;
	}
	
	private boolean isDebit() {
		return TransactionType.DEBIT.equals(type);
	}

	public BigDecimal getSignedDeal() {
		return TransactionType.DEBIT.equals(type)? this.deal.negate() : this.deal;
	}
	
}
