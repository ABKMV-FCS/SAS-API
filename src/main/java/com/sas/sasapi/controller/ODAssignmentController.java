package com.sas.sasapi.controller;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.ODAssignment;
import com.sas.sasapi.repository.ODAssignmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/odassignment")
public class ODAssignmentController {
    private final ODAssignmentRepository odAssignmentRepository;

    public ODAssignmentController(ODAssignmentRepository odAssignmentRepository) {
        this.odAssignmentRepository = odAssignmentRepository;
    }

    @GetMapping("/all")
    public List<ODAssignment> getAllODAssignments(){
        return odAssignmentRepository.findAll();
    }

    @PostMapping("/create")
    public ODAssignment createODAssignment(@RequestBody ODAssignment ODAssignment){
        return odAssignmentRepository.save(ODAssignment);
    }

    @PutMapping("/update")
    public ResponseEntity<ODAssignment> updateODAssignment(@RequestBody ODAssignment odAssignment){
        ODAssignment odAssignmentObj = odAssignmentRepository.findByOdAssignmentId(odAssignment.getOdAssignmentId()).orElseThrow(() -> new ResourceNotFound("Cannot find odAssignment in db"));

        odAssignmentObj.setOdEvent(odAssignment.getOdEvent());
        odAssignmentObj.setUser(odAssignment.getUser());
        odAssignmentObj.setOdAssignmentId(odAssignment.getOdAssignmentId());

        ODAssignment updatedODAssignment = odAssignmentRepository.save(odAssignmentObj);
        return new ResponseEntity<>(updatedODAssignment, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ODAssignment> deleteODAssignment(@RequestBody ODAssignment odAssignment){

        ODAssignment odAssignmentObj = odAssignmentRepository.findByOdAssignmentId(odAssignment.getOdAssignmentId()).orElseThrow(() -> new ResourceNotFound("Cannot find odAssignment in db"));
        odAssignmentRepository.delete(odAssignmentObj);
        return new ResponseEntity<>(odAssignmentObj,HttpStatus.OK);

    }
}
