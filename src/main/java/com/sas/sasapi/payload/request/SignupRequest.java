package com.sas.sasapi.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import javax.validation.constraints.*;


public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    @Getter
    @Setter
    private String name;

    @NotBlank
    @Size(min = 3, max = 20)
    @Getter
    @Setter
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    @Getter
    @Setter
    private String password;

    @NotBlank
    @Size(max = 50)
    @Email
    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private Set<String> role;

    @NotBlank
    @Size(max = 400)
    @Getter
    @Setter
    private String address;
}