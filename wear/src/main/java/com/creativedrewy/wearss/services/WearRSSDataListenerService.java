package com.creativedrewy.wearss.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.creativedrewy.wearss.R;
import com.creativedrewy.wearss.activities.ViewHeadlinesActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Service to listen for rss headline data updates
 */
public class WearRSSDataListenerService extends WearableListenerService {
    public static String NOTIFICATION_ID_EXTRA = "notificationId";
    private int mNotificationId = 636867;   //We may or may not be able to dictate this

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        storeHeadlineData(new String(messageEvent.getData()));
        generateNotification();
    }

    /**
     * Store the headline json data to prefs so the view activity can load them
     */
    private void storeHeadlineData(String messageData) {
        SharedPreferences prefs = getSharedPreferences(ViewHeadlinesActivity.PREFS_STORE, 0);

        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(ViewHeadlinesActivity.HEADLINES_DATA, messageData);
        edit.commit();
    }

    /**
     * Generate the notification that will allow the user to see their headlines
     */
    private void generateNotification() {
        Intent showHeadlinesIntent = new Intent(this, ViewHeadlinesActivity.class);
        showHeadlinesIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        showHeadlinesIntent.putExtra(NOTIFICATION_ID_EXTRA, mNotificationId);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.wearss_launcher)
                .setContentTitle(getString(R.string.updated_feed_notify_title))
                .setContentText(getString(R.string.updated_feed_notify_desc))
                .setContentIntent(PendingIntent.getActivity(this, 0, showHeadlinesIntent, 0));

        NotificationManagerCompat mgrCompat = NotificationManagerCompat.from(this);
        mgrCompat.notify(mNotificationId, builder.build());
    }
}
