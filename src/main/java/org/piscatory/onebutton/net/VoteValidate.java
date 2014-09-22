package org.piscatory.onebutton.net;

import android.net.http.AndroidHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.piscatory.onebutton.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class VoteValidate {
    public static boolean validate(String location) {

        String result = "";
        AndroidHttpClient client = AndroidHttpClient.newInstance(Constants.UAC);
        HttpGet get = new HttpGet(location);
        HttpResponse response = null;
        try {
            response = client.execute(get);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                client.close();
            }
            client.close();
        }
        if (response == null) {
            client.close();
            return false;
        }

        BufferedReader in;
        HttpEntity en = response.getEntity();
        if (en == null) {
            client.close();
            return false;

        }
        try {
            in = new BufferedReader(new InputStreamReader(en.getContent()));
        } catch (Exception e) {
            client.close();
            e.printStackTrace();
            return false;

        }
        try {
            result = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.close();

        return !result.equals("") && result.contains("ema14-gem");
    }


}

