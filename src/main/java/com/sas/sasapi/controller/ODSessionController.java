package com.sas.sasapi.controller;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.ODSession;
import com.sas.sasapi.repository.ODSessionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/odsession")
public class ODSessionController {
    private final ODSessionRepository odSessionRepository;

    public ODSessionController(ODSessionRepository odSessionRepository) {
        this.odSessionRepository = odSessionRepository;
    }

    @GetMapping("/getmyodapprovals")
    @PreAuthorize("hasRole('FACULTY')")
    public List<ODSession> getMyOdApprovals(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder. getContext(). getAuthentication().getPrincipal();
        String username = userDetails. getUsername();
        return odSessionRepository.findByUsernameGetOdSessionApproval(username);
    }

    @GetMapping("/all")
    public List<ODSession> getAllODSessions(){
        return odSessionRepository.findAll();
    }

    @PostMapping("/create")
    public ODSession createODSession(@RequestBody ODSession ODSession){
        return odSessionRepository.save(ODSession);
    }

    @PutMapping("/update")
    public ResponseEntity<ODSession> updateODSession(@RequestBody ODSession odSession){
        ODSession odSessionObj = odSessionRepository.findByOdSessionId(odSession.getOdSessionId()).orElseThrow(() -> new ResourceNotFound("Cannot find odSession in db"));

        odSessionObj.setSession(odSession.getSession());
        odSessionObj.setOdSessionId(odSession.getOdSessionId());
        odSessionObj.setOdAssignment(odSession.getOdAssignment());

        ODSession updatedODSession = odSessionRepository.save(odSessionObj);
        return new ResponseEntity<>(updatedODSession, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ODSession> deleteODSession(@RequestBody ODSession odSession){
        ODSession odSessionObj = odSessionRepository.findByOdSessionId(odSession.getOdSessionId()).orElseThrow(() -> new ResourceNotFound("Cannot find odSession in db"));
        odSessionRepository.delete(odSessionObj);
        return new ResponseEntity<>(odSessionObj,HttpStatus.OK);
    }
}
