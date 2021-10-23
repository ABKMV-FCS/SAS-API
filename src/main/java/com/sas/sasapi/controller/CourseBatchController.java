package com.sas.sasapi.controller;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.CourseBatch;
import com.sas.sasapi.repository.CourseBatchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/course_batch")
public class CourseBatchController {
    private final CourseBatchRepository courseBatchRepository;

    public CourseBatchController(CourseBatchRepository courseBatchRepository) {
        this.courseBatchRepository = courseBatchRepository;
    }

    @GetMapping("/all")

    public List<CourseBatch> getAllCourseBatches(){
        return courseBatchRepository.findAll();
    }

    @PostMapping("/create")
    public CourseBatch createCourseBatch(@RequestBody CourseBatch courseBatch){

        return courseBatchRepository.save(courseBatch);
    }

    @PutMapping("/update")
    public ResponseEntity<CourseBatch> updateCourseYear(@RequestBody CourseBatch courseBatch){
        CourseBatch courseYearObj = courseBatchRepository.findByCourseBatchId(courseBatch.getCourseBatchId()).orElseThrow(() -> new ResourceNotFound("Cannot find courseBatch in db"));

        courseYearObj.setCourseBatchId(courseBatch.getCourseBatchId());
        courseYearObj.setCourseYear(courseBatch.getCourseYear());
        courseYearObj.setBatch(courseBatch.getBatch());


        CourseBatch updatedCourseYear = courseBatchRepository.save(courseYearObj);
        return new ResponseEntity<>(updatedCourseYear, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CourseBatch> deleteCourseYear(@RequestBody CourseBatch courseBatch){

        CourseBatch courseYearObj = courseBatchRepository.findByCourseBatchId(courseBatch.getCourseBatchId()).orElseThrow(() -> new ResourceNotFound("Cannot find courseBatch in db"));
        courseBatchRepository.delete(courseYearObj);
        return new ResponseEntity<>(courseYearObj,HttpStatus.OK);

    }
}
