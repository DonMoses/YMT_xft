package com.ymt.demo1.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * Created by DonMoses on 2015/9/17
 */
public class ConnectionActReceiver extends BroadcastReceiver {
    private OnConnectionChangeListener onConnectionChangeListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case ConnectivityManager.CONNECTIVITY_ACTION:
                if (onConnectionChangeListener == null) return;
                onConnectionChangeListener.onConnected();
                break;
            default:
                break;
        }
    }

    public interface OnConnectionChangeListener {
        void onConnected();
    }

    public void setOnConnectionChangeListener(OnConnectionChangeListener onConnectionChangeListener) {
        this.onConnectionChangeListener = onConnectionChangeListener;
    }
}
