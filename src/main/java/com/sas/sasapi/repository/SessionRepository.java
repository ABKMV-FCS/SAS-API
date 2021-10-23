package com.sas.sasapi.repository;

import com.sas.sasapi.model.ODEvent;
import com.sas.sasapi.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findBySessionId(Long sessionID);

}