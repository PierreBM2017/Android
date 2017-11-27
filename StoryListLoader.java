package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Loader for a StoryList, extends AsyncTaskLoader
 * Make the Http Request and Extract Json features
 * create a List of Story(s)
 */

class StoryListLoader extends AsyncTaskLoader {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = StoryListLoader.class.getName();
    // Query URL
    private String mUrl;

    /**
     * Constructs a new {@link StoryListLoader}.
     *
     * @param context          of the activity
     * @param StoryToSearchUrl to load data from GOOGLE API and parse the Json response
     */
    StoryListLoader(Context context, String StoryToSearchUrl) {
        super(context);
        mUrl = StoryToSearchUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Story> loadInBackground() {
        String JsonResponse = "";
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of books.
        URL RequestUrl = QueryUtils.createUrl(mUrl);
        try {
            JsonResponse = QueryUtils.makeHttpRequest(RequestUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the requested url.", e);
        }

        // Extract relevant fields from the JSON response and return a list of {@link Story}s
        return StoryJsonParser.extractStories(JsonResponse);
    }
}
