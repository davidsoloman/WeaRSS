package com.creativedrewy.wearss.feedservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.creativedrewy.wearss.R;

import java.util.concurrent.TimeUnit;

/**
 * Facilitates the recurring headline sync operation
 */
public class SyncAlarmListenerService implements WakefulIntentService.AlarmListener {
    @Override
    public void scheduleAlarms(AlarmManager alarmManager, PendingIntent pendingIntent, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String refreshFreqString = prefs.getString(context.getString(R.string.key_prefs_refresh_frequency), "360");
        int refreshInMinutes = Integer.parseInt(refreshFreqString);

        if (refreshInMinutes > -1) {
            long refreshMillis = TimeUnit.MINUTES.toMillis(refreshInMinutes);
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + refreshMillis, refreshMillis, pendingIntent);
        } else {
            WakefulIntentService.cancelAlarms(context);
        }

        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(context.getString(R.string.key_prefs_alarms_started), true);
        edit.commit();
    }

    @Override
    public void sendWakefulWork(Context context) {
        WakefulIntentService.sendWakefulWork(context, RefreshHeadlinesService.class);
    }

    @Override
    public long getMaxAge() {
        return (AlarmManager.INTERVAL_FIFTEEN_MINUTES * 2);
    }
}
