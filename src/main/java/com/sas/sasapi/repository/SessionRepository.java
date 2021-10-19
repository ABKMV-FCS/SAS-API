package com.sas.sasapi.repository;

import com.sas.sasapi.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}