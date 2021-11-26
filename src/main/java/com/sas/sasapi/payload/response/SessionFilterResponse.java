package com.sas.sasapi.payload.response;

import com.sas.sasapi.model.Session;
import lombok.*;

import java.util.List;
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SessionFilterResponse {
    @Getter
    @Setter
    private List<Double> Attendance;
    @Getter
    @Setter
    private List<Session> Sessions;


}
