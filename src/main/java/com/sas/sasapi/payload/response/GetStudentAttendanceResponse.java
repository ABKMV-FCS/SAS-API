package com.sas.sasapi.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStudentAttendanceResponse {
    List<String> cc;
    List<Long> sessions;
    List<Integer> attendedSessions;
}
