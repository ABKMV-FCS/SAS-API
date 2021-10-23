package com.sas.sasapi.repository;

import com.sas.sasapi.model.ODEvent;
import com.sas.sasapi.model.SessionAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionAttendanceRepository extends JpaRepository<SessionAttendance, Long> {
    Optional<SessionAttendance> findBySessionAttendanceId(Long eventID);

}