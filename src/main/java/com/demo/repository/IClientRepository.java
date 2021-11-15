package com.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.demo.concurrency.entity.Client;

public interface IClientRepository extends CrudRepository<Client, Long> {

	Optional<Client> findByAccount(@Param("account") String account);
	
}
