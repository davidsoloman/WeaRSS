package com.creativedrewy.wearss.feedservice;

import com.axelby.riasel.Feed;

import rx.Subscriber;

/**
 * Translate a feed processor to an observable subscriber
 */
public class FeedProcessedSubscriber implements IFeedProcessedHandler {
    private Subscriber<? super Feed> mSubscriber;

    /**
     * Constructor
     */
    public FeedProcessedSubscriber(Subscriber<? super Feed> sub) {
        mSubscriber = sub;
    }

    @Override
    public void processSuccessful(Feed feed) {
        finishSubscriber(feed);
    }

    @Override
    public void errorProcessingFeed(Throwable throwable) {
        finishSubscriber(null);
    }

    private void finishSubscriber(Feed feed) {
        mSubscriber.onNext(feed);
        mSubscriber.onCompleted();
    }
}
