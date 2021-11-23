package com.sas.sasapi.controller;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.ODSession;
import com.sas.sasapi.model.User;
import com.sas.sasapi.payload.response.GetStudentODListResponse;
import com.sas.sasapi.repository.ODSessionRepository;
import com.sas.sasapi.repository.UserRepository;
import com.sas.sasapi.service.PushNotificationService;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/odsession")
public class ODSessionController {
    private final ODSessionRepository odSessionRepository;
    private final PushNotificationService pushNotificationService;

    private final UserRepository userRepository;

    public ODSessionController(ODSessionRepository odSessionRepository, PushNotificationService pushNotificationService, UserRepository userRepository) {
        this.odSessionRepository = odSessionRepository;
        this.pushNotificationService = pushNotificationService;
        this.userRepository = userRepository;
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

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<ODSession> updateODSession(@RequestBody ODSession odSession) throws JSONException, IOException {
        ODSession odSessionObj = odSessionRepository.findByOdSessionId(odSession.getOdSessionId()).orElseThrow(() -> new ResourceNotFound("Cannot find odSession in db"));

        odSessionObj.setSession(odSession.getSession());
        odSessionObj.setOdSessionId(odSession.getOdSessionId());
        odSessionObj.setOdAssignment(odSession.getOdAssignment());
        if(odSessionObj.getStatus() != odSession.getStatus()){
            String fcmToken = userRepository.getFcmToken(odSession.getOdAssignment().getUser().getUsername());
            pushNotificationService.sendNotification(fcmToken,odSession.getStatus());
        }
        odSessionObj.setStatus(odSession.getStatus());
        ODSession updatedODSession = odSessionRepository.save(odSessionObj);
        return new ResponseEntity<>(updatedODSession, HttpStatus.OK);
    }


    @GetMapping("/test")
    public void testNotification(@RequestParam String username) throws JSONException, IOException{
        System.out.println(username);
        User user=(User) userRepository.findByUsername(username.trim())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        pushNotificationService.sendNotification(user.getFcmToken(),"Approved");
    }

    @GetMapping("/getStudentODList")
    public ResponseEntity<List<Object>> getStudentODList(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder. getContext(). getAuthentication().getPrincipal();
        String username = userDetails. getUsername();
        System.out.println("username = " + username);
        List<Object> ODList=odSessionRepository.getStudentODList(username);
        System.out.println("ODList = " + ODList);
        return new ResponseEntity<>(ODList, HttpStatus.OK);
    }

    @PostMapping("/")

    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<ODSession> deleteODSession(@RequestBody ODSession odSession){
        ODSession odSessionObj = odSessionRepository.findByOdSessionId(odSession.getOdSessionId()).orElseThrow(() -> new ResourceNotFound("Cannot find odSession in db"));
        odSessionRepository.delete(odSessionObj);
        return new ResponseEntity<>(odSessionObj,HttpStatus.OK);
    }
}
