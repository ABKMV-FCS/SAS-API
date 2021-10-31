package com.sas.sasapi.payload.request;

import com.sas.sasapi.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class UpdateSessionDetailsRequest {

    @NotBlank
    @Getter
    @Setter
    private Long sessionId;

    @NotBlank
    @Getter
    @Setter
    private List<User> Users;
}
