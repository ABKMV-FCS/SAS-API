package com.sas.sasapi.repository;

import com.sas.sasapi.model.CourseAllocation;
import com.sas.sasapi.model.CourseBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseAllocationRepository extends JpaRepository<CourseAllocation, Long> {
    Optional<CourseAllocation> findByCourseAllocationId(Long courseAllocationId);
    List<CourseAllocation> findByCourseBatch_CourseBatchId(Long courseBatchId);

    @Query("select c from CourseAllocation c where c.courseBatch.courseBatchId=?1 and c.user.username=?2")
    CourseAllocation findByCourseBatch_CourseBatchIdAndUser_Name(Long CourseBatchId,String username);

    @Query("select c from CourseAllocation c")
    List<CourseAllocation> getAll();
}