package com.sas.sasapi.service;

import com.sas.sasapi.payload.response.GoogleResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Duration;
import java.util.regex.Pattern;

@Service
public class CaptchaService {

    private final RestTemplate restTemplate;


    @Value("${sas.app.captchaSecret}")
    private String captchaSecret;



    private static Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    private boolean responseSanityCheck(String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }

    public CaptchaService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(500)).setReadTimeout(Duration.ofSeconds(500)).build();
    }

    public void verifyCaptcha(String response) {

        if(!responseSanityCheck(response)) {
            throw new RuntimeException("reCaptcha response contains invalid characters");
        }



        URI verifyUri = URI.create(String.format("https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s", captchaSecret, response));
        GoogleResponse googleResponse=this.restTemplate.getForObject(verifyUri, GoogleResponse.class);

        if(!googleResponse.isSuccess()) {
            throw new RuntimeException("reCaptcha was not successfully validated");
        }

    }
}