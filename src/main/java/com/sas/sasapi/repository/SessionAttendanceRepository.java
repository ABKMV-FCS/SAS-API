package com.sas.sasapi.repository;

import com.sas.sasapi.model.SessionAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionAttendanceRepository extends JpaRepository<SessionAttendance, Long> {
}