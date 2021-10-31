package com.sas.sasapi.controller;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.CourseBatch;
import com.sas.sasapi.model.Session;
import com.sas.sasapi.model.SessionAttendance;
import com.sas.sasapi.model.User;
import com.sas.sasapi.payload.request.*;
import com.sas.sasapi.payload.response.GetSessionDetailsResponse;
import com.sas.sasapi.payload.response.SessionFilterResponse;
import com.sas.sasapi.payload.response.UniqueDetailsResponse;
import com.sas.sasapi.repository.CourseBatchRepository;
import com.sas.sasapi.repository.SessionRepository;
import com.sas.sasapi.service.SessionService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/session")
public class SessionController {
    private final SessionRepository sessionRepository;
    private final SessionService sessionService;
    private final CourseBatchRepository courseBatchRepository;
    public SessionController(SessionRepository sessionRepository,SessionService sessionService,CourseBatchRepository courseBatchRepository) {
        this.sessionRepository = sessionRepository;
        this.sessionService = sessionService;
        this.courseBatchRepository = courseBatchRepository;

    }




    @GetMapping("/all")
    public List<Session> getAllSessions(){
        return sessionRepository.findAll();
    }



    @PostMapping("/create")
    public Long createSession(@RequestBody CreateSessionRequest createSessionRequest){
    CourseBatch courseBatch = courseBatchRepository.findByCourseCodeAndYearAndSemesterAndBatch(createSessionRequest.getCourseCode(),createSessionRequest.getYear(),createSessionRequest.getSemester(),createSessionRequest.getBatch());
        Session session = Session.builder().courseBatch(courseBatch).fromDateTime(createSessionRequest.getFromDateTime()).toDateTime(createSessionRequest.getToDateTime()).build();
        return sessionRepository.save(session).getSessionId();
    }

    @Transactional
    @PostMapping("/updateSessionDetails")
    public ResponseEntity<List<SessionAttendance>> updateSessionDetails(@RequestBody UpdateSessionDetailsRequest updateSessionDetailsRequest){
        List<SessionAttendance> sl = sessionService.updateSessionDetails(updateSessionDetailsRequest);
        return new ResponseEntity<>(sl,HttpStatus.OK);
    }
@Transactional
    @PostMapping("/deleteSessionDetails")
    public ResponseEntity<Session> deleteSessionDetails(@RequestBody DeleteSessionDetailsRequest deleteSessionDetailsRequest){
        return sessionService.deleteSessionDetails(deleteSessionDetailsRequest);
    }

    @GetMapping("/getUniqueDetails")
    public ResponseEntity<UniqueDetailsResponse> getUniqueDetails(){
        List <Long> AcademicYear= sessionRepository.getUniqueAcademicYear();
        List <Long> semester= sessionRepository.getUniqueSemester();
        List <String> courseCode= sessionRepository.getCourseCode();
        List <String> batch= sessionRepository.getUniqueBatch();

        UniqueDetailsResponse result = new UniqueDetailsResponse(AcademicYear,semester,courseCode,batch);
        return new ResponseEntity<> (result,HttpStatus.OK);
    }

    @PostMapping("/getAddSessionDetails")
    public ResponseEntity<GetSessionDetailsResponse> getAddSessionDetails(@RequestBody GetSessionDetailsRequest getSessiondetailsRequest){
        System.out.println("getSessiondetailsRequest = " + getSessiondetailsRequest);
        GetSessionDetailsResponse result;
        List<User> u = sessionRepository.getCourseUsers(getSessiondetailsRequest.getCourseCode(),getSessiondetailsRequest.getAcademicYear(),getSessiondetailsRequest.getSemester(),getSessiondetailsRequest.getBatch());
        List<User> ur = null;
        if(getSessiondetailsRequest.getFromDateTime() != null && getSessiondetailsRequest.getToDateTime() != null){
            ur = sessionRepository.getCourseAttendedUsers(getSessiondetailsRequest.getCourseCode(),getSessiondetailsRequest.getAcademicYear(),getSessiondetailsRequest.getSemester(),getSessiondetailsRequest.getBatch(),getSessiondetailsRequest.getFromDateTime(),getSessiondetailsRequest.getToDateTime());
        }
        result = new GetSessionDetailsResponse(u,ur);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/filter")
    public ResponseEntity<SessionFilterResponse> filterSessions(@RequestBody SessionFilter sessionFilter){
        System.out.println(sessionFilter.getCourseCode()+sessionFilter.getYear()+sessionFilter.getSemester()+sessionFilter.getBatch());
    List<Session> s = sessionRepository.filterSessions(sessionFilter.getCourseCode(),sessionFilter.getYear(),sessionFilter.getSemester(),sessionFilter.getBatch());
        List<Long> sessionId = new ArrayList<>();
        List<Long> attendance = new ArrayList<>();
        Long attendedCount;
        Long courseBatchId = s.get(0).getCourseBatch().getCourseBatchId();
        Long totalCount = sessionRepository.getAllCount(courseBatchId);
        for (int i=0;i<s.size();++i){
            sessionId.add(s.get(i).getSessionId());
            attendedCount = sessionRepository.getAttendedCount(sessionId.get(i));
            attendance.add(attendedCount/totalCount);
        }
        SessionFilterResponse result = new SessionFilterResponse(attendance,s);
        System.out.println("result = " + result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    };

    @Transactional
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
@Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<Session> deleteSession(@RequestBody Session session){

        Session sessionObj = sessionRepository.findBySessionId(session.getSessionId()).orElseThrow(() -> new ResourceNotFound("Cannot find session in db"));
        sessionRepository.delete(sessionObj);
        return new ResponseEntity<>(sessionObj,HttpStatus.OK);

    }
}
