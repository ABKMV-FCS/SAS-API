package com.sas.sasapi.repository;

import com.sas.sasapi.model.Course;
import com.sas.sasapi.model.ODEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>{
    Optional<Course> findByCourseId(Long courseID);
}