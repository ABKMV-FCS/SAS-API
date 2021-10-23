package com.sas.sasapi.repository;

import com.sas.sasapi.model.Course;
import com.sas.sasapi.model.CourseBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseBatchRepository extends JpaRepository<CourseBatch, Long> {
    Optional<CourseBatch> findByCourseBatchId(Long courseBatchId);

}