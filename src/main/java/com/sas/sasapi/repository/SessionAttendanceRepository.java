package com.sas.sasapi.repository;

import com.sas.sasapi.model.ODEvent;
import com.sas.sasapi.model.SessionAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SessionAttendanceRepository extends JpaRepository<SessionAttendance, Long> {
    Optional<SessionAttendance> findBySessionAttendanceId(Long eventID);
    @Modifying
    @Transactional
    @Query("DELETE FROM SessionAttendance WHERE session.sessionId=?1")
    void deleteSessionAttendanceBySessionId(Long sessionId);

}