package com.creativedrewy.wearss.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.creativedrewy.wearss.R;
import com.creativedrewy.wearss.activities.FeedsMainActivity;
import com.creativedrewy.wearss.activities.SettingsActivity;
import com.creativedrewy.wearss.wearable.SendHeadline;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.gson.Gson;

/**
 * Service to respond from data coming back from wearable
 */
public class PhoneFromWearListenerService extends WearableListenerService {
    public static String OPEN_READ_LIST_EXTRA = "openReadListExtra";
    public static String OPEN_URL_PATH = "/saveFeed";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (OPEN_URL_PATH.equals(messageEvent.getPath())) {
            SendHeadline receivedHeadline = new Gson().fromJson(new String(messageEvent.getData()), SendHeadline.class);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String readListPref = prefs.getString(getString(R.string.key_prefs_read_item_tap), "0");
            boolean appShareFromWatch = prefs.getBoolean(getString(R.string.key_prefs_article_select_from_watch), false);
            if (appShareFromWatch && readListPref.equals(SettingsActivity.TAP_SPECIFIC_APP_VALUE)) {
                new DeviceAppService().shareToSpecificSelectedApp(this, receivedHeadline);
            } else {
                receivedHeadline.setInReadList(true);

                if (!HeadlinesService.isArticleInList(receivedHeadline.getArticleUrl())) {
                    receivedHeadline.save();
                }

                Intent intent = new Intent(this, FeedsMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(OPEN_READ_LIST_EXTRA, true);
                startActivity(intent);

                sendBroadcast(new Intent(getString(R.string.receive_headline_intent)));
            }
        }
    }
}
