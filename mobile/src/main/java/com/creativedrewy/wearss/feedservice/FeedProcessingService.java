package com.creativedrewy.wearss.feedservice;

import android.content.Context;
import android.util.Xml;

import com.axelby.riasel.FeedParser;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;

/**
 * Encapsulate functionality around processing feeds
 */
public class FeedProcessingService {
    private Context mContext;
    private IFeedProcessedHandler mProcessedHandler;

    /**
     * Constructor
     */
    public FeedProcessingService(Context context, IFeedProcessedHandler processedHandler) {
        mContext = context;
        mProcessedHandler = processedHandler;
    }

    /**
     * Attempt to get the feed data and parse it; handle any errors
     */
    public void getAndProcessFeed(String feedUrl) {
        Request request = new Request.Builder().url(feedUrl).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mProcessedHandler.errorProcessingFeed(e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                FeedParser feedParser = new FeedParser();
                feedParser.setFullFeedDataHandler((parser, feed) -> mProcessedHandler.processSuccessful(feed));

                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(response.body().byteStream(), "utf-8");
                    feedParser.parseFeed(parser);
                } catch (Exception e) {
                    mProcessedHandler.errorProcessingFeed(e);
                }
            }
        });
    }
}