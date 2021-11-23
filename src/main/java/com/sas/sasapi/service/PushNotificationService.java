package com.sas.sasapi.service;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.sas.sasapi.payload.request.NotificationRequestBody;
import com.sas.sasapi.payload.response.Post;
import io.jsonwebtoken.lang.Assert;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class PushNotificationService {
    @Value("${sas.app.server_key}")
    private String server_key;

    public void sendNotification(String fcmToken,String newStatus) throws JSONException, IOException {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "https://fcm.googleapis.com/fcm/send";
//        // create headers
//        org.springframework.http.HttpHeaders headers = new HttpHeaders();
//        // set `content-type` header
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        // set `accept` header
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//        headers.setBearerAuth("AAAA8zNWBaQ:APA91bHW3IqC8fo3viPJWWC6ETrxdUTVPVQ0g_artCu_GWnf6eXG8Lx_isF0sKDuN0VFF4I9mqBwOsXqkmF5qs-2e3Jj0T2eMWe3eJtHevzot3jIJMWkpS4NRvL3FXCHrcjECTP63w7L");
//        // create a map for post parameters
//        JSONObject request = new JSONObject();
//        JSONObject data = new JSONObject();
//        data.put("click_action","http://${config.UIPublicIP}/admin");
//        request.putOpt("data", data);
//        JSONObject notification = new JSONObject();
//        notification.put("title", "IRS Issue Reported");
//        notification.put("body", "New Issue Reported");
//        notification.put("click_action","http://${config.UIPublicIP}/admin");
//        notification.put("icon", "http://${config.UIPublicIP}/favicon.ico");
//        request.put("notification", notification);
//        System.out.println(fcmToken);
//        request.put("to","edj2K-UqJ0ABfgYQljlsSX:APA91bHBvEgB1-cfo4hhign-S7aVyIwEq76qZ-3XapFHdslcKxp5Tt0GFFgpwqGtoQYypWeDBvCbYas1-zQPA5EOhf86AJ_llGWVfq95Xul58wQpBP9B5sbNFdbjyls3lgUlEfKeSzQN");
//        MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
//        jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        restTemplate.getMessageConverters().add(jsonHttpMessageConverter);
//
//        // build the request
//        System.out.println(request.toString());
//        System.out.println("headers = " + headers);
//        org.springframework.http.HttpEntity<JSONObject> entity = new HttpEntity<>(request, headers);
//
//        // send POST request
//        ResponseEntity<Post> response = restTemplate.postForEntity(url, entity, Post.class);
//        System.out.println("response = " + response);
//
//





        JSONObject json = new JSONObject();
        JSONObject notification=new JSONObject();
        notification.put("icon","http://${config.UIPublicIP}/favicon.ico");
        notification.put("title","http://${config.UIPublicIP}/favicon.ico");
        notification.put("body","http://${config.UIPublicIP}/favicon.ico");
        notification.put("click_action","http://${config.UIPublicIP}/favicon.ico");



        json.put("notification", notification);
        json.put("to", "edj2K-UqJ0ABfgYQljlsSX:APA91bHBvEgB1-cfo4hhign-S7aVyIwEq76qZ-3XapFHdslcKxp5Tt0GFFgpwqGtoQYypWeDBvCbYas1-zQPA5EOhf86AJ_llGWVfq95Xul58wQpBP9B5sbNFdbjyls3lgUlEfKeSzQN");

        HttpPost method = null;
        try {
            method = new HttpPost(new URI("https://fcm.googleapis.com/fcm/send"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        method.setHeader("Content-Type", "application/json");
        method.setHeader("Authorization", "Bearer "+server_key);
        System.out.println("server_key = " + server_key);
        method.setEntity(new StringEntity(json.toString(), ContentType.APPLICATION_JSON));
//        HttpParams params=message.getParams();
//        HttpConnectionParams.setConnectionTimeout(params, timeout);
//        HttpConnectionParams.setSoTimeout(params, timeout);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(method);
        System.out.println("response = " + response);
        System.out.println("method = " + method);
        InputStream in = response.getEntity().getContent();
        System.out.println("in = " + in);

    }
}
