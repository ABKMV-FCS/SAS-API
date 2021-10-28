package com.sas.sasapi.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.sas.sasapi.exception.ResourceNotFound;
import com.sas.sasapi.model.User;
import com.sas.sasapi.payload.request.PasswordRequest;
import com.sas.sasapi.payload.request.UseridRequest;
import com.sas.sasapi.payload.response.MessageResponse;
import com.sas.sasapi.repository.UserRepository;
import com.sas.sasapi.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Value("${application.bucket.name}")
    private String bucketName;

    private final UserRepository userRepository;

    @Autowired
    private StorageService service;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private AmazonS3 s3Client;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @PostMapping("/findone")
    public User findOneUser(@RequestBody UseridRequest useridRequest){
        return userRepository.findByUserId(useridRequest.getUserId()).orElseThrow(() -> new ResourceNotFound("Cannot find user in db"));
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user){
        return userRepository.save(user);
    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        User userObj = userRepository.findByUserId(user.getUserId()).orElseThrow(() -> new ResourceNotFound("Cannot find user in db"));

        userObj.setUsername(user.getUsername());
        userObj.setPassword(user.getPassword());
        userObj.setName(user.getName());
        userObj.setRoles(user.getRoles());
        userObj.setAddress(user.getAddress());
        userObj.setEmail(user.getEmail());

        User updatedUser = userRepository.save(userObj);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    @Transactional

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "photo") MultipartFile file) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder. getContext(). getAuthentication().getPrincipal();
        String username = userDetails. getUsername();
        try {
            boolean doesItExists = s3Client.doesObjectExist(bucketName, username);
            if(doesItExists == true){
                System.out.println( deleteFile(username)+" deleted");
            }
        } catch (Exception error) {
            System.out.println(error);
        }
        return new ResponseEntity<>(service.uploadFile(file), HttpStatus.OK);
    }
    @Transactional
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        return new ResponseEntity<>(service.deleteFile(fileName), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUser(@RequestBody User user){

        User userObj = userRepository.findByUserId(user.getUserId()).orElseThrow(() -> new ResourceNotFound("Cannot find user in db"));
        userRepository.delete(userObj);
        return new ResponseEntity<>(userObj,HttpStatus.OK);

    }

    @PostMapping("/resetpassword")
    public  ResponseEntity<MessageResponse> resetPassword(@RequestBody PasswordRequest passwordRequest){
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder. getContext(). getAuthentication().getPrincipal();
            String username = userDetails. getUsername();
            User user = (User) userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
            user.setPassword(encoder.encode(passwordRequest.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok().body(new MessageResponse("password reset successfully!"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }

    }
}