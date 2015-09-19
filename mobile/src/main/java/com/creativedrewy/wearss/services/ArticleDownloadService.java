package com.creativedrewy.wearss.services;

import com.creativedrewy.wearss.wearable.SendHeadline;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Source;

import rx.Observable;

/**
 * Provides methods for downloading and processing article text
 */
public class ArticleDownloadService {

    /**
     * Download the full html contents of the article at the provided URL
     */
    public Observable<Boolean> downloadAndTrimArticle(SendHeadline headline) {
        return Observable.create((Observable.OnSubscribe<Boolean>) subscriber -> {
            Request request = new Request.Builder().url(headline.getArticleUrl()).build();

            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    subscriber.onError(e);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String articleText = new String(response.body().bytes());

                    Renderer renderer = new Source(articleText).getRenderer();
                    renderer.setMaxLineLength(150);
                    renderer.setIncludeHyperlinkURLs(false);
                    renderer.setIncludeAlternateText(false);
                    renderer.setHRLineLength(8);
                    renderer.setListIndentSize(0);

                    String trimmedArticle = renderer.toString();
                    headline.setArticleText(trimmedArticle);

                    subscriber.onNext(true);
                    subscriber.onCompleted();
                }
            });
        });
    }
}
