package com.creativedrewy.wearss.feedservice;

import android.util.Log;

import com.axelby.riasel.FeedItem;
import com.creativedrewy.wearss.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Compares two RssItem objects by their publication date
 */
public class FeedItemDateComparator implements Comparator<FeedItem> {
    @Override
    public int compare(FeedItem firstItem, FeedItem secondItem) {
        try {
            return secondItem.getPublicationDate().compareTo(firstItem.getPublicationDate());
        } catch (Exception e) {
            Log.e("WeaRSS", "::: Error when sorting RSS items :::", e);
            return 0;
        }
    }
}
