package com.sas.sasapi.payload.response;

import com.sas.sasapi.model.Session;
import lombok.*;

import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UniqueDetailsResponse {
    @Getter
    @Setter
    private List<Long> academicYear;
    @Getter
    @Setter
    private List<Long> semester;
    @Getter
    @Setter
    private List<String> courseCode;
    @Getter
    @Setter
    private List<String> batch;

}
