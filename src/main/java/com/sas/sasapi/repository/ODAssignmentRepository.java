package com.sas.sasapi.repository;

import com.sas.sasapi.model.ODAssignment;
import com.sas.sasapi.model.ODEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ODAssignmentRepository extends JpaRepository<ODAssignment,String> {
    Optional<ODAssignment> findByOdAssignmentId(Long odAssignmentID);

    @Query("select oda.user.username from ODAssignment oda where oda.odEvent = ?1")
    List<String> getODStudentsList(ODEvent odEvent);

    @Query("delete from ODAssignment oda where oda.odEvent = ?1")
    @Modifying
    void deleteAllByODEvent(ODEvent odEvent);

    ODAssignment findByOdEvent_OdEventIdAndUser_Username(Long odEventId, String username);

    @Query("select o from ODAssignment o")
    List<ODAssignment> getAll();
}


