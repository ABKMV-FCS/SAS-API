package com.sas.sasapi.repository;

import com.sas.sasapi.model.CourseAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseAllocationRepository extends JpaRepository<CourseAllocation, Long> {
}