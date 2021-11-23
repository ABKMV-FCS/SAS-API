package com.sas.sasapi.controller;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.Session;
import com.sas.sasapi.model.SessionAttendance;
import com.sas.sasapi.payload.response.GetStudentAttendanceResponse;
import com.sas.sasapi.payload.response.GetStudentCourseWiseAttendanceResponse;
import com.sas.sasapi.repository.SessionAttendanceRepository;
import com.sas.sasapi.repository.SessionRepository;
import com.sas.sasapi.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/session_attendance")
public class SessionAttendanceController {
    private final SessionAttendanceRepository sessionAttendanceRepository;
    private final SessionService sessionService;
    public SessionAttendanceController(SessionAttendanceRepository sessionAttendanceRepository, SessionService sessionService) {
        this.sessionAttendanceRepository = sessionAttendanceRepository;
        this.sessionService = sessionService;
    }

    @GetMapping("/all")
    public List<SessionAttendance> getAllSessionAttendances(){
        return sessionAttendanceRepository.findAll();
    }
    @PostMapping("/getStudentCourseWiseAttendance")
    public ResponseEntity<GetStudentCourseWiseAttendanceResponse> getStudentCourseWiseAttendance(@RequestParam String courseCode){

        UserDetails userDetails = (UserDetails) SecurityContextHolder. getContext(). getAuthentication().getPrincipal();
        String username = userDetails. getUsername();
        System.out.println("username = " + username);
        HashSet<Session> coursesAttended = sessionAttendanceRepository.getCourseCodeAttendedSessionsByCourseCodeAndUsername(courseCode,username);
        HashSet<Session> coursesNotAttended = sessionAttendanceRepository.getCourseCodeNotAttendedCountByCourseCodeAndUsername(courseCode,username);
        GetStudentCourseWiseAttendanceResponse getStudentCourseWiseAttendanceResponse = GetStudentCourseWiseAttendanceResponse.builder().coursesAttended(coursesAttended).coursesNotAttended(coursesNotAttended).build();
        return new ResponseEntity<>(getStudentCourseWiseAttendanceResponse, HttpStatus.OK);
    }

    @PostMapping("/create")
    public SessionAttendance createSessionAttendance(@RequestBody SessionAttendance SessionAttendance){
        return sessionAttendanceRepository.save(SessionAttendance);
    }
@Transactional
    @PutMapping("/update")
    public ResponseEntity<SessionAttendance> updateSessionAttendance(@RequestBody SessionAttendance sessionAttendance){
        SessionAttendance sessionAttendanceObj = sessionAttendanceRepository.findBySessionAttendanceId(sessionAttendance.getSessionAttendanceId()).orElseThrow(() -> new ResourceNotFound("Cannot find sessionAttendance in db"));

        sessionAttendanceObj.setSession(sessionAttendance.getSession());
        sessionAttendanceObj.setSessionAttendanceId(sessionAttendance.getSessionAttendanceId());
        sessionAttendanceObj.setUser(sessionAttendance.getUser());

        SessionAttendance updatedSessionAttendance = sessionAttendanceRepository.save(sessionAttendanceObj);
        return new ResponseEntity<>(updatedSessionAttendance, HttpStatus.OK);
    }
    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<SessionAttendance> deleteSessionAttendance(@RequestBody SessionAttendance sessionAttendance){

        SessionAttendance sessionAttendanceObj = sessionAttendanceRepository.findBySessionAttendanceId(sessionAttendance.getSessionAttendanceId()).orElseThrow(() -> new ResourceNotFound("Cannot find sessionAttendance in db"));
        sessionAttendanceRepository.delete(sessionAttendanceObj);
        return new ResponseEntity<>(sessionAttendanceObj,HttpStatus.OK);

    }

    @GetMapping("/getStudentAttendance")
    public ResponseEntity<GetStudentAttendanceResponse> getStudentAttendance(){

        GetStudentAttendanceResponse gs = sessionService.getStudentAttendance();
        return new ResponseEntity<>(gs,HttpStatus.OK);

    }
}
