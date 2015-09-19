package com.creativedrewy.wearss.services;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import rx.Observable;

/**
 * Provides methods for downloading and processing article text
 */
public class ArticleDownloadService {

    /**
     * Download the full html contents of the article at the provided URL
     */
    public static Observable<String> downloadArticle(String url) {
        return Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            Request request = new Request.Builder().url(url).build();

            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    subscriber.onError(e);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    subscriber.onNext(new String(response.body().bytes()));
                    subscriber.onCompleted();
                }
            });
        });
    }
}
