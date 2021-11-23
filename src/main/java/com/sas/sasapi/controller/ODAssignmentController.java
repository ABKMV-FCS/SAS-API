package com.sas.sasapi.controller;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.*;
import com.sas.sasapi.payload.request.BulkODUpdateRequest;
import com.sas.sasapi.repository.ODAssignmentRepository;
import com.sas.sasapi.repository.ODSessionRepository;
import com.sas.sasapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/odassignment")
public class ODAssignmentController {
    @Autowired
    private final ODAssignmentRepository odAssignmentRepository;

    @Autowired
    private final UserRepository userRepository;
    
    private final ODSessionRepository odSessionRepository;

    public ODAssignmentController(ODAssignmentRepository odAssignmentRepository, UserRepository userRepository,ODSessionRepository odSessionRepository) {
        this.odAssignmentRepository = odAssignmentRepository;
        this.userRepository = userRepository;
        this.odSessionRepository = odSessionRepository;
    }

    @GetMapping("/all")
    public List<ODAssignment> getAllODAssignments(){
        return odAssignmentRepository.findAll();
    }

    @PostMapping("/create")
    public ODAssignment createODAssignment(@RequestBody ODAssignment ODAssignment){
        return odAssignmentRepository.save(ODAssignment);
    }

    @Transactional
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

    @PostMapping("/bulkODListDownload")
    public ResponseEntity<List<String>> bulkODListDownload(@RequestBody ODEvent odEvent){
        return new ResponseEntity<List<String>>(odAssignmentRepository.getODStudentsList(odEvent),HttpStatus.OK);
    }


    @Transactional
    @PostMapping("/bulkODListUpdate")
    public ResponseEntity<?> bulkODListUpdate(@RequestBody BulkODUpdateRequest bulkODUpdateRequest){
        List<ODAssignment> inDB = odAssignmentRepository.getAll();
        for (ODAssignment ODAssignmentInDB:(inDB)
        ) {
            int flag = 0;
            for (String username:(bulkODUpdateRequest.getUsernames())
            ) {
                if(username.equals(ODAssignmentInDB.getUser().getUsername())){
                    System.out.println("1.username==ODAssignmentInDB.getUser().getUsername()");
                    flag=1;
                    break;
                }
            }
            if(flag == 0){
                ODAssignment o = odAssignmentRepository.findByOdEvent_OdEventIdAndUser_Username(bulkODUpdateRequest.getOdEvent().getOdEventId(),ODAssignmentInDB.getUser().getUsername());
                System.out.println("o = " + o);
                List<ODSession> lod = odSessionRepository.findByOdAssignment(o);
                odSessionRepository.deleteAll(lod);
                odAssignmentRepository.delete(o);
            }
        }
        for (String username:(bulkODUpdateRequest.getUsernames())
        ) {
            int flag = 0;
            for (ODAssignment ODAssignmentInDB:(inDB)
            ) {
                if(username.equals(ODAssignmentInDB.getUser().getUsername())){
                    System.out.println("2.username==ODAssignmentInDB.getUser().getUsername()");
                    flag=1;
                    break;
                }
            }
            if(flag == 0){
                User user = (User) userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
                odAssignmentRepository.save(new ODAssignment(bulkODUpdateRequest.getOdEvent(), user));
            }
        }
        return new ResponseEntity<String>("Updated Successfully!",HttpStatus.OK);
    }
}
