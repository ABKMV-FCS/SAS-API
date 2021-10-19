package com.sas.sasapi.controller;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.ODEvent;
import com.sas.sasapi.repository.ODEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/odevent")
public class ODEventController {
    private final ODEventRepository odEventRepository;

    public ODEventController(ODEventRepository odEventRepository) {
        this.odEventRepository = odEventRepository;
    }

    @GetMapping("/all")
    public List<ODEvent> getAllODEvents(){
        return odEventRepository.findAll();
    }

    @PostMapping("/create")
    public ODEvent createODEvent(@RequestBody ODEvent ODEvent){
        return odEventRepository.save(ODEvent);
    }

    @PutMapping("/update")
    public ResponseEntity<ODEvent> updateODEvent(@RequestBody ODEvent odEvent){
        ODEvent odEventObj = odEventRepository.findByEventId(odEvent.getEventId()).orElseThrow(() -> new ResourceNotFound("Cannot find odEvent in db"));

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

        ODEvent odEventObj = odEventRepository.findByEventId(odEvent.getEventId()).orElseThrow(() -> new ResourceNotFound("Cannot find odEvent in db"));
        odEventRepository.delete(odEventObj);
        return new ResponseEntity<>(odEventObj,HttpStatus.OK);

    }
}
