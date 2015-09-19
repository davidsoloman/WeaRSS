package com.creativedrewy.wearss.services;

import com.creativedrewy.wearss.wearable.SendHeadline;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Source;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import rx.Observable;

/**
 * Provides methods for downloading and processing article text
 */
public class ArticleDownloadService {

    /**
     * Download the full html contents of the article at the provided URL and save it back
     * into the headline object
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

                    String plainText = generateArticlePlainText(articleText);
                    plainText = plainText.replace("\n\n\n\n", "\n");
                    plainText = plainText.replace("\n\n\n", "\n");
                    plainText = plainText.replace("\n\n", "\n");
                    headline.setArticleText(plainText);

                    subscriber.onNext(true);
                    subscriber.onCompleted();
                }
            });
        });
    }

    /**
     * Return the plain text version of the full article text
     */
    private String generateArticlePlainText(String srcArticleMarkup) {
        String cleanedMarkup = cleanArticleMarkup(srcArticleMarkup);

        Renderer renderer = new Source(cleanedMarkup).getRenderer();
        renderer.setMaxLineLength(150);
        renderer.setIncludeHyperlinkURLs(false);
        renderer.setIncludeAlternateText(false);
        renderer.setHRLineLength(8);
        renderer.setListIndentSize(0);

        return renderer.toString();
    }

    /**
     * Clean any unwanted elements from the source article markup
     */
    private String cleanArticleMarkup(String srcMarkup) {
        Source sourceDom = new Source(srcMarkup);
        OutputDocument outputDocument = new OutputDocument(sourceDom);
        List<Element> allElements = sourceDom.getAllElements();

        List<String> elementsToRemove = Arrays.asList(HTMLElementName.LI, HTMLElementName.OL, HTMLElementName.UL);
        for (Element element : allElements) {
            if (elementsToRemove.contains(element.getName())) {
                outputDocument.remove(element);
            }
        }

        return outputDocument.toString();
    }
}
