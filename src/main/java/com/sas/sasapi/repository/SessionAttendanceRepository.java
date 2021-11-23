package com.sas.sasapi.repository;

import com.sas.sasapi.model.ODEvent;
import com.sas.sasapi.model.Session;
import com.sas.sasapi.model.SessionAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface SessionAttendanceRepository extends JpaRepository<SessionAttendance, Long> {
    Optional<SessionAttendance> findBySessionAttendanceId(Long eventID);
    @Modifying
    @Transactional
    @Query("DELETE FROM SessionAttendance WHERE session.sessionId=?1")
    void deleteSessionAttendanceBySessionId(Long sessionId);

    @Query("select distinct c.courseBatch.courseYear.course.courseCode from CourseAllocation c where c.user.username=?1")
    List<String> getCourseCodesByUsername(String username);

    @Query("select s.session.sessionId from SessionAttendance s where s.session.courseBatch.courseYear.course.courseCode = ?1 and s.user.username=?2")
    HashSet<Long> getCourseCodeAttendedCountByCourseCodeAndUsername(String courseCode, String username);

    @Query("select s.session from SessionAttendance s where s.session.courseBatch.courseYear.course.courseCode = ?1 and s.user.username=?2")
    HashSet<Session> getCourseCodeAttendedSessionsByCourseCodeAndUsername(String courseCode, String username);

    @Query("select s.session from SessionAttendance s where s.session.courseBatch.courseYear.course.courseCode = ?1 and s.user.username <> ?2")
    HashSet<Session> getCourseCodeNotAttendedCountByCourseCodeAndUsername(String courseCode, String username);
}