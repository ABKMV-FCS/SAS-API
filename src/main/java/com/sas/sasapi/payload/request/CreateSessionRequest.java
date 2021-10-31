package com.sas.sasapi.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;


public class CreateSessionRequest {
    @NotBlank
    @Getter
    @Setter
    private Date fromDateTime;
    @NotBlank
    @Getter
    @Setter
    private Date toDateTime;
    @NotBlank
    @Getter
    @Setter
    private String courseCode;
    @NotBlank
    @Getter
    @Setter
    private Long year;
    @NotBlank
    @Getter
    @Setter
    private Long semester;
    @NotBlank
    @Getter
    @Setter
    private String batch;

}
