package com.sas.sasapi.payload.request;

import com.sas.sasapi.model.User;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;
@Data
public class UpdateODEventRequest {
    private Date fromDate;

    private Date toDate;

    private String eventName;
    private String description;

}
