package com.sas.sasapi.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class EmailRequest {

    @NotBlank
    @Getter
    @Setter
    @Email
    private String email;
}
