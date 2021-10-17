package com.sas.sasapi.controller;

import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.User;
import com.sas.sasapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user){
        return userRepository.save(user);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        User userObj = userRepository.findById(user.getUsername()).orElseThrow(() -> new ResourceNotFound("Cannot find user in db"));

        userObj.setUsername(user.getUsername());
        userObj.setPassword(user.getPassword());
        userObj.setRole(user.getRole());
        userObj.setAddress(user.getAddress());

        User updatedUser = userRepository.save(userObj);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUser(@RequestBody User user){

        User userObj = userRepository.findById(user.getUsername()).orElseThrow(() -> new ResourceNotFound("Cannot find user in db"));
        userRepository.delete(userObj);
        return new ResponseEntity<>(userObj,HttpStatus.OK);

    }
}