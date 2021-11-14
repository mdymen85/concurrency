package com.demo.concurrency.entity;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "client")
@NoArgsConstructor
@AllArgsConstructor
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID", nullable = false, unique = true)
	private Long id;
	
	@Column(name="ACCOUNT", nullable = false, unique = true)
	private String account;
	
	@Column(name="NAME", nullable = false)
	private String name;
	
	@Column(name="BALANCE", nullable = false)
	private BigDecimal balance;
	
	@Column(name="CREATED", nullable = false)
	@CreationTimestamp
	private Instant created;
	
}
