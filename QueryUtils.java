package com.example.android.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

/**
 * methods for retrieving data over a network using google book api
 * mainly generic to network operations
 * the most specific part is the bitmap loading
 */
final class QueryUtils {
    // TAG for Log exception handling
    private static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * @param thisApp is the context of Main Activity
     * @return the boolean "true" if network and connectivity are available
     */
    @NonNull
    static Boolean isConnectivity(Context thisApp) {

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) thisApp.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, TRUE to fetch data
        return (networkInfo != null && networkInfo.isConnected());
    }


    /**
     * Returns new URL object from the given string URL.
     *
     * @param stringUrl is the String to use to GET an answer
     * @return url a valid URL or null if stringURL is null
     */
    static URL createUrl(String stringUrl) {
        URL url = null;
        if (!stringUrl.equals("")) {
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Problem building the URL ", e);
            }
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     *
     * @param url should have been validated before use
     * @return httpResponse, a string with the stream if the request has been completed
     */
    static String makeHttpRequest(URL url) throws IOException {
        String httpResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return httpResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                httpResponse = readFromStream(inputStream);
                inputStream.close();
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Google Book API JSON results.", e);
        } finally {

            if (urlConnection != null) urlConnection.disconnect();
        }
        return httpResponse;
    }


    /**
     * @param inputStream is a stream return by a GET on the Internet
     * @return a String that can be parsed
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
