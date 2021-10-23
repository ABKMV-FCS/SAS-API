package com.sas.sasapi.controller;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.Session;
import com.sas.sasapi.repository.SessionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/session")
public class SessionController {
    private final SessionRepository sessionRepository;
    public SessionController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/all")

    public List<Session> getAllSessions(){
        return sessionRepository.findAll();
    }

    @PostMapping("/create")
    public Session createSession(@RequestBody Session session){

        return sessionRepository.save(session);
    }

    @PutMapping("/update")
    public ResponseEntity<Session> updateSession(@RequestBody Session session){
        Session sessionObj = sessionRepository.findBySessionId(session.getSessionId()).orElseThrow(() -> new ResourceNotFound("Cannot find session in db"));

        sessionObj.setSessionId(session.getSessionId());
        sessionObj.setCourseBatch(session.getCourseBatch());
        sessionObj.setFromDateTime(session.getFromDateTime());
        sessionObj.setToDateTime(session.getToDateTime());


        Session updatedSession = sessionRepository.save(sessionObj);
        return new ResponseEntity<>(updatedSession, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Session> deleteSession(@RequestBody Session session){

        Session sessionObj = sessionRepository.findBySessionId(session.getSessionId()).orElseThrow(() -> new ResourceNotFound("Cannot find session in db"));
        sessionRepository.delete(sessionObj);
        return new ResponseEntity<>(sessionObj,HttpStatus.OK);

    }
}
