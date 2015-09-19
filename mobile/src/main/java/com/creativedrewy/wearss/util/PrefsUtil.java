package com.creativedrewy.wearss.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.creativedrewy.wearss.R;

/**
 * Helper methods around working with the shard prefs
 */
public class PrefsUtil {

    /**
     * Get the number of headlines per feed to sync
     */
    public static int getHeadlineSyncCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String countStr = prefs.getString(context.getString(R.string.key_prefs_headlines_count), "5");
        return Integer.parseInt(countStr);
    }

    /**
     * Get whether or not the user wants to load read the full article or not
     */
    public static boolean getLoadFullArticle(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(R.string.key_prefs_read_full_articles), false);
    }

}
