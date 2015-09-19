package com.creativedrewy.wearss.feedservice;

import com.creativedrewy.wearss.wearable.SendHeadline;

import java.util.Comparator;

/**
 * Compare two SendHeadlines, taking into account if either has no pub date
 */
public class SendHeadlineComparator implements Comparator<SendHeadline> {
    @Override
    public int compare(SendHeadline firstHeadline, SendHeadline secondHeadline) {
        return secondHeadline.getPostDate().compareTo(firstHeadline.getPostDate());
    }
}
