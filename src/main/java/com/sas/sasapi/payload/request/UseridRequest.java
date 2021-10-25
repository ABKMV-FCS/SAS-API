package com.sas.sasapi.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class UseridRequest {
    @Getter
    @Setter
    @NotBlank
    private Long userId;

}
