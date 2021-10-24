package com.sas.sasapi.payload.response;

import com.sas.sasapi.model.Session;
import com.sas.sasapi.model.User;
import lombok.*;

import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor

public class GetSessionDetailsResponse {
    @Getter
    @Setter
    List<User> users;
    @Getter
    @Setter
    List<User> attendedUsers;
}
