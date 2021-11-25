package com.sas.sasapi.payload.request;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TeamAttendanceUpdateRequest {
        Long academicYear;
        Long semester;
        String courseCode;
        String batch;
        String fromDateTime;
        String toDateTime;
        Long threshold;
        List<List<String>> csv;
}
