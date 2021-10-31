package com.sas.sasapi.service;

import com.sas.sasapi.controller.SessionController;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.Session;
import com.sas.sasapi.model.SessionAttendance;
import com.sas.sasapi.payload.request.DeleteSessionDetailsRequest;
import com.sas.sasapi.payload.request.UpdateSessionDetailsRequest;
import com.sas.sasapi.payload.response.GetStudentAttendanceResponse;
import com.sas.sasapi.repository.ODSessionRepository;
import com.sas.sasapi.repository.SessionAttendanceRepository;
import com.sas.sasapi.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("SessionService")
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private SessionAttendanceRepository sessionAttendanceRepository;
    @Autowired
    private ODSessionRepository odSessionRepository;


    public List<SessionAttendance> updateSessionDetails(UpdateSessionDetailsRequest updateSessionDetailsRequest){

        Long count = sessionRepository.getSessionCount(updateSessionDetailsRequest.getSessionId());
        if(count != 0){
            sessionAttendanceRepository.deleteSessionAttendanceBySessionId(updateSessionDetailsRequest.getSessionId());
        }
        else{
            throw new ResourceNotFound("Cannot find session in db");
        }

        List<SessionAttendance> sl = new ArrayList<SessionAttendance>();
        Session session = sessionRepository.FilterBySessionId(updateSessionDetailsRequest.getSessionId());
        for (int i = 0; i < updateSessionDetailsRequest.getUsers().size(); i++) {
            SessionAttendance s = SessionAttendance.builder().session(session).user(updateSessionDetailsRequest.getUsers().get(i)).build();
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

    public GetStudentAttendanceResponse getStudentAttendance(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder. getContext(). getAuthentication().getPrincipal();
        String username = userDetails. getUsername();

        List<String> cc = sessionAttendanceRepository.getCourseCodesByUsername(username);
        System.out.println("username = " + username);
        System.out.println("cc = " + cc);

        List<Long> sessions = new ArrayList<Long>();
        List<Integer> attendedSessions = new ArrayList<Integer>();


        HashSet<Long> set = new HashSet <Long>();
        HashSet<Long> set1 = new HashSet <Long>();
        for (int i = 0; i < cc.size(); i++) {
            sessions.add(sessionRepository.getCourseCodeCountByCourseCode(cc.get(i)));
            set = sessionAttendanceRepository.getCourseCodeAttendedCountByCourseCodeAndUsername(cc.get(i),username);
            set1 = odSessionRepository.findApprovedODSessionIDs(username, cc.get(i));
            System.out.println("set = " + set);
            set.addAll(set1);
            int val = set.size();
            attendedSessions.add(val);
        }
        System.out.println("set1 = " + set1);

        GetStudentAttendanceResponse gs = GetStudentAttendanceResponse.builder().cc(cc).attendedSessions(attendedSessions).sessions(sessions).build();
        return gs;
    }
}
