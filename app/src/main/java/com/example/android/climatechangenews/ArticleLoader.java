package com.example.android.climatechangenews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by izzystannett on 11/06/2017.
 */

public class ArticleLoader extends AsyncTaskLoader<List<NewsArticle>> {

    //create constructor
    public ArticleLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * on background thread, fetch news stories
     */
    public List<NewsArticle> loadInBackground() {
        List<NewsArticle> newsArticles = QueryUtils.fetchNewsArticles();
        return newsArticles;
    }
}
