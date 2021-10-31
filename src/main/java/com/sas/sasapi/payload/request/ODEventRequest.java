package com.sas.sasapi.payload.request;

import com.sas.sasapi.model.ODEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public class ODEventRequest {
    @Getter
    @Setter
    private ODEvent odEvent;
    @Getter
    @Setter
    private List<String> usernames;
}
