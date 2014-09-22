package org.piscatory.onebutton.net;

import android.net.http.AndroidHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.piscatory.onebutton.Constants;
import org.piscatory.onebutton.service.VoteService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by kc on 9/15/14.
 */
public class IPGetter extends Thread {
    private VoteService service;

    public IPGetter(VoteService service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();

        String result = "";
        AndroidHttpClient client = AndroidHttpClient.newInstance(Constants.UAC);
        HttpGet get = new HttpGet("http://www.telize.com/ip");
        HttpResponse response = null;
        try {
            if (service.getIsConnected())
                response = client.execute(get);
            else
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (response == null) {
            client.close();
            return;
        }

        BufferedReader in = null;
        HttpEntity en = response.getEntity();
        if (en == null) {
            client.close();
            return;
        }
        try {
            in = new BufferedReader(new InputStreamReader(en.getContent()));
        } catch (Exception e) {
            client.close();
            e.printStackTrace();

        }
        try {
            result = in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        client.close();

        if (!result.equals("")) {
            service.updateIP(result);
        }


    }
}
