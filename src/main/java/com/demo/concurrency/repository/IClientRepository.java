package com.demo.concurrency.repository;

import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.demo.concurrency.entity.Client;

@Primary
public interface IClientRepository extends CrudRepository<Client, Long> {

	Optional<Client> findByAccount(@Param("account") String account);
	
}
