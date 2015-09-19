package com.creativedrewy.wearss.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Small POJO to hold each individual headline from a news feed
 */
public class FeedHeadline implements Serializable {
    private String mHeadline;
    private String mSourceHost;
    private Date mPostDate;
    private String mArticleText;
    private String mArticleUrl;
    private boolean mHasPubDate;

    public String getHeadline() { return mHeadline; }
    public void setHeadline(String mHeadline) { this.mHeadline = mHeadline; }

    /**
     * Source host is the string title of the feed - not the URL
     */
    public String getSourceHost() { return mSourceHost; }
    public void setSourceHost(String mSourceHost) { this.mSourceHost = mSourceHost; }

    public Date getPostDate() { return mPostDate; }
    public void setPostDate(Date mPostDate) { this.mPostDate = mPostDate; }

    public String getArticleText() { return mArticleText; }
    public void setArticleText(String mArticleText) { this.mArticleText = mArticleText; }

    public String getArticleUrl() { return mArticleUrl; }
    public void setArticleUrl(String mArticleUrl) { this.mArticleUrl = mArticleUrl; }

    public boolean hasPubDate() { return mHasPubDate; }
    public void setHasPubDate(boolean hasPubDate) { mHasPubDate = hasPubDate; }

    /**
     * Constructor
     */
    public FeedHeadline(String headline, String source) {
        mHeadline = headline;
        mSourceHost = source;
    }
}
