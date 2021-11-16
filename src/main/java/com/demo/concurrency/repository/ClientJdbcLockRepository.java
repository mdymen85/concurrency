package com.demo.concurrency.repository;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.concurrency.entity.Client;

@Repository
@ConditionalOnProperty(name = "application.lockmanager.type", havingValue = "jdbc", matchIfMissing = false)
public interface ClientJdbcLockRepository extends IClientRepository {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Override
	Optional<Client> findByAccount(@Param("account") String account);
	
}
