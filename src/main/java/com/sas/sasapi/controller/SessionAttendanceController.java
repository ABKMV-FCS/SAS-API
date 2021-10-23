package com.sas.sasapi.controller;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.SessionAttendance;
import com.sas.sasapi.repository.SessionAttendanceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/session_attendance")
public class SessionAttendanceController {
    private final SessionAttendanceRepository sessionAttendanceRepository;

    public SessionAttendanceController(SessionAttendanceRepository sessionAttendanceRepository) {
        this.sessionAttendanceRepository = sessionAttendanceRepository;
    }

    @GetMapping("/all")
    public List<SessionAttendance> getAllSessionAttendances(){
        return sessionAttendanceRepository.findAll();
    }

    @PostMapping("/create")
    public SessionAttendance createSessionAttendance(@RequestBody SessionAttendance SessionAttendance){
        return sessionAttendanceRepository.save(SessionAttendance);
    }

    @PutMapping("/update")
    public ResponseEntity<SessionAttendance> updateSessionAttendance(@RequestBody SessionAttendance sessionAttendance){
        SessionAttendance sessionAttendanceObj = sessionAttendanceRepository.findBySessionAttendanceId(sessionAttendance.getSessionAttendanceId()).orElseThrow(() -> new ResourceNotFound("Cannot find sessionAttendance in db"));

        sessionAttendanceObj.setSession(sessionAttendance.getSession());
        sessionAttendanceObj.setSessionAttendanceId(sessionAttendance.getSessionAttendanceId());
        sessionAttendanceObj.setUser(sessionAttendance.getUser());

        SessionAttendance updatedSessionAttendance = sessionAttendanceRepository.save(sessionAttendanceObj);
        return new ResponseEntity<>(updatedSessionAttendance, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<SessionAttendance> deleteSessionAttendance(@RequestBody SessionAttendance sessionAttendance){

        SessionAttendance sessionAttendanceObj = sessionAttendanceRepository.findBySessionAttendanceId(sessionAttendance.getSessionAttendanceId()).orElseThrow(() -> new ResourceNotFound("Cannot find sessionAttendance in db"));
        sessionAttendanceRepository.delete(sessionAttendanceObj);
        return new ResponseEntity<>(sessionAttendanceObj,HttpStatus.OK);

    }
}
