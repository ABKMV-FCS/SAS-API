package com.sas.sasapi.repository;

import com.sas.sasapi.model.ODSession;
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

}
