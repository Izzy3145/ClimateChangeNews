package com.example.android.climatechangenews;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by izzystannett on 11/06/2017.
 */

public class ArticleAdapter extends ArrayAdapter<NewsArticle> {

    public ArticleAdapter(Activity context, ArrayList<NewsArticle> articles) {
        super(context, 0, articles);
    }

    //override the getView method so we are not limited to passing just one TextView in
    //to the ListView

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final NewsArticle article = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_item, parent, false);
        }

        //set text views
        TextView articleSection = (TextView) view.findViewById(R.id.article_section);
        articleSection.setText(article.getmSection());

        TextView articleName = (TextView) view.findViewById(R.id.article_name);
        articleName.setText(article.getmArticleName());

        //set on click listeners to take user to the URL
        View currentView = view.findViewById(R.id.article_list_item);

        //Set the onClickListener method.
        //
        currentView.setOnClickListener(new TextView.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(article.getmArticleUrl()));
                getContext().startActivity(intent);
            }
        });

        return view;
    }

}
