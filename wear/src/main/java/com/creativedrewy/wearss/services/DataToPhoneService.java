package com.creativedrewy.wearss.services;

import android.content.Context;
import android.os.AsyncTask;

import com.creativedrewy.wearss.model.FeedHeadline;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

/**
 * Encapsulates functionality around the wearable sending info back to the phone
 */
public class DataToPhoneService {
    private GoogleApiClient mApiClient;

    /**
     * Constructor
     */
    public DataToPhoneService(Context context) {
        mApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }

    /**
     * Send the particular Url string to be opened on the phone
     */
    public void sendHeadlineToReadList(final FeedHeadline headline) {
        //TODO: Come back and refactor this to be more general and could be used elsewhere
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ConnectionResult connectionResult = mApiClient.blockingConnect(30, TimeUnit.SECONDS);
                if (connectionResult.isSuccess()) {
                    NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();

                    if (nodes.getNodes().size() > 0) {
                        Node connectedNode = nodes.getNodes().get(0);

                        String headlineJSON = new Gson().toJson(headline);
                        Wearable.MessageApi.sendMessage(mApiClient, connectedNode.getId(), "/saveFeed", headlineJSON.getBytes());
                    }
                }

                return null;
            }
        };

        task.execute();
    }
}
