package com.sas.sasapi.service;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class PushNotificationService {
    @Value("${sas.app.server_key}")
    private String server_key;

    public void sendNotification(String fcmToken,String newStatus) throws JSONException, IOException {
        JSONObject json = new JSONObject();

        JSONObject data = new JSONObject();
        data.put("click_action","");

        json.putOpt("data", data);

        JSONObject notification = new JSONObject();
        notification.put("title","OD Status Updated");
        notification.put("body","Your OD is " + newStatus);
        notification.put("click_action","");
        notification.put("icon","");

        json.putOpt("notification",notification);
        json.putOpt("to",fcmToken);

        JSONObject header = new JSONObject();
        JSONObject headers = new JSONObject();
        headers.put("Authorization", "Bearer " + server_key);
        header.putOpt("headers",headers);


        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpPost request = new HttpPost("https://fcm.googleapis.com/fcm/send");
            System.out.println(json.toString());
            StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            httpClient.execute(request);
            System.out.println("finished");
// handle response here...
        } catch (Exception ex) {
            System.out.println("ex.getMessage() = " + ex.getMessage());
        } finally {
            httpClient.close();
        }
    }
}
