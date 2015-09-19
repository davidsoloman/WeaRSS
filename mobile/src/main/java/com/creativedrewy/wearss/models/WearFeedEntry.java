package com.creativedrewy.wearss.models;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

/**
 * POJO Representing a feed that the user will have pushed to their device
 */
public class WearFeedEntry extends SugarRecord<WearFeedEntry> {
    private String mFeedUrl;
    private String mFeedName;

    public String getFeedUrl() { return mFeedUrl; }
    public void setFeedUrl(String mFeedUrl) { this.mFeedUrl = mFeedUrl; }

    public String getFeedName() { return mFeedName; }
    public void setFeedName(String mFeedName) { this.mFeedName = mFeedName; }

    public WearFeedEntry() {}

    /**
     * Constructor
     */
    public WearFeedEntry(String name, String url) {
        mFeedName = name;
        mFeedUrl = url;
    }

    @Override
    public String toString() {
        return mFeedName;
    }

    /**
     * Whether or not the specified URL feed entry is already in the db
     */
    public static boolean isFollowingUrl(String url) {
        //TODO: Look into making column name not a string
        WearFeedEntry entry = Select.from(WearFeedEntry.class).where(Condition.prop("M_FEED_URL").eq(url)).first();
        return entry != null;
    }
}
