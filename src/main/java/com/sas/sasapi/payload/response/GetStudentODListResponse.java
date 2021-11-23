package com.sas.sasapi.payload.response;

import lombok.Builder;

import java.util.Date;
@Builder
public class GetStudentODListResponse {
    String eventName;
    Date fromDate;
    Date toDate;
    String name;
    String status;
}
