package com.example.android.climatechangenews;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.R.attr.onClick;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsArticle>> {
    private static final int ARTICLE_LOADER_ID = 1;
    private ArticleAdapter mAdapter;
    TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find a reference to the listView in the layout
        ListView newsListView = (ListView) findViewById(R.id.news_list_view);

        //create a new adapter that takes an empty list of articles as input
        mAdapter = new ArticleAdapter(this, new ArrayList<NewsArticle>());

        //set the adapter to the list view so it can be populated
        newsListView.setAdapter(mAdapter);

        //set onClickListener to the update button
        Button updateButton = (Button) findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call LoaderManager to fetch new articles
                getLoaderManager().initLoader(ARTICLE_LOADER_ID, null, MainActivity.this).forceLoad();
            }
        });

        //set up the empty view for when no results are returned
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        //ask Connectivity Manager to check the status of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        //get status of default network connection
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //if there is a network connection, initialise loader
        if(networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(ARTICLE_LOADER_ID, null, this).forceLoad();
        } else {

            //otherwise set the EmptyView to display an appropriate message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    /**
     * override the three abstract methods within Loader Callbacks interface
     * First, onCreateLoader to determine when to load a new loader
     * Second, onLoadFinished, which is normally used to update the UI
     * Third, OnLoaderReset which contains what to do when the data from our loader is no longer valid
     */
    @Override
    public Loader<List<NewsArticle>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader
        return new ArticleLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsArticle>> loader, List<NewsArticle> newsArticles) {
        mAdapter.clear();

        mEmptyStateTextView.setText(R.string.no_results);

        //if some articles have been found, inflate the adapter with them
        if (newsArticles != null && !newsArticles.isEmpty()) {
            mAdapter.addAll(newsArticles);
        }

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<NewsArticle>> loader) {
        mAdapter.clear();
    }
}
