package com.sas.sasapi.repository;

import com.sas.sasapi.model.ODEvent;
import com.sas.sasapi.model.Session;
import com.sas.sasapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Component
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findBySessionId(Long sessionID);
    Optional<Session> deleteBySessionId(Long sessionID);



    @Query("select s from Session s where s.courseBatch.batch = ?4 and s.courseBatch.courseYear.year = ?2 and s.courseBatch.courseYear.semester = ?3 and s.courseBatch.courseYear.course.courseCode = ?1")
    List<Session> filterSessions(String courseCode, Long year, Long semester, String batch);

    @Query("select count(s) from SessionAttendance s where s.session.sessionId = ?1")
    Long getAttendedCount(Long sessionId);

    @Query("select count(s) from CourseAllocation s where s.courseBatch.courseBatchId = ?1")
    Long getAllCount(Long courseBatchId);

    @Query("select distinct year from CourseYear")
    List<Long> getUniqueAcademicYear();

    @Query("select distinct semester from CourseYear")
    List<Long> getUniqueSemester();

    @Query("select distinct batch from CourseBatch")
    List<String> getUniqueBatch();

    @Query("select distinct courseCode from Course")
    List<String> getCourseCode();

    @Query("select c.user from CourseAllocation c where c.courseBatch.batch = ?4 and c.courseBatch.courseYear.year = ?2 and c.courseBatch.courseYear.semester = ?3 and c.courseBatch.courseYear.course.courseCode = ?1")
    List<User> getCourseUsers(String courseCode, Long year, Long semester, String batch);

    @Query("select s.user from SessionAttendance s where s.session.fromDateTime = ?5 and s.session.toDateTime = ?6 and s.session.courseBatch.batch = ?4 and s.session.courseBatch.courseYear.year = ?2 and s.session.courseBatch.courseYear.semester = ?3 and s.session.courseBatch.courseYear.course.courseCode = ?1")
    List<User> getCourseAttendedUsers(String courseCode, Long year, Long semester, String batch, Date fromDateTime, Date toDateTime);

    @Query("select count(s) from Session s where s.sessionId=?1")
    Long getSessionCount(Long sessionId);








}