package com.sas.sasapi.repository;

import com.sas.sasapi.model.Course;
import com.sas.sasapi.model.ODEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>{
    Optional<Course> findByCourseId(Long courseID);

    Course findByCourseCode(String courseCode);

    @Query("select count(sa) from SessionAttendance sa where sa.session in (select s from Session s where s.courseBatch in (select cy from CourseYear cy where cy.course = ?1 ))")
    Integer findCourseAttendance(Course course);



}