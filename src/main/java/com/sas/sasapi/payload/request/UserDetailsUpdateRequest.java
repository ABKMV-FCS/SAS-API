package com.sas.sasapi.payload.request;

import lombok.Getter;
import lombok.Setter;

public class UserDetailsUpdateRequest {
    @Setter
    @Getter
    private Long userId;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String address;
    @Getter
    @Setter
    private String coordinates;
}
