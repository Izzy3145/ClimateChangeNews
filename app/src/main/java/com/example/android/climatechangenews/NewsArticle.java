package com.example.android.climatechangenews;

import static android.R.attr.author;

/**
 * Created by izzystannett on 11/06/2017.
 */

public class NewsArticle {

    //intialise variables
    private String mSection;
    private String mArticleName;
    private String mArticleUrl;

    //set up constructor
    public NewsArticle(String section, String articleName, String articleUrl){
        mSection=section;
        mArticleName=articleName;
        mArticleUrl = articleUrl;
    }

    //set getter methods
    public String getmSection(){
        return mSection;
    }

    public String getmArticleName(){
        return mArticleName;
    }

    public String getmArticleUrl(){
        return mArticleUrl;
    }

}
