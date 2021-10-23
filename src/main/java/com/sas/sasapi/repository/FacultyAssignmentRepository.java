package com.sas.sasapi.repository;

import com.sas.sasapi.model.FacultyAssignment;
import com.sas.sasapi.model.ODAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultyAssignmentRepository extends JpaRepository<FacultyAssignment, Long> {
    Optional<FacultyAssignment> findByFacultyAssignmentId(Long facultyAssignmentID);

}