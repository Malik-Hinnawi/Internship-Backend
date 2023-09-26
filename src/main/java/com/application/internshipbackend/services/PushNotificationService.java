package com.application.internshipbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class PushNotificationService {
    public static final String REST_API_KEY = "OGIyMjA0M2ItMTk0Yy00ZTlhLTk5MTAtYjkyOTYxMWY0NjA4";
    public static final String APP_ID = "7e8a55e4-4b00-49a2-9adf-57644312c6f7";


    public void sendMessageToUser(
            String message, Integer userId) {
        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", REST_API_KEY);
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                    +   "\"app_id\": \""+ APP_ID +"\","
                    +   "\"include_player_ids\": [\""+ userId +"\"],"
                    +   "\"data\": {\"foo\": \"bar\"},"
                    +   "\"contents\": {\"message\": \""+ message +"\"}"
                    + "}";



            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();

        } catch(Throwable t) {
            t.printStackTrace();
        }
    }
}
