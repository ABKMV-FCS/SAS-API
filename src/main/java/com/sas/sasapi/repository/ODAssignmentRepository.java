package com.sas.sasapi.repository;

import com.sas.sasapi.model.ODAssignment;
import com.sas.sasapi.model.ODEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ODAssignmentRepository extends JpaRepository<ODAssignment,String> {
    Optional<ODAssignment> findByOdAssignmentId(Long odAssignmentID);

}
