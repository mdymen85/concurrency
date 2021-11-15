package com.demo.concurrency.repository;

import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.query.Param;

import com.demo.concurrency.entity.Client;
import com.demo.repository.IClientRepository;

@ConditionalOnProperty(name = "application.lockmanager.type", havingValue = "redis", matchIfMissing = true)
public interface ClientRedisLockRepository extends IClientRepository  {

	@Override
	Optional<Client> findByAccount(@Param("account") String account);
}
