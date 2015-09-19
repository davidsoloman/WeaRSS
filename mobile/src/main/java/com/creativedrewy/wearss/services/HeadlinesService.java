package com.creativedrewy.wearss.services;

import com.creativedrewy.wearss.wearable.SendHeadline;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

/**
 * Encapsulates calls around working with the send/saved headlines
 */
public class HeadlinesService {

    /**
     * Has the article already been added to the list
     */
    public static boolean isArticleInList(String articleUrl) {
        SendHeadline headline = Select.from(SendHeadline.class)
                                        .where(Condition.prop(SendHeadline.ARTICLE_URL_COL).eq(articleUrl))
                                        .first();
        return headline != null;
    }

    /**
     * Get the set of headline objects that have been saved from the wearable
     */
    public static List<SendHeadline> getSavedHeadlines() {
        return Select.from(SendHeadline.class)
                .where(Condition.prop(SendHeadline.IN_READ_LIST_COL).eq(1))
                .orderBy(SendHeadline.HEADLINE_COL)
                .list();
    }
}
