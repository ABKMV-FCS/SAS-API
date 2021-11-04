package com.sas.sasapi.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


import com.amazonaws.services.simpleworkflow.flow.core.TryCatch;
import com.google.firebase.auth.FirebaseAuthException;
import com.sas.sasapi.payload.request.PasswordResetRequest;
import com.sas.sasapi.security.services.EmailService;
import com.sas.sasapi.service.CaptchaService;
import com.sas.sasapi.service.FirebaseAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.sas.sasapi.model.ERole;
import com.sas.sasapi.model.Role;
import com.sas.sasapi.model.User;
import com.sas.sasapi.payload.request.LoginRequest;
import com.sas.sasapi.payload.request.SignupRequest;
import com.sas.sasapi.payload.response.JwtResponse;
import com.sas.sasapi.payload.response.MessageResponse;
import com.sas.sasapi.repository.RoleRepository;
import com.sas.sasapi.repository.UserRepository;
import com.sas.sasapi.security.jwt.JwtUtils;
import com.sas.sasapi.security.services.UserDetailsImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private  EmailService emailService;

    @Autowired
    private FirebaseAdmin firebaseAdmin;

    @Autowired
    private CaptchaService captchaService;

    @Value("${sas.app.fe_url}")
    private String url;

    @Value("${sas.app.jwtExpirationMs}")
    private int defaultJwtExpirationMs;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            captchaService.verifyCaptcha(loginRequest.getCaptchaResponse());
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/googlelogin")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String,Object> idTokenRequest){
        System.out.println(idTokenRequest);
        String email,idToken= (String) idTokenRequest.getOrDefault("idToken","");
        try {
            email=firebaseAdmin.ifAuthenticatedGetEmail(idToken);
        } catch (FirebaseAuthException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        try {
            User user=userRepository.findByEmail(email);
            if(user==null){
                throw new RuntimeException("Couldn't find user with such email!");
            }
            String jwt= jwtUtils.generateJwtTokenFromUsername(user.getUsername(),defaultJwtExpirationMs);
            List<String> roles = user.getRoles().stream().map(s->String.valueOf(s)).collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt,
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    roles));
        }catch (Exception e){
            return (ResponseEntity<?>) ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getName(),signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail(),signUpRequest.getAddress());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "faculty":
                        Role modRole = roleRepository.findByName(ERole.ROLE_FACULTY)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/forgotpassword")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        try {
            captchaService.verifyCaptcha(passwordResetRequest.getCaptchaResponse());
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        try {
            User user=userRepository.findByEmail(passwordResetRequest.getEmail());
            if(user==null){
                throw new RuntimeException("Couldn't find user with such email!");
            }
            String jwt= jwtUtils.generateJwtTokenFromUsername(user.getUsername(),600000);
//            send jwt to email
            emailService.sendMail(passwordResetRequest.getEmail(), "SAS Application Password Reset", "Click on this url to reset your password: "+url+"/resetpassword?token="+jwt);
            return ResponseEntity.ok(new MessageResponse("Further instructions to reset password are sent to mail!"));
        }catch (Exception e){
            return (ResponseEntity<?>) ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }

    }
}
