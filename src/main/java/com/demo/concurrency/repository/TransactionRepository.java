package com.demo.concurrency.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.concurrency.entity.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long>{

}
