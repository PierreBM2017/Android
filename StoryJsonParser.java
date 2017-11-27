package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.newsapp.NewsActivity.MAX_STORY_TO_DISPLAY;

/**
 * Json Parser based on the Guardian API, to create Story and List of Story
 * e.g. http://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2014-01-01&api-key=test
 */

class StoryJsonParser {
    // TAG for display in exception handling
    private static final String LOG_TAG = StoryJsonParser.class.getName();


    /**
     * Create a private constructor because no one should ever create a {@link StoryJsonParser} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */

    /**
     * @param storyJSON contains a JSON formatted string
     * @return a list of {@link Story} objects that has been built up from
     * parsing the given JSON response.
     * <p>
     * This method retrieves Title, Author, Description and Thumbnail image url
     * accordingly to the Story class
     */
    static List<Story> extractStories(String storyJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(storyJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding storys to
        List<Story> stories = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(storyJSON);

            // verify that the response status is "ok"
            JSONObject jsonResponse = baseJsonResponse.getJSONObject("response");
            String status = jsonResponse.getString("status");
            if (!status.equals("ok")) {
                return null;
            }
            // Extract the JSONArray associated with the key called "items",
            // which represents a list of or Story(s).
            JSONArray itemsJsonList = jsonResponse.getJSONArray("results");

            // For each item in the itemJsonList, create an {@link Story} object,
            // keep MAX_STORY_TO_DISPLAY as defined in main
            int maxStories = MAX_STORY_TO_DISPLAY;
            if (itemsJsonList.length() < maxStories) {
                maxStories = itemsJsonList.length();
            }
            for (int i = 0; i < maxStories; i++) {

                // Get a single story at position i within the list of stories
                JSONObject currentStory = itemsJsonList.getJSONObject(i);
                // Extract the value for the key called "title"
                String title = "";
                if (currentStory.has("webTitle")) {
                    title = currentStory.getString("webTitle");
                }
                // Extract the value for the key called "webPublicationDate"
                String date = "";
                if (currentStory.has("webPublicationDate")) {
                    date = currentStory.getString("webPublicationDate");
                }
                // Extract the value for the key called "sectionName"
                String section = "";
                if (currentStory.has("sectionName")) {
                    section = currentStory.getString("sectionName");
                }
                // Extract the value for the key called "author" if exists
                String author = "";
                if (currentStory.has("author")) {
                    author = currentStory.getString("author");
                }
                // extract the Json objects with URL for this story
                String webUrl = "";
                if (currentStory.has("webUrl")) {
                    webUrl = currentStory.getString("webUrl");
                }

                /** Create a new {@link Story} object with :
                 *   Story(String Title, String Author, String StoryDate, String Section, String StoryURL)
                 *  */
                Story story = new Story(title, author, date, section, webUrl);
                // Add the new {@link Story} to the list of Stories.
                stories.add(story);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the story JSON results", e);
        }

        // Return the list of stories
        return stories;
    }
}
