package com.sas.sasapi.repository;

import com.sas.sasapi.model.ODSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ODSessionRepository extends JpaRepository<ODSession,String> {
    Optional<ODSession> findByOdSessionId(Long ODSessionId);

    @Query("select o from ODSession o where o.odAssignment.user.username like ?1")
    List<ODSession> findByUsernameGetOdSessionApproval(String username);

}
