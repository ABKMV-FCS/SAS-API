package com.sas.sasapi.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.Date;


@ToString
public class GetSessionDetailsRequest {
    @NotBlank
    @Getter
    @Setter
    private Long academicYear;
    @NotBlank
    @Getter
    @Setter
    private Long semester;
    @NotBlank
    @Getter
    @Setter
    private String courseCode;
    @NotBlank
    @Getter
    @Setter
    private String batch;
    @Getter
    @Setter
    private Date fromDateTime;
    @Getter
    @Setter
    private Date toDateTime;

}
