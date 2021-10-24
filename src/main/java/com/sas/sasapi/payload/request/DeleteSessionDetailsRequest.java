package com.sas.sasapi.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class DeleteSessionDetailsRequest {
    @NotBlank
    @Getter
    @Setter
    private Long sessionId;
}
