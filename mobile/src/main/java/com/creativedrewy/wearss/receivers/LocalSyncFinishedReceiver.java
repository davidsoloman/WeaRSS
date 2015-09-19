package com.creativedrewy.wearss.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.creativedrewy.wearss.R;
import com.creativedrewy.wearss.feedservice.RefreshHeadlinesService;

/**
 * Small broadcast receiver that handles news feed sync finish event; meant only to be utilized
 * within a visible activity
 */
public class LocalSyncFinishedReceiver extends BroadcastReceiver {
    private Activity mActivityContext;

    /**
     * Constructor
     */
    public LocalSyncFinishedReceiver(Activity activity) {
        mActivityContext = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = mActivityContext.getString(R.string.headlines_synced);
        if (!intent.getBooleanExtra(RefreshHeadlinesService.COMPLETION_SUCCESS_EXTRA, true)) {
            message = mActivityContext.getString(R.string.error_syncing_headlines);
        }

        Toast.makeText(mActivityContext, message, Toast.LENGTH_LONG).show();
        mActivityContext.setProgressBarIndeterminateVisibility(false);
    }
}
