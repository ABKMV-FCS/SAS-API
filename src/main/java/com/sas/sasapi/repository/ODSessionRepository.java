package com.sas.sasapi.repository;

import com.sas.sasapi.model.ODSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ODSessionRepository extends JpaRepository<ODSession,String> {
    Optional<ODSession> findByOdSessionId(Long ODSessionId);
}
