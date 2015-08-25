package com.ymt.demo1.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ymt.demo1.R;
import com.ymt.demo1.demos.TestActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by DonMoses on 2015/8/25
 */
public class MosesJPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("TAG", "action>>>" + intent.getAction());
        String action = intent.getAction();
        switch (action) {
            case "cn.jpush.android.intent.MESSAGE_RECEIVED":
                Intent intent1 = new Intent(context, TestActivity.class);
                PendingIntent pendingIntent =
                        PendingIntent.getActivity(context, 0, intent1, 0);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

                builder.setSmallIcon(R.drawable.ic_plusone_small_off_client)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_app_corner))
                        .setContentTitle("新消防消息")
                        .setContentText(intent.getExtras().getString(JPushInterface.EXTRA_MESSAGE))
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);

                Notification notification = builder.build();
                NotificationManager manager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, notification);
                break;
            default:
                break;
        }
    }
}
