package com.sas.sasapi.repository;

import com.sas.sasapi.model.CourseAllocation;
import com.sas.sasapi.model.CourseBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseAllocationRepository extends JpaRepository<CourseAllocation, Long> {
    Optional<CourseAllocation> findByCourseAllocationId(Long courseAllocationId);
}