package org.piscatory.onebutton.view;


import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.piscatory.onebutton.Constants;
import org.piscatory.onebutton.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment {


    // VoteBroadcastReceiver
    private IntentFilter mStatusIntentFilter = new IntentFilter();
    private ResponseReceiver receiver = new ResponseReceiver();

    // NetworkBroadcastReceiver
    private IntentFilter networkUpdateFilter = new IntentFilter();
    private NetworkStatusReceiver networkStatusReceiver = new NetworkStatusReceiver();

    private TextView textViewVoteCount;
    private TextView textViewIP;
    private TextView textViewResult;
    private TextView textViewResultAndroid;
    private TextView textViewResultIOS;
    private TextView textViewVoteCountTotal;
    private TextView textViewDelay;
    private TextView textViewDelayAndroid;
    private TextView textViewDelayIOS;
    private TextView textViewSuccessCount;
    private TextView textViewSuccessCountTotal;
    private TextView textViewSuccessRate;
    private TextView textViewSpeed;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mStatusIntentFilter.addAction(Constants.VOTE_RESULT_ACTION);
        mStatusIntentFilter.addAction(Constants.VOTE_TIME_CHANGE_ACTION);
        networkUpdateFilter.addAction(Constants.NETWORK_STATUS_UPDATE_ACTION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity rootView = this.getActivity();
        textViewVoteCount = (TextView) rootView.findViewById(R.id.textViewVoteCount);

        textViewIP = (TextView) rootView.findViewById(R.id.textVieIP);
        textViewResult = (TextView) rootView.findViewById(R.id.textViewResult);
        textViewResultAndroid = (TextView) rootView.findViewById(R.id.textViewResult_Android);
        textViewResultIOS = (TextView) rootView.findViewById(R.id.textViewResult_iOS);
        textViewVoteCountTotal = (TextView) rootView.findViewById(R.id.textViewVoteCountTotal);
        textViewDelay = (TextView)rootView.findViewById(R.id.textViewDelay);
        textViewDelayAndroid = (TextView)rootView.findViewById(R.id.textViewDelay_android);
        textViewDelayIOS = (TextView)rootView.findViewById(R.id.textViewDelay_iOS);
        textViewSuccessCount = (TextView)rootView.findViewById(R.id.textViewSuccessCount);
        textViewSuccessCountTotal = (TextView)rootView.findViewById(R.id.textViewSuccessCountTotal);
        textViewSuccessRate = (TextView)rootView.findViewById(R.id.textViewSuccessRate);
        textViewSpeed = (TextView)rootView.findViewById(R.id.textViewSpeed);
    }


    @Override
    public void onResume() {
        super.onResume();
        this.getActivity().registerReceiver(receiver, mStatusIntentFilter);
        this.getActivity().registerReceiver(networkStatusReceiver, networkUpdateFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.getActivity().unregisterReceiver(receiver);
        this.getActivity().unregisterReceiver(networkStatusReceiver);
    }


    private class ResponseReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.VOTE_RESULT_ACTION)) {
                Bundle extra = intent.getExtras();
                textViewVoteCount.setText("" + extra.getInt(Constants.VOTE_COUNT_KEY));
                textViewVoteCountTotal.setText("" + extra.getInt(Constants.VOTE_COUNT_TOTAL_KEY));

                textViewResult.setText(extra.getString("LOCATION"));
                textViewResultIOS.setText(extra.getString("iOS_LOCATION"));
                textViewResultAndroid.setText(extra.getString("ANDROID_LOCATION"));

                textViewSuccessCount.setText(intent.getExtras().getString(Constants.VOTE_SUCCESS_KEY));
                textViewSuccessCountTotal.setText(intent.getExtras().getString(Constants.VOTE_SUCCESS_TOTAL_KEY));
                textViewSuccessRate.setText(intent.getExtras().getString(Constants.VOTE_SUCCESS_RATE));
                textViewSpeed.setText(intent.getExtras().getString(Constants.VOTE_SUCCESS_SPEED));
            }

            if (intent.getAction().equals(Constants.VOTE_TIME_CHANGE_ACTION)) {
                textViewDelayAndroid.setText(intent.getExtras().getString("AndroidRemainTime"));
                textViewDelayIOS.setText(intent.getExtras().getString("iOSRemainTime"));
                textViewDelay.setText(intent.getExtras().getString("RemainTime"));


            }
        }
    }


    private class NetworkStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            textViewIP.setText(intent.getExtras().getString(Constants.NETWORK_IP_KEY));
        }
    }
}
