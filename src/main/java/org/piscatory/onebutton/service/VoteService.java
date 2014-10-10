package org.piscatory.onebutton.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpHost;
import org.piscatory.onebutton.Constants;
import org.piscatory.onebutton.net.Poster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class VoteService extends IntentService {
    private static VoteService self;

    public static VoteService getService() {
        return self;
    }


    private boolean keepRunning = true;
    private SpeedChangeReceiver speedChangeReceiver = new SpeedChangeReceiver();


    private int voteCount;

    public int getVoteCount() {
        return this.voteCount;
    }

    private long channelLastTimestamp[] = new long[3];


//    private int currentAgent;

    private int delayMilliseconds;

//    private ArrayList<String> proxyList = new ArrayList<String>();


    public VoteService() {
        super("VoteService");
        self = this;

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
//        this.delayMilliseconds = intent.getExtras().getInt(Constants.SPEED, 16000);
//        this.voteCount = intent.getExtras().getInt(Constants.COUNT, 0);
//        registerReceiver(speedChangeReceiver, new IntentFilter(Constants.SPEED_CHANGE));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(speedChangeReceiver);

        this.keepRunning = false;

        SharedPreferences sharedPref = getSharedPreferences("VOTE", 0);
        sharedPref.edit().putInt("DELAY", delayMilliseconds).apply();
        sharedPref.edit().putInt("VOTE_COUNT", voteCount).apply();


//        this.startService(new Intent(this, VoteService.class).putExtra(Constants.COUNT, voteCount).putExtra(Constants.SPEED, delayMilliseconds));
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return START_STICKY;
//
//    }


    @Override
    protected void onHandleIntent(Intent intent) {
//        // get proxy list
//        File sdcard = Environment.getExternalStorageDirectory();
//        File file = new File(sdcard, "OneButton/proxy_list.txt");
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            String line;
//
//            while ((line = br.readLine()) != null) {
//                Long[] numbers = new Long[3];
//                for (int i = 0; i < 3; i++) {
//                    numbers[i] = new Long(0);
//                }
//                lastTimestampList.add(numbers);
//
//                proxyList.add(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        String location;


        SharedPreferences sharedPref = getSharedPreferences("VOTE", 0);
        this.voteCount = sharedPref.getInt("VOTE_COUNT", 0);
        this.delayMilliseconds = sharedPref.getInt("DELAY", 60000);
        keepRunning = true;

        int currentAgent = 0;
        long defferSleepMilliseconds;
        while (keepRunning) {
//            for (int currentAgent = 0; currentAgent < 2; currentAgent++) {
            try {
                location = Poster.post(currentAgent, null);
            } catch (Exception e) {
                e.printStackTrace();
                Poster.getClient().close();
                continue;
            }
            voteCount++;
            Intent uiIntent = new Intent(Constants.VOTE_STATUS_UPDATE_ACTION)
                    .putExtra("VOTE_COUNT", voteCount)
                    .putExtra("LOCATION", location)
                    .putExtra("SPEED", delayMilliseconds);
            sendBroadcast(uiIntent);

            channelLastTimestamp[currentAgent] = System.currentTimeMillis();
            currentAgent = ++currentAgent % 2;
            defferSleepMilliseconds = (delayMilliseconds - (System.currentTimeMillis() - channelLastTimestamp[(currentAgent)]));


            defferSleepMilliseconds = defferSleepMilliseconds < 0 ? 0 : defferSleepMilliseconds;

            Log.i("xxx", defferSleepMilliseconds + " sleep");

            try {
                Thread.sleep(defferSleepMilliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            }


        }
    }

    private class SpeedChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            delayMilliseconds = intent.getExtras().getInt("SPEED");
        }
    }
}
//        serviceStartTimestamp = System.currentTimeMillis();
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//
//        int changeIPCount = Integer.parseInt(sharedPref.getString("changeIPCount", "100"));
//        registerReceiver(networkChangeReceiver, intentFilter);
//        SharedPreferences sp = getSharedPreferences(Constants.VOTE_COUNT_TOTAL_KEY, 0);
//        voteCountTotal = sp.getInt(Constants.VOTE_COUNT_TOTAL_KEY, 0);
//        voteSuccessTotal = sp.getInt(Constants.VOTE_SUCCESS_TOTAL_KEY, 0);
//
//        int threadNo = 3;
//        serviceStart(threadNo);
//        boolean inService = true;
//        postLastActiveTimestamp = System.currentTimeMillis();
//        while (keepRunning) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if (System.currentTimeMillis() - this.postLastActiveTimestamp > 50000) {
//                if (!keepRunning)
//                    break;
//                if (!isNetworkConnected)
//                    continue;
//                broadcastStatus("连接超时, 服务重启");
//                service.shutdownNow();
//                while (true) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if (service.isTerminated()) {
//                        serviceStart(threadNo);
//                        inService = true;
//                        break;
//                    }
//                }
//            }
//            if (voteSuccess % changeIPCount == 0 && voteSuccess != 0 && inService) {
//                inService = false;
//                if (!isWiFi) {
//
//                    broadcastStatus("服务关闭");
//                    service.shutdownNow();
//                    while (true) {
//                        if (service.isTerminated()) {
//                            broadcastStatus("更换IP");
//
//                            Utils.setMobileDataStatus(this, false);
//                            updateNetworkStatus();
//                            while (isNetworkConnected) {
//                                try {
//                                    Thread.sleep(300);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                updateNetworkStatus();
//                            }
//                            broadcastStatus("已断开");
//
//
//                            Utils.setMobileDataStatus(this, true);
//                            updateNetworkStatus();
//                            while (!isNetworkConnected) {
//                                try {
//                                    Thread.sleep(300);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                updateNetworkStatus();
//                            }
//                            broadcastStatus("已连接");
//                            postLastActiveTimestamp = System.currentTimeMillis();
//                            broadcastStatus("服务开始");
//                            serviceStart(threadNo);
//                            inService = true;
//                            broadcastStatus("获取外网IP");
//                            IPGetter ipGetter = new IPGetter(this);
//                            ipGetter.start();
//                            break;
//                        }
//                        try {
//                            Thread.sleep(500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//
//                } else {
//                    inService = true;
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//                    builder.setContentTitle("请更换IP");
//                    builder.setContentText("设定的票数已投完, 请手动更新IP.");
//                    builder.setSmallIcon(R.drawable.vote);
//                    Notification notification = builder.build();
//                    NotificationManager mNotificationManager =
//                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                    mNotificationManager.notify(0, notification);
//                }
//            }
//
//
//            // update delay
//            r_sleepTime -= 1000;
//            r_sleepTime_Android -= 1000;
//            r_sleepTime_iOS -= 1000;
//
//            if (r_sleepTime < 0) r_sleepTime = 0;
//            if (r_sleepTime_Android < 0) r_sleepTime_Android = 0;
//            if (r_sleepTime_iOS < 0) r_sleepTime_iOS = 0;
//            Intent intent1 = new Intent(Constants.VOTE_TIME_CHANGE_ACTION)
//                    .putExtra("AndroidRemainTime", "" + r_sleepTime_Android)
//                    .putExtra("iOSRemainTime", "" + r_sleepTime_iOS)
//                    .putExtra("RemainTime", "" + r_sleepTime);
//            sendBroadcast(intent1);
//
//        }
//
//        broadcastStatus("服务停止");
//
//    }
//
//    private void serviceStart(int threadNo) {
//        service = Executors.newFixedThreadPool(threadNo);
//        for (int i = 1; i < threadNo + 1; i++) {
//            service.execute(new Poster(this, i));
//        }
//    }
//
//
//    public void update(String result, boolean success, int agentType) {
//        switch (agentType) {
//            case 1:
//                this.r_sleepTime_Android = sleepTime_Android;
//                this.location_android = result;
//                break;
//            case 2:
//                this.r_sleepTime_iOS = sleepTime_iOS;
//                this.location_iOS = result;
//                break;
//            case 3:
//                this.r_sleepTime = sleepTime;
//                this.location = result;
//                break;
//        }
//        postLastActiveTimestamp = System.currentTimeMillis();
//        double hour = 0.0;
//        if ((System.currentTimeMillis() - serviceStartTimestamp) != 0) {
//            hour = ((double) (System.currentTimeMillis() - serviceStartTimestamp) / 3600000);
//        }
//        Intent intent;
//        if (result.contains("entries")) {
//            voteCount++;
//            voteCountTotal++;
//            if (success) {
//                voteSuccessTotal += 1;
//                voteSuccess += 1;
//            }
//            intent = new Intent(Constants.VOTE_RESULT_ACTION);
//            intent.putExtra(Constants.VOTE_COUNT_KEY, voteCount);
//            intent.putExtra(Constants.VOTE_COUNT_TOTAL_KEY, voteCountTotal);
//            intent.putExtra(Constants.VOTE_SUCCESS_TOTAL_KEY, "" + voteSuccessTotal);
//            intent.putExtra(Constants.VOTE_SUCCESS_KEY, "" + voteSuccess);
//            intent.putExtra(Constants.VOTE_SUCCESS_RATE, (voteCount == 0 ? 0.0 : formatter.format(((double) voteSuccess / voteCount) * 100)) + "%");
//            intent.putExtra(Constants.VOTE_SUCCESS_SPEED, (hour == 0 ? 0 : formatter.format(((double) voteSuccess / hour))) + "/小时");
//            intent.putExtra("ANDROID_LOCATION", location_android);
//            intent.putExtra("iOS_LOCATION", location_iOS);
//            intent.putExtra("LOCATION", location);
//
//            sendBroadcast(intent);
//            broadcastNetworkStatus();
//        }
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        this.keepRunning = false;
//        service.shutdownNow();
//        SharedPreferences sp = getSharedPreferences(Constants.VOTE_COUNT_TOTAL_KEY, 0);
//        sp.edit().putInt(Constants.VOTE_COUNT_TOTAL_KEY, voteCountTotal).apply();
//        sp.edit().putInt(Constants.VOTE_SUCCESS_TOTAL_KEY, voteSuccessTotal).apply();
//
//        unregisterReceiver(networkChangeReceiver);
//        broadcastStatus("投票服务结束");
//    }
//
//    private void updateNetworkStatus() {
//        ConnectivityManager cm =
//                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        isNetworkConnected = activeNetwork != null &&
//                activeNetwork.isConnectedOrConnecting();
//        isWiFi = activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
//        IP = "";
//        if (isWiFi) {
//            broadcastStatus("获取外网IP");
//            IPGetter ipGetter = new IPGetter(this);
//            ipGetter.start();
//
//        }
//        broadcastNetworkStatus();
//
//
//    }
//
//    // update for logging
//    private void broadcastStatus(String status) {
//        Intent intent = new Intent(Constants.VOTE_STATUS_UPDATE_ACTION)
//                .putExtra(Constants.VOTE_STATUS_KEY, status);
//        sendBroadcast(intent);
//    }
//
//    private void broadcastNetworkStatus() {
//        Intent reIntent = new Intent(Constants.NETWORK_STATUS_UPDATE_ACTION)
//                .putExtra(Constants.NETWORK_IP_KEY, IP);
//        sendBroadcast(reIntent);
//    }
//
//    public void updateIP(String result) {
//        this.IP = result;
//        broadcastNetworkStatus();
//    }
//
//    public boolean isRunning() {
//        return keepRunning;
//    }
//
//
//    public void updateDelay(int sleepTime, int agentType) {
//        switch (agentType) {
//            case 1:
//                this.sleepTime_Android = sleepTime;
//                break;
//            case 2:
//                this.sleepTime_iOS = sleepTime;
//                break;
//            case 3:
//                this.sleepTime = sleepTime;
//                break;
//        }
//    }
//
//
//    private class NetworkChangeReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            updateNetworkStatus();
//
//        }
//    }


//}
