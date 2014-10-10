package org.piscatory.onebutton.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.view.VelocityTrackerCompat;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.piscatory.onebutton.Constants;
import org.piscatory.onebutton.R;
import org.piscatory.onebutton.Utils;
import org.piscatory.onebutton.service.VoteService;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class VoteActivity extends Activity {
    // UI elements
    private TextView textViewCount;
    private TextView textViewGesture;

    private IntentFilter voteActionFilter = new IntentFilter(Constants.VOTE_STATUS_UPDATE_ACTION);
    private VoteStatusReceiver voteStatusReceiver = new VoteStatusReceiver();
    private IntentFilter proxyActionFilter = new IntentFilter(Constants.PROXY);
    private ProxyChangeReceiver proxyChangeReceiver = new ProxyChangeReceiver();

    private String currentLocation;
    private String currentVoteCount;
    private String currentSpeed;

    private boolean changingSpeed = false;

    private VelocityTracker mVelocityTracker = null;


    private Intent speedChangeIntent;

    NumberFormat formatter = new DecimalFormat("#0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_vote);
        textViewCount = (TextView) this.findViewById(R.id.textViewCount);
        textViewGesture = (TextView) this.findViewById(R.id.textViewGesture);
        if (!Utils.isMyServiceRunning(VoteService.class, this)) {
            this.startService(new Intent(this, VoteService.class));
        } else {
            textViewCount.setText(""+VoteService.getService().getVoteCount());
        }
        SharedPreferences sp = getSharedPreferences("VOTE", 0);
        currentSpeed = sp.getInt("DELAY", 60000) + "";
        textViewGesture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int index = motionEvent.getActionIndex();
                int action = motionEvent.getActionMasked();
                int pointerId = motionEvent.getPointerId(index);

                switch (action) {
                    case (MotionEvent.ACTION_DOWN):
                        changingSpeed = true;
                        textViewCount.setText(currentSpeed);
                        if (mVelocityTracker == null) {
                            // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                            mVelocityTracker = VelocityTracker.obtain();
                        } else {
                            // Reset the velocity tracker back to its initial state.
                            mVelocityTracker.clear();
                        }
                        // Add a user's movement to the tracker.
                        mVelocityTracker.addMovement(motionEvent);
                        return true;
                    case (MotionEvent.ACTION_MOVE):
                        mVelocityTracker.addMovement(motionEvent);
                        // When you want to determine the velocity, call
                        // computeCurrentVelocity(). Then call getXVelocity()
                        // and getYVelocity() to retrieve the velocity for each pointer ID.
                        mVelocityTracker.computeCurrentVelocity(100);
                        // Log velocity of pixels per second
                        // Best practice to use VelocityTrackerCompat where possible.
                        float tmp = Float.parseFloat(currentSpeed);
                        tmp -= mVelocityTracker.getYVelocity(pointerId);
                        if (tmp < 0) {
                            tmp = 0;
                        }
                        if (tmp > 100000) {
                            tmp = 100000;
                        }
                        currentSpeed = "" + formatter.format(tmp);
                        textViewCount.setText(currentSpeed);
                        return true;
                    case (MotionEvent.ACTION_UP):
                        changingSpeed = false;
                        textViewCount.setText(currentVoteCount);
                        speedChangeIntent = new Intent(Constants.SPEED_CHANGE)
                                .putExtra("SPEED", Integer.parseInt(currentSpeed));
                        sendBroadcast(speedChangeIntent);

                        return true;
                    default:
                        return onTouchEvent(motionEvent);
                }
            }
        });
    }


    public void clickNumber(View view) {
        if (currentLocation != null && !"".equals(currentLocation)) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentLocation));
            startActivity(browserIntent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(voteStatusReceiver, voteActionFilter);
        this.registerReceiver(proxyChangeReceiver, proxyActionFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(voteStatusReceiver);
        this.unregisterReceiver(proxyChangeReceiver);
    }

    private class ProxyChangeReceiver extends  BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
//            textViewGesture.setText(intent.getExtras().getString("PROXY"));
        }
    }

    private class VoteStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            currentLocation = intent.getExtras().getString("LOCATION");
            currentVoteCount = "" + intent.getExtras().getInt("VOTE_COUNT");
            currentSpeed = ""+ intent.getExtras().getInt("SPEED");
            if (!changingSpeed)
                textViewCount.setText(currentVoteCount);
        }
    }


}
