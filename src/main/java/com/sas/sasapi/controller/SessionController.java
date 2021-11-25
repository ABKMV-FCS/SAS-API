package com.sas.sasapi.controller;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.*;
import com.sas.sasapi.payload.request.*;
import com.sas.sasapi.payload.response.GetSessionDetailsResponse;
import com.sas.sasapi.payload.response.SessionFilterResponse;
import com.sas.sasapi.payload.response.UniqueDetailsResponse;
import com.sas.sasapi.repository.*;
import com.sas.sasapi.service.SessionService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Array;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/session")
public class SessionController {
    private final SessionRepository sessionRepository;
    private final SessionService sessionService;
    private final CourseBatchRepository courseBatchRepository;
    private final CourseRepository courseRepository;
    private final CourseYearRepository courseYearRepository;
    private final UserRepository userRepository;
    private final SessionAttendanceRepository sessionAttendanceRepository;

    public SessionController(SessionRepository sessionRepository,SessionService sessionService,CourseBatchRepository courseBatchRepository,CourseRepository courseRepository,CourseYearRepository courseYearRepository,UserRepository userRepository,SessionAttendanceRepository sessionAttendanceRepository) {
        this.sessionAttendanceRepository = sessionAttendanceRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.sessionRepository = sessionRepository;
        this.sessionService = sessionService;
        this.courseBatchRepository = courseBatchRepository;
        this.courseYearRepository = courseYearRepository;
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

    public long findTimeDiff(Date d1,Date d2) throws ParseException {
        long duration  = Math.abs(d1.getTime() - d2.getTime());
        long diff = TimeUnit.MILLISECONDS.toMinutes(duration);
        return diff;
    }

    public String convertDateFormat(String date){
        String dt[]=date.split(",");
        String d1=dt[0].trim(),d2=dt[1].trim();
        String d1a[]=d1.split("/");
        String d2a[]=d2.split(":");
        System.out.println("date = " + date);
        return d1a[2]+"-"+d1a[1]+"-"+d1a[0]+"T"+d2a[0]+":"+d2a[1];
    }

    public Date parseDate(String dateString) throws ParseException {
//        String dateString = "2021-11-25T22:20";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = simpleDateFormat.parse(dateString);
        return date;
    }

    @Transactional
    @PostMapping("/teamAttendanceUpdate")
    public ResponseEntity<String> teamAttendanceUpdate(@RequestBody TeamAttendanceUpdateRequest teamAttendanceUpdateRequest) throws ParseException {
        System.out.println("teamAttendanceUpdateRequest = " + teamAttendanceUpdateRequest);
        Course course = courseRepository.findByCourseCode(teamAttendanceUpdateRequest.getCourseCode());
        CourseYear courseYear = courseYearRepository.findByCourseAndSemesterAndYear(course, teamAttendanceUpdateRequest.getSemester(), teamAttendanceUpdateRequest.getAcademicYear());
        System.out.println("courseYear = " + courseYear);
        CourseBatch courseBatch = courseBatchRepository.findByCourseYearAndBatch(courseYear, teamAttendanceUpdateRequest.getBatch());
        Session session = Session.builder().courseBatch(courseBatch).fromDateTime(parseDate(teamAttendanceUpdateRequest.getFromDateTime())).toDateTime(parseDate(teamAttendanceUpdateRequest.getToDateTime())).build();
        sessionRepository.save(session);

        HashMap<String, ArrayList<String>> usernamesAndTime = new HashMap<String, ArrayList<String>>();
        for (int i = 0; i < teamAttendanceUpdateRequest.getCsv().size(); i++) {
            String name = teamAttendanceUpdateRequest.getCsv().get(i).get(0);
            ArrayList<String> al = usernamesAndTime.get(name);
            if (al == null) {
                al = new ArrayList<String>();
            }
            al.add(teamAttendanceUpdateRequest.getCsv().get(i).get(2));
            usernamesAndTime.put(name, al);
        }
        System.out.println("usernamesAndTime = " + usernamesAndTime);
        Set<String> keys = usernamesAndTime.keySet();
        for (String username : keys) {
            ArrayList<String> times = usernamesAndTime.get(username);
            int diff = 0;
            int times_length = times.size();
            for (int i = 0; i < times_length; i += 2) {
                if (times_length % 2 != 0 && i == times_length - 1) {
                    Date D1 = parseDate(convertDateFormat(times.get(i)));
//                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm",Locale.ENGLISH).parse(times.get(i));
                    diff += findTimeDiff(D1, parseDate(teamAttendanceUpdateRequest.getToDateTime()));
                } else {
                    Date D1 = parseDate(convertDateFormat(times.get(i)));
                    Date D2 = parseDate(convertDateFormat(times.get(i+1)));
//                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH).parse(times.get(i+1));
                    diff += findTimeDiff(D1,D2);
                }
            }
            long duration  = parseDate(teamAttendanceUpdateRequest.getToDateTime()).getTime() - parseDate(teamAttendanceUpdateRequest.getFromDateTime()).getTime();
            long threshold = TimeUnit.MILLISECONDS.toMinutes(duration);
            System.out.println("threshold = " + threshold);
                System.out.println("diff = " + diff);
            if (diff >= threshold) {
                System.out.println("diff = " + diff);
                User u = userRepository.getByUsername(username);
                sessionAttendanceRepository.save(SessionAttendance.builder().session(session).user(u).build());
                System.out.println("u.getUsername() = " + u.getUsername());
            }
        }
            return new ResponseEntity<>("Updated!", HttpStatus.OK);
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
    public ResponseEntity<SessionFilterResponse> filterSessions(@RequestBody SessionFilter sessionFilter) throws Exception {
        System.out.println(sessionFilter.getCourseCode()+sessionFilter.getYear()+sessionFilter.getSemester()+sessionFilter.getBatch());
    List<Session> s = sessionRepository.filterSessions(sessionFilter.getCourseCode(),sessionFilter.getYear(),sessionFilter.getSemester(),sessionFilter.getBatch());
        System.out.println("s = " + s);
        if(s.size()==0)throw new Exception("No such class found!");
    List<Long> sessionId = new ArrayList<>();
        List<Long> attendance = new ArrayList<>();
        Long attendedCount;
        Long courseBatchId = s.get(0).getCourseBatch().getCourseBatchId();
        Long totalCount = sessionRepository.getAllCount(courseBatchId);
        for (int i=0;i<s.size();++i){
            sessionId.add(s.get(i).getSessionId());
            attendedCount = sessionRepository.getAttendedCount(sessionId.get(i));
            if(totalCount == 0){
                attendance.add((long)0);
            }
            else{
                attendance.add(attendedCount/totalCount);
            }
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
