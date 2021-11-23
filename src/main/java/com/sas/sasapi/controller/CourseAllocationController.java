package com.sas.sasapi.controller;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.CourseAllocation;
import com.sas.sasapi.model.CourseBatch;
import com.sas.sasapi.model.ODAssignment;
import com.sas.sasapi.model.User;
import com.sas.sasapi.payload.request.BulkStudentListUpdateRequest;
import com.sas.sasapi.repository.CourseAllocationRepository;
import com.sas.sasapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/course_allocation")
public class CourseAllocationController {
    private final CourseAllocationRepository courseAllocationRepository;
    private final UserRepository userRepository;

    public CourseAllocationController(CourseAllocationRepository courseAllocationRepository,UserRepository userRepository) {
        this.userRepository = userRepository;
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


    @Transactional
    @PutMapping("/update")
    public ResponseEntity<CourseAllocation> updateCourseAllocation(@RequestBody CourseAllocation courseAllocation){
        CourseAllocation courseAllocationObj = courseAllocationRepository.findByCourseAllocationId(courseAllocation.getCourseAllocationId()).orElseThrow(() -> new ResourceNotFound("Cannot find courseAllocation in db"));

        courseAllocationObj.setCourseBatch(courseAllocation.getCourseBatch());
        courseAllocationObj.setCourseAllocationId(courseAllocation.getCourseAllocationId());
        courseAllocationObj.setUser(courseAllocation.getUser());


        CourseAllocation updatedCourseAllocation = courseAllocationRepository.save(courseAllocationObj);
        return new ResponseEntity<>(updatedCourseAllocation, HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/bulkStudentListUpdateRequest")
    public ResponseEntity<String> bulkStudentListUpdate(@RequestBody BulkStudentListUpdateRequest bulkStudentListUpdateRequest){

        List<CourseAllocation> inDB = courseAllocationRepository.getAll();
        for (CourseAllocation courseAllocationInDB:(inDB)
        ) {
            int flag = 0;
            for (String username:(bulkStudentListUpdateRequest.getUsernames())
            ) {
                if(username.equals(courseAllocationInDB.getUser().getUsername())){
                    flag=1;
                    break;
                }
            }
            if(flag == 0){
                CourseAllocation c = courseAllocationRepository.findByCourseBatch_CourseBatchIdAndUser_Name(bulkStudentListUpdateRequest.getCourseBatchEvent().getCourseBatchId(),courseAllocationInDB.getUser().getUsername());
                courseAllocationRepository.delete(c);
            }
        }
        for (String username:(bulkStudentListUpdateRequest.getUsernames())
             ) {
            int flag = 0;
            for (CourseAllocation courseAllocationInDB:(inDB)
                 ) {
                if(username.equals(courseAllocationInDB.getUser().getUsername())){
                    flag=1;
                    break;
                }
            }
            if(flag == 0){
                User user = (User) userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
                courseAllocationRepository.save(new CourseAllocation(bulkStudentListUpdateRequest.getCourseBatchEvent(), user));
            }
        }
            return new ResponseEntity<String>("Updated Successfully!",HttpStatus.OK);
    }
    @PostMapping("/bulkStudentListDownload")
    public ResponseEntity<List<String>> bulkStudentListDownload(@RequestBody CourseBatch courseBatch){
        List<CourseAllocation> cas= courseAllocationRepository.findByCourseBatch_CourseBatchId(courseBatch.getCourseBatchId());
        List<String> users = new ArrayList<>();
        for (int i = 0; i < cas.size(); i++) {
            users.add(cas.get(i).getUser().getUsername());
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<CourseAllocation> deleteCourseYear(@RequestBody CourseAllocation courseAllocation){
        CourseAllocation courseAllocationObj = courseAllocationRepository.findByCourseAllocationId(courseAllocation.getCourseAllocationId()).orElseThrow(() -> new ResourceNotFound("Cannot find courseAllocation in db"));
        courseAllocationRepository.delete(courseAllocationObj);
        return new ResponseEntity<>(courseAllocationObj,HttpStatus.OK);

    }
}
