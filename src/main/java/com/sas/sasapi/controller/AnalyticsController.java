package com.sas.sasapi.controller;

import com.amazonaws.Response;
import com.google.api.Http;
import com.sas.sasapi.model.Course;
import com.sas.sasapi.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    CourseRepository courseRepository;

    AnalyticsController(CourseRepository courseRepository){
        this.courseRepository=courseRepository;
    }

    @GetMapping("/courseattendance")
    public ResponseEntity<HashMap<String,Integer>> getCourseAttendance(){
        List<Course> courses=courseRepository.findAll();
        HashMap<String,Integer> ans=new HashMap<>();
        for (Course course: courses) {
            ans.put(course.getCourseCode()+" - "+course.getDescription(),courseRepository.findCourseAttendance(course));

        }
        return new ResponseEntity<>(ans, HttpStatus.OK);
    }


}
