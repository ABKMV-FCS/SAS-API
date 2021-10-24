package com.sas.sasapi.service;

import com.sas.sasapi.controller.SessionController;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.Session;
import com.sas.sasapi.model.SessionAttendance;
import com.sas.sasapi.payload.request.DeleteSessionDetailsRequest;
import com.sas.sasapi.payload.request.UpdateSessionDetailsRequest;
import com.sas.sasapi.repository.ODSessionRepository;
import com.sas.sasapi.repository.SessionAttendanceRepository;
import com.sas.sasapi.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("SessionService")
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private SessionAttendanceRepository sessionAttendanceRepository;
    @Autowired
    private ODSessionRepository odSessionRepository;

    public List<SessionAttendance> updateSessionDetails(UpdateSessionDetailsRequest updateSessionDetailsRequest){
        Long count = sessionRepository.getSessionCount(updateSessionDetailsRequest.getSession().getSessionId());
        if(count == 0){
            Session s = sessionRepository.save(updateSessionDetailsRequest.getSession());;
        }
        else{
            sessionAttendanceRepository.deleteSessionAttendanceBySessionId(updateSessionDetailsRequest.getSession().getSessionId());
        }

        List<SessionAttendance> sl = new ArrayList<SessionAttendance>();
        for (int i = 0; i < updateSessionDetailsRequest.getUsers().size(); i++) {
            SessionAttendance s = SessionAttendance.builder().session(updateSessionDetailsRequest.getSession()).user(updateSessionDetailsRequest.getUsers().get(i)).build();
            sl.add(s);
            sessionAttendanceRepository.save(s);
        }
        return sl;
    }
    @Transactional
    public ResponseEntity<Session> deleteSessionDetails(DeleteSessionDetailsRequest deleteSessionDetailsRequest){
        Session sessionObj = sessionRepository.findBySessionId(deleteSessionDetailsRequest.getSessionId()).orElseThrow(() -> new ResourceNotFound("Cannot find session in db"));
        odSessionRepository.deleteODSessionBySessionId(deleteSessionDetailsRequest.getSessionId());
        sessionAttendanceRepository.deleteSessionAttendanceBySessionId(deleteSessionDetailsRequest.getSessionId());
        sessionRepository.deleteBySessionId(deleteSessionDetailsRequest.getSessionId());
        return new ResponseEntity<>(sessionObj,HttpStatus.OK);
    }
}
