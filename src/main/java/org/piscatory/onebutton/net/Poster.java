package org.piscatory.onebutton.net;

import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.preference.PreferenceManager;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.piscatory.onebutton.Constants;
import org.piscatory.onebutton.service.VoteService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Poster extends Thread {
    private AndroidHttpClient client;
    private HttpPost post;
    private HttpResponse response;
    private VoteService voteService;
    private int agentType;

    public Poster(VoteService service, int agentType) {
        this.voteService = service;
        this.agentType = agentType;
    }

    @Override
    public void run() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(voteService);
        int sleepTime = 1000;
        int checkValidCount = Integer.parseInt(sharedPref.getString("valid", "100"));
        boolean smartWater = sharedPref.getBoolean("smart_water", false);
        int delta = Integer.parseInt(sharedPref.getString("delta", "1000"));
        int threshold = Integer.parseInt(sharedPref.getString("threshold", "30"));
        switch (agentType) {
            case 1:
                client = AndroidHttpClient.newInstance(Constants.UAA);
                post = new HttpPost(Constants.URLA);
                sleepTime = Integer.parseInt(sharedPref.getString("delay_android", "1000"));
                break;
            case 2:
                client = AndroidHttpClient.newInstance(Constants.UAI);
                post = new HttpPost(Constants.URLI);
                sleepTime = Integer.parseInt(sharedPref.getString("delay_ios", "1000"));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                client = AndroidHttpClient.newInstance(Constants.UAC);
                post = new HttpPost(Constants.URLC);
                break;
            case 3:
                client = AndroidHttpClient.newInstance(Constants.UAIE);
                post = new HttpPost(Constants.URLC);
                sleepTime = Integer.parseInt(sharedPref.getString("delay", "1000"));
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            default:
                sleepTime = 1000;
        }
        int runCount = 0;
        int runThreshold = 0;
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        nameValuePair.add(new BasicNameValuePair("ema14-1-china", "ema14-gem"));
        nameValuePair.add(new BasicNameValuePair("ema14-1-china_l", "CN"));
        switch (agentType) {
            case 3:
                nameValuePair.add(new BasicNameValuePair("ema2014-vote_id", UUID.randomUUID().toString() + "_ema14-1-china"));
                post.setHeader("Origin", "http://cn.mtvema.com");
                post.setHeader("Pragma", "no-cache");
                post.setHeader("Referer", "http://cn.mtvema.com/vote");
                post.setHeader("Accept", "*/*");
                post.setHeader("Accept-Encoding", "gzip,deflate");
                post.setHeader("Accept-Language", "zh,en;q=0.8,zh-CN;q=0.6,en-US;q=0.4");
                post.setHeader("Cache-Control", "no-cache");
                post.setHeader("Host", "funnel.mtvnservices.com");
                break;
            case 1:
                post.setHeader("Accept-Encoding", "gzip");
                break;
            case 2:
                post.setHeader("Accept", "*/*");
                post.setHeader("Accept-Encoding", "gzip,deflate");
                break;
            case 4:
                nameValuePair.add(new BasicNameValuePair("ema2014-vote_id", UUID.randomUUID().toString() + "_ema14-1-china"));
                post.setHeader("Origin", "http://cn.mtvema.com");
                post.setHeader("Pragma", "no-cache");
                post.setHeader("Referer", "http://cn.mtvema.com/vote");
                post.setHeader("Accept", "*/*");
                post.setHeader("Accept-Encoding", "gzip,deflate");
                post.setHeader("Accept-Language", "zh,en;q=0.8,zh-CN;q=0.6,en-US;q=0.4");
                post.setHeader("Cache-Control", "no-cache");
                post.setHeader("Host", "funnel.mtvnservices.com");
        }
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            client.close();
            return;
        }


        while (voteService.isRunning()) {

            try {
                if (voteService.getIsConnected())
                    response = client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
                client.close();
                break;
            }
//            if (response == null) {
//                continue;
//            }
//            InputStream is = null;
//            try {
//                is = Utils.decompressStream(response.getEntity().getContent());
//            } catch (Exception e) {
//                e.printStackTrace();
//                client.close();
//                break;
//            }
            boolean successThisTime = true;
            Header header = null;
            if (response != null)
                header = response.getFirstHeader("Location");
            if (runCount % checkValidCount == 0 && header != null) {
                successThisTime = VoteValidate.validate(header.getValue());
            }
            runCount++;

            if (smartWater) {
                if (successThisTime) {
                    if (runThreshold == threshold) {
                        runThreshold = 0;
                        sleepTime -= delta;
                    } else {
                        runThreshold++;
                    }
                } else {
                    sleepTime += delta;
                }
            }
            voteService.updateDelay(sleepTime, agentType);

//
//            byte[] buffer = new byte[1024];
//            int len;
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            if (is != null) {
//                try {
//                    while ((len = is.read(buffer)) != -1) {
//                        baos.write(buffer, 0, len);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                continue;
//            }
//            String str = baos.toString();
//
//            try {
//                baos.close();
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            if (header != null)
                voteService.update(header.getValue(), successThisTime, agentType);

            try {
                sleep(sleepTime);
            } catch (InterruptedException e) {
                client.close();
                break;
            }
            if (this.isInterrupted()) {
                client.close();
                break;
            }
            if (agentType == 3 || agentType == 4) {
                nameValuePair.remove(nameValuePair.size() - 1);
                nameValuePair.add(new BasicNameValuePair("ema2014-vote_id", UUID.randomUUID().toString() + "_ema14-1-china"));
                try {
                    post.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }

    }


}
