package org.piscatory.onebutton.net;

import android.net.http.AndroidHttpClient;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.message.BasicNameValuePair;
import org.piscatory.onebutton.Constants;

import java.util.ArrayList;
import java.util.List;

public class Poster {

    private static AndroidHttpClient client;
    private static List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    private static HttpPost post = new HttpPost();

    static {
        nameValuePair.add(new BasicNameValuePair("ema14-1-china", "ema14-gem"));
        nameValuePair.add(new BasicNameValuePair("ema14-1-china_l", "CN"));
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    }

    public static AndroidHttpClient getClient() {
        return client;
    }

    public static String post(int agentType, HttpHost proxyHost) throws Exception {


        String url, userAgent;
        switch (agentType) {
            case 0:
                url = Constants.URLA;
                userAgent = Constants.UAA;
                break;
            case 1:
                url = Constants.URLI;
                userAgent = Constants.UAI;
                break;
            case 2:
                url = Constants.URLC;
                userAgent = Constants.UAIE;
                break;

            default:
                url = Constants.URLC;
                userAgent = Constants.UAC;
        }
        client = AndroidHttpClient.newInstance(userAgent);
        //set proxy
        if (proxyHost != null) {
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHost);
        } else {
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, null);
        }

        // set post
        post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(nameValuePair));
        Log.i("xxx", "start vote");
        HttpResponse response = client.execute(post);
        Header header = response.getFirstHeader("Location");
        client.close();
        return header.getValue();
    }
}
