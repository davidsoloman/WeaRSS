package com.creativedrewy.wearss.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.creativedrewy.wearss.R;
import com.creativedrewy.wearss.models.DestinationAppInfo;
import com.creativedrewy.wearss.wearable.SendHeadline;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates getting and working with device app info
 */
public class DeviceAppService {

    /**
     * Get the intent that opens the general text share dialog
     */
    public Intent getGeneralShareIntent(String urlText) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, urlText);
        sendIntent.setType("text/plain");

        return sendIntent;
    }

    /**
     * Get intent to share url to specific app
     */
    public Intent getSpecificAppShareIntent(String url, String packageName, String activityName) {
        Intent specific = new Intent();
        specific.setAction(Intent.ACTION_SEND);
        specific.putExtra(Intent.EXTRA_TEXT, url);
        specific.setClassName(packageName, activityName);

        return specific;
    }

    /**
     * Share to the specific app that the user has selected in the prefs
     */
    public void shareToSpecificSelectedApp(Context context, SendHeadline article) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String sourceJson = prefs.getString(context.getString(R.string.key_stored_specific_share_app), "");

        if (!sourceJson.equals("")) {
            DestinationAppInfo destAppInfo = DestinationAppInfo.deserialize(sourceJson);
            Intent intent = getSpecificAppShareIntent(article.getArticleUrl(), destAppInfo.getPackageName(), destAppInfo.getActivityInfo());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * Enumerate the apps that can receive urls for sharing
     */
    public List<DestinationAppInfo> getDestinationApps(Activity activity) {
        List<DestinationAppInfo> destApps = new ArrayList<DestinationAppInfo>();

        Intent sendIntent = getGeneralShareIntent("http://www.google.com");
        List<ResolveInfo> resInfo = activity.getPackageManager().queryIntentActivities(sendIntent, 0);

        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                DestinationAppInfo currInfo = new DestinationAppInfo();
                currInfo.setPackageName(info.activityInfo.packageName);
                currInfo.setActivityInfo(info.activityInfo.name);

                try {
                    currInfo.setAppIcon(activity.getPackageManager().getApplicationIcon(info.activityInfo.packageName));

                    ApplicationInfo appInfo = activity.getPackageManager().getApplicationInfo(info.activityInfo.packageName, 0);
                    currInfo.setReadableName(activity.getPackageManager().getApplicationLabel(appInfo).toString());
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e("WeaRSS", "-- Error getting application related information --", e);
                }

                destApps.add(currInfo);
            }
        }

        return destApps;
    }
}