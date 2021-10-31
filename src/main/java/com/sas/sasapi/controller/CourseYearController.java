package com.sas.sasapi.controller;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.CourseYear;
import com.sas.sasapi.repository.CourseYearRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/course_year")
public class CourseYearController {
    private final CourseYearRepository courseYearRepository;
    public CourseYearController(CourseYearRepository courseYearRepository) {
        this.courseYearRepository = courseYearRepository;
    }

    @GetMapping("/all")

    public List<CourseYear> getAllCourseYears(){
        return courseYearRepository.findAll();
    }

    @PostMapping("/create")
    public CourseYear createCourseYear(@RequestBody CourseYear courseYear){

        return courseYearRepository.save(courseYear);
    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<CourseYear> updateCourseYear(@RequestBody CourseYear courseYear){
        CourseYear courseYearObj = courseYearRepository.findByCourseYearId(courseYear.getCourseYearId()).orElseThrow(() -> new ResourceNotFound("Cannot find courseYear in db"));

        courseYearObj.setCourseYearId(courseYear.getCourseYearId());
        courseYearObj.setCourse(courseYear.getCourse());
        courseYearObj.setSemester(courseYear.getSemester());
        courseYearObj.setYear(courseYear.getYear());


        CourseYear updatedCourseYear = courseYearRepository.save(courseYearObj);
        return new ResponseEntity<>(updatedCourseYear, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CourseYear> deleteCourseYear(@RequestBody CourseYear courseYear){

        CourseYear courseYearObj = courseYearRepository.findByCourseYearId(courseYear.getCourseYearId()).orElseThrow(() -> new ResourceNotFound("Cannot find courseYear in db"));
        courseYearRepository.delete(courseYearObj);
        return new ResponseEntity<>(courseYearObj,HttpStatus.OK);

    }
}
