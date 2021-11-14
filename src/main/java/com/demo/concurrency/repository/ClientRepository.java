package com.demo.concurrency.repository;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.concurrency.entity.Client;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select c from Client c where c.account = :account")
	Optional<Client> findByAccountPessimisticWrite(@Param("account") String account);
	
}
