package com.sas.sasapi.controller;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.CourseAllocation;
import com.sas.sasapi.repository.CourseAllocationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/course_allocation")
public class CourseAllocationController {
    private final CourseAllocationRepository courseAllocationRepository;

    public CourseAllocationController(CourseAllocationRepository courseAllocationRepository) {
        this.courseAllocationRepository = courseAllocationRepository;
    }

    @GetMapping("/all")

    public List<CourseAllocation> getAllCoursesAllocated(){
        return courseAllocationRepository.findAll();
    }

    @PostMapping("/create")
    public CourseAllocation createCourseAllocation(@RequestBody CourseAllocation courseAllocation){

        return courseAllocationRepository.save(courseAllocation);
    }

    @PutMapping("/update")
    public ResponseEntity<CourseAllocation> updateCourseYear(@RequestBody CourseAllocation courseAllocation){
        CourseAllocation courseAllocationObj = courseAllocationRepository.findByCourseAllocationId(courseAllocation.getCourseAllocationId()).orElseThrow(() -> new ResourceNotFound("Cannot find courseAllocation in db"));

        courseAllocationObj.setCourseBatch(courseAllocation.getCourseBatch());
        courseAllocationObj.setCourseAllocationId(courseAllocation.getCourseAllocationId());
        courseAllocationObj.setUser(courseAllocation.getUser());


        CourseAllocation updatedCourseAllocation = courseAllocationRepository.save(courseAllocationObj);
        return new ResponseEntity<>(updatedCourseAllocation, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CourseAllocation> deleteCourseYear(@RequestBody CourseAllocation courseAllocation){

        CourseAllocation courseAllocationObj = courseAllocationRepository.findByCourseAllocationId(courseAllocation.getCourseAllocationId()).orElseThrow(() -> new ResourceNotFound("Cannot find courseAllocation in db"));
        courseAllocationRepository.delete(courseAllocationObj);
        return new ResponseEntity<>(courseAllocationObj,HttpStatus.OK);

    }
}
