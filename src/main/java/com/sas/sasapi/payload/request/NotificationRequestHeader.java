package com.sas.sasapi.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.api.client.json.Json;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "headers"
})
public class NotificationRequestHeader {
    @JsonProperty("headers")
    @Getter
    @JsonPropertyOrder({
            "Authorization"
    })
    private Json headers;
}
