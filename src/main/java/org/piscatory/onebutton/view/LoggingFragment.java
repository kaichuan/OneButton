package org.piscatory.onebutton.view;

import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import org.piscatory.onebutton.Constants;
import org.piscatory.onebutton.R;
import org.piscatory.onebutton.Utils;

import java.util.Date;

public class LoggingFragment extends ListFragment {

    private LoggingAdapter loggingAdapter;

    private IntentFilter intentFilter = new IntentFilter(Constants.VOTE_STATUS_UPDATE_ACTION);
    private StatusUpdateReceiver statusUpdateReceiver = new StatusUpdateReceiver();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loggingAdapter = new LoggingAdapter(getActivity(), R.layout.logging_item);
        setListAdapter(loggingAdapter);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getActivity().registerReceiver(statusUpdateReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.getActivity().unregisterReceiver(statusUpdateReceiver);
    }

    private class LoggingAdapter extends ArrayAdapter<String>{
        public LoggingAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }
    }


    private class StatusUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getExtras().getString(Constants.VOTE_STATUS_KEY);
            loggingAdapter.add(Utils.formatDate(new Date()) + " " + message);
        }
    }
}
