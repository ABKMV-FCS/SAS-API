package com.sas.sasapi.repository;

import com.sas.sasapi.model.Course;
import com.sas.sasapi.model.CourseBatch;
import com.sas.sasapi.model.CourseYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CourseBatchRepository extends JpaRepository<CourseBatch, Long> {
    Optional<CourseBatch> findByCourseBatchId(Long courseBatchId);
    @Query("select c from CourseBatch c where c.batch = ?4 and c.courseYear.semester=?3 and c.courseYear.year=?2 and c.courseYear.course.courseCode=?1")
    CourseBatch findByCourseCodeAndYearAndSemesterAndBatch(String courseCode,Long year,Long semester,String batch);

    CourseBatch findByCourseYearAndBatch(CourseYear courseYear, String batch);


}