package com.sas.sasapi.payload.response;

import com.sas.sasapi.model.Session;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;

@Data
@Builder
public class GetStudentCourseWiseAttendanceResponse {
    HashSet<Session> coursesAttended;
    HashSet<Session> coursesNotAttended;
}
