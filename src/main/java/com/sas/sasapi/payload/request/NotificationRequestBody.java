package com.sas.sasapi.payload.request;

import com.fasterxml.jackson.annotation.*;
import com.google.api.client.json.Json;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "data",
        "notification",
        "to"
})
@Builder
public class NotificationRequestBody {

    @JsonProperty("success")
    @Getter
    @JsonPropertyOrder({
            "click_action"
    })
    private Json data;

    @JsonProperty("challenge_ts")
    @JsonPropertyOrder({
            "title",
            "body",
            "click_action",
            "icon"
    })
    private Json notification;

    @JsonProperty("to")
    private String to;
}