package com.creativedrewy.wearss.feedservice;

import com.axelby.riasel.Feed;

/**
 * Result interface for feed processing
 */
public interface IFeedProcessedHandler {
    public void processSuccessful(Feed feed);
    public void errorProcessingFeed(Throwable throwable);
}
