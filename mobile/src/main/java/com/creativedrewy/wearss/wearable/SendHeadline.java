package com.creativedrewy.wearss.wearable;

import com.orm.SugarRecord;

import java.util.Calendar;
import java.util.Date;

/**
 * Small POJO to hold data about a headline to send to the wearable
 */
public class SendHeadline extends SugarRecord<SendHeadline> {
    public static String HEADLINE_COL = "M_HEADLINE";
    public static String ARTICLE_URL_COL = "M_ARTICLE_URL";
    public static String IN_READ_LIST_COL = "IS_IN_READ_LIST";

    private String mHeadline;
    private String mSourceHost;
    private Date mPostDate;
    private String mArticleText;
    private String mArticleUrl;
    private boolean mHasPubDate = true;
    private boolean isInReadList = false;
    private boolean mHasBeenRead = false;
    private boolean isItemSelected = false;

    public String getHeadline() { return mHeadline; }
    public void setHeadline(String mHeadline) { this.mHeadline = mHeadline; }

    public String getSourceHost() { return mSourceHost; }
    public void setSourceHost(String mSourceHost) { this.mSourceHost = mSourceHost; }

    public String getArticleUrl() { return mArticleUrl; }
    public void setArticleUrl(String mArticleUrl) { this.mArticleUrl = mArticleUrl; }

    public Date getPostDate() { return mPostDate; }
    public void setPostDate(Date mPostDate) { this.mPostDate = mPostDate; }

    public String getArticleText() { return mArticleText; }
    public void setArticleText(String mArticleText) { this.mArticleText = mArticleText; }

    public boolean hasPubDate() { return mHasPubDate; }
    public void setHasPubDate(boolean hasPubDate) { mHasPubDate = hasPubDate; }

    public boolean isInReadList() { return isInReadList; }
    public void setInReadList(boolean isInReadList) { this.isInReadList = isInReadList; }

    public boolean isHasBeenRead() { return mHasBeenRead; }
    public void setHasBeenRead(boolean hasBeenRead) { this.mHasBeenRead = hasBeenRead; }

    public boolean isItemSelected() { return isItemSelected; }
    public void setItemSelected(boolean isItemSelected) { this.isItemSelected = isItemSelected; }

    /**
     * Constructor required for SugarORM
     */
    public SendHeadline() { }

    /**
     * Constructor
     */
    public SendHeadline(String headline, String source, Date sourceDate, String articleText, String url) {
        mHeadline = headline;
        mSourceHost = source;
        mArticleText = articleText;
        mArticleUrl = url;

        mPostDate = sourceDate;
        if (mPostDate == null) {
            mHasPubDate = false;

            //If not parcelable, set the date to one year ago for comparison purposes - should push headline to back of list
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(cal.getTimeInMillis() - 31556952000L);
            mPostDate = cal.getTime();
        }
    }
}
