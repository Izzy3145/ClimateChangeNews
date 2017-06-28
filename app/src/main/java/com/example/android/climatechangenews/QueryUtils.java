package com.example.android.climatechangenews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by izzystannett on 11/06/2017.
 */

public class QueryUtils {

    /**
     * Tag for the log messages that returns simple class name
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * provide the Guardian URL with personal API_KEY
     */
    private static final String API_URL = "https://content.guardianapis.com/search?q=climatechange&api-key=9d3f96ad-2cc7-4f8d-9026-9f6f442bd691";

    public static List<NewsArticle> fetchNewsArticles() {

        URL guardianAPI = createUrl(API_URL);

        //perform HTTP request to the queiried URL and receive JSONresponse back
        String jsonResponse = "";

        try {
            jsonResponse = makeHttpRequest(guardianAPI);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        List<NewsArticle> newsArticles = extractFeatureFromJson(jsonResponse);
        return newsArticles;
    }

    /**
     * create URL object from string
     */
    private static URL createUrl(String stringUrl) {
        //handle null case
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }

    /**
     * perform a network request to a URL and return the JSONReponse
     */
    private static String makeHttpRequest(URL url) throws IOException {

        //the output string starts as an empty string, ready to be 'built'
        String jsonResponse = "";

        //initialise objects
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            //try to connect
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //if connection successful, then read input stream
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Problem with connection. Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                //call close() method, which may cause IOExcpetion
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * convert the inputstream, which is currently in byte-form
     * into a readable string
     */

    private static String readFromStream(InputStream inputStream) throws IOException {
        //create String builder so the buffered reader can continually add characters until end of inputStream
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            //call the readLine() method, which may cause an IOException
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a NewsArticle object by parsing the JSON response
     */
    private static List<NewsArticle> extractFeatureFromJson(String jsonResponse) {
        List<NewsArticle> newsArticles = new ArrayList<>();

        //if the jsonString is empty, return early
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONObject baseObject = baseJsonResponse.getJSONObject("response");
            JSONArray resultsArray = baseObject.getJSONArray("results");


            String articleSection;
            String articleName;
            String articleUrl;

            // If there are results in the features array
            for (int i = 0; i < resultsArray.length(); i++) {
                //Extract each article one at a time
                JSONObject articleObject = resultsArray.getJSONObject(i);
                //text for an entry under article section
                if (articleObject.has("sectionName")) {
                    articleSection = articleObject.getString("sectionName");
                } else {
                    articleSection = null;
                }
                //test for article name
                if (articleObject.has("webTitle")) {
                    articleName = articleObject.getString("webTitle");
                } else {
                    articleName = null;
                }

                //test for article website
                if (articleObject.has("webUrl")) {
                    articleUrl = articleObject.getString("webUrl");
                } else {
                    articleUrl = null;
                }


                //create new BookItem object and add to the Array List
                NewsArticle foundArticle = new NewsArticle(articleSection, articleName, articleUrl);
                newsArticles.add(foundArticle);
            }
            return newsArticles;

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON results", e);
        }
        return null;
    }

}
