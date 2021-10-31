package com.sas.sasapi.controller;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.Course;
import com.sas.sasapi.repository.CourseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/course")
public class CourseController {
    private final CourseRepository courseRepository;
    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/all")
    public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }

    @PostMapping("/create")
    public Course createCourse(@RequestBody Course course){

        return courseRepository.save(course);
    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<Course> updateCourse(@RequestBody Course course){
        Course courseObj = courseRepository.findByCourseId(course.getCourseId()).orElseThrow(() -> new ResourceNotFound("Cannot find course in db"));

        courseObj.setCourseId(course.getCourseId());
        courseObj.setDescription(course.getDescription());
        courseObj.setCourseCode(course.getCourseCode());

        Course updatedCourse = courseRepository.save(courseObj);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    @Transactional

    @DeleteMapping("/delete")
    public ResponseEntity<Course> deleteCourse(@RequestBody Course course){

        Course courseObj = courseRepository.findByCourseId(course.getCourseId()).orElseThrow(() -> new ResourceNotFound("Cannot find course in db"));
        courseRepository.delete(courseObj);
        return new ResponseEntity<>(courseObj,HttpStatus.OK);

    }
}
