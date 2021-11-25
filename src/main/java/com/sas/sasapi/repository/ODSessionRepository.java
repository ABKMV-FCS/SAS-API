package com.sas.sasapi.repository;

import com.sas.sasapi.model.ODAssignment;
import com.sas.sasapi.model.ODSession;
import com.sas.sasapi.payload.response.GetStudentODListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ODSessionRepository extends JpaRepository<ODSession,String> {
    Optional<ODSession> findByOdSessionId(Long ODSessionId);
    @Modifying
    @Transactional
    @Query("DELETE FROM ODSession WHERE session.sessionId=?1")
    void deleteODSessionBySessionId(Long sessionId);

    @Query("select o.session.sessionId from ODSession o where o.odAssignment.user.username like ?1 and o.session.courseBatch.courseYear.course.courseCode like ?2 and o.status like 'approved'")
    HashSet<Long> findApprovedODSessionIDs(String username, String courseCode);

    @Query("select o from ODSession o where o.odAssignment.user.username like ?1")
    List<ODSession> findByUsernameGetOdSessionApproval(String username);

    @Query("select o.odAssignment.odEvent.eventName,o.odAssignment.odEvent.description,o.odAssignment.odEvent.fromDate,o.odAssignment.odEvent.toDate,o.odAssignment.odEvent.user.name,o.status from ODSession o where o.odAssignment.user.username like ?1")
    List<Object> getStudentODList(String username);

    List<ODSession> findByOdAssignment(ODAssignment odAssignment);

    @Query("select o from ODAssignment o where o.user.username = ?1")
    List<ODAssignment> getODsAssignedToUsername(String username);

    @Query("select f.courseBatch.courseBatchId from FacultyAssignment f where f.user.username = ?1")
    List<Long> getCoursebatchIdsByFaculty(String username);

    @Query("select o from ODSession o where o.session.courseBatch.courseBatchId = ?1")
    List<ODSession> getODSessionsByCourseBatchID(Long courseBatchId);
}
