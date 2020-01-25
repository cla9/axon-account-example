package com.cqrs.query.repository;

import com.cqrs.query.entity.HolderAccountSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.retry.annotation.Retryable;

import java.sql.SQLException;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<HolderAccountSummary,String> {
    Optional<HolderAccountSummary> findByHolderId(String holderId);
}
