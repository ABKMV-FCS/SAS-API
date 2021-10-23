package com.sas.sasapi.controller;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.FacultyAssignment;
import com.sas.sasapi.repository.FacultyAssignmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/faculty_assignment")
public class FacultyAssignmentController {
    private final FacultyAssignmentRepository facultyAssignmentRepository;

    public FacultyAssignmentController(FacultyAssignmentRepository facultyAssignmentRepository) {
        this.facultyAssignmentRepository = facultyAssignmentRepository;
    }

    @GetMapping("/all")
    public List<FacultyAssignment> getAllFacultyAssignments(){
        return facultyAssignmentRepository.findAll();
    }

    @PostMapping("/create")
    public FacultyAssignment createFacultyAssignment(@RequestBody FacultyAssignment FacultyAssignment){
        return facultyAssignmentRepository.save(FacultyAssignment);
    }

    @PutMapping("/update")
    public ResponseEntity<FacultyAssignment> updateFacultyAssignment(@RequestBody FacultyAssignment odEvent){
        FacultyAssignment odEventObj = facultyAssignmentRepository.findByFacultyAssignmentId(odEvent.getFacultyAssignmentId()).orElseThrow(() -> new ResourceNotFound("Cannot find odEvent in db"));

        odEventObj.setFacultyAssignmentId(odEvent.getFacultyAssignmentId());
        odEventObj.setCourseBatch(odEvent.getCourseBatch());
        odEventObj.setUser(odEvent.getUser());

        FacultyAssignment updatedFacultyAssignment = facultyAssignmentRepository.save(odEventObj);
        return new ResponseEntity<>(updatedFacultyAssignment, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<FacultyAssignment> deleteFacultyAssignment(@RequestBody FacultyAssignment odEvent){

        FacultyAssignment odEventObj = facultyAssignmentRepository.findByFacultyAssignmentId(odEvent.getFacultyAssignmentId()).orElseThrow(() -> new ResourceNotFound("Cannot find odEvent in db"));
        facultyAssignmentRepository.delete(odEventObj);
        return new ResponseEntity<>(odEventObj,HttpStatus.OK);

    }
}
