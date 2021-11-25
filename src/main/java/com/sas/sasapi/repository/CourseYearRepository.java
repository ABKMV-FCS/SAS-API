package com.sas.sasapi.repository;

import com.sas.sasapi.model.Course;
import com.sas.sasapi.model.CourseYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseYearRepository extends JpaRepository<CourseYear, Long> {
    Optional<CourseYear> findByCourseYearId(Long courseYearId);
    CourseYear findByCourseAndSemesterAndYear(Course course, Long semester, Long year);
    CourseYear findBySemesterAndYear(Long semester,Long year);
}