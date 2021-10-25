package com.sas.sasapi.controller;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.ODEvent;
import com.sas.sasapi.model.User;
import com.sas.sasapi.payload.request.UpdateODEventRequest;
import com.sas.sasapi.repository.ODEventRepository;
import com.sas.sasapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/odevent")
public class ODEventController {
    private final ODEventRepository odEventRepository;
    private final UserRepository userRepository;

    public ODEventController(ODEventRepository odEventRepository,UserRepository userRepository) {
        this.odEventRepository = odEventRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public List<ODEvent> getAllODEvents(){
        return odEventRepository.findAll();
    }

    @PostMapping("/create")
    public ODEvent createODEvent(@RequestBody ODEvent ODEvent){
        return odEventRepository.save(ODEvent);
    }

    @PostMapping("/updateODEventByUsername")
    public ResponseEntity<ODEvent> updateODEventByUsername(@RequestBody UpdateODEventRequest updateODEventRequest){
        UserDetails userDetails = (UserDetails) SecurityContextHolder. getContext(). getAuthentication().getPrincipal();
        String username = userDetails. getUsername();

        User u = userRepository.findUserByUsername(username);


        ODEvent odEventObj = odEventRepository.findByEventName(updateODEventRequest.getEventName())
                .orElse(ODEvent.builder().eventName(updateODEventRequest.getEventName()).description(updateODEventRequest.getDescription()).user(u).fromDate(updateODEventRequest.getFromDate()).toDate(updateODEventRequest.getToDate()).build());

        odEventObj.setEventName(updateODEventRequest.getEventName());
        odEventObj.setDescription(updateODEventRequest.getDescription());
        odEventObj.setFromDate(updateODEventRequest.getFromDate());
        odEventObj.setToDate(updateODEventRequest.getToDate());
        odEventObj.setUser(u);

        ODEvent updatedODEvent = odEventRepository.save(odEventObj);
        return new ResponseEntity<>(updatedODEvent, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ODEvent> updateODEvent(@RequestBody ODEvent odEvent){
        ODEvent odEventObj = odEventRepository.findByOdEventId(odEvent.getOdEventId()).orElseThrow(() -> new ResourceNotFound("Cannot find odEvent in db"));

        odEventObj.setEventName(odEvent.getEventName());
        odEventObj.setDescription(odEvent.getDescription());
        odEventObj.setFromDate(odEvent.getFromDate());
        odEventObj.setToDate(odEvent.getToDate());
        odEventObj.setUser(odEvent.getUser());

        ODEvent updatedODEvent = odEventRepository.save(odEventObj);
        return new ResponseEntity<>(updatedODEvent, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ODEvent> deleteODEvent(@RequestBody ODEvent odEvent){

        ODEvent odEventObj = odEventRepository.findByOdEventId(odEvent.getOdEventId()).orElseThrow(() -> new ResourceNotFound("Cannot find odEvent in db"));
        odEventRepository.delete(odEventObj);
        return new ResponseEntity<>(odEventObj,HttpStatus.OK);

    }
}
