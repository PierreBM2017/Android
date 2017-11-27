package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.android.newsapp.R.id.update_news_image;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Story>> {

   // Query usually gets hundreds answers
    public static final int MAX_STORY_TO_DISPLAY = 10;
    // Constant value for the loader ID.
    private static final int STORY_LOADER_ID = 1;
    // TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;
    //Adapter for the list of Story(s)
    private StoryAdapter mAdapter;

    /**
     * "global warming" Query for Story searching using TheGuardian API
     */
    private String StoryToSearch = "http://content.guardianapis.com/search?q=global+warming&api-key=test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Find a reference to the {@link ListView} in the layout
        ListView storyListView = (ListView) findViewById(R.id.activity_story_list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        storyListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of stories as input
        mAdapter = new StoryAdapter(this, new ArrayList<Story>());
        storyListView.setEmptyView(findViewById(R.id.empty_view));
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        storyListView.setAdapter(mAdapter);
        /****************************************************************
         * check the connectivity availability and type
         * display a warning message if none
         * */
        // If there is a network connection, fetch data for android book to initialize UI
        if (QueryUtils.isConnectivity(this)) {
            // first view
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(STORY_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.progress);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message 
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
        /*****************************************************************/
        /**
         * listener on the search image
         * use text from storyToSearch to create the url for TheGuardian API
         */
        ImageView searchNewsImage = (ImageView) findViewById(update_news_image);
        if (searchNewsImage != null) {
            searchNewsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Check connectivity
                    Context context = getApplicationContext();
                    if (QueryUtils.isConnectivity(context)) {

                        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                        // the bundle. Pass in the MainActivity for the LoaderCallbacks parameter (which is valid
                        // because this activity implements the LoaderCallbacks interface).
                        getLoaderManager().restartLoader(STORY_LOADER_ID, null, NewsActivity.this);
                        //  Change the TextView update time
                        TextView textToDisplay = (TextView) findViewById(R.id.last_update_date);
                        textToDisplay.setText(nowDateHour());

                    } else {
                        // Otherwise, display error
                        // First, hide loading indicator so error message will be visible
                        View loadingIndicator = findViewById(R.id.progress);
                        loadingIndicator.setVisibility(View.GONE);

                        // Update empty state with no connection error message
                        mEmptyStateTextView.setText(R.string.no_internet_connection);
                    }
                }
            });
        }

        /**
         * set a Listener on the List to define which Story has been clicked on
         * than display it with a web browser
         */
        storyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                /**
                 * Get the {@link Story} object at the given position the user clicked on
                 *Create and setup the intent
                 */
                // Get the item at the position clicked
                Story clickedItemStory = mAdapter.getItem(position);
                // get the Url of the Story
                String StoryStringUrl = clickedItemStory.getStoryURL();
                if (!StoryStringUrl.equals("")) {
                    // create a new Intent for web display of the story
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(StoryStringUrl));
                    // Start the new activity
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);

                    } else {
                        //  Change the TextView Text to warn user
                        TextView textToDisplay = (TextView) findViewById(R.id.last_update_date);
                        textToDisplay.setText(R.string.no_browser);
                    }
                } else {
                    TextView textToDisplay = (TextView) findViewById(R.id.last_update_date);
                    textToDisplay.setText(R.string.no_stories);
                }
            }
        });
    }

    /********
     * LOADER MANAGEMENT  for List of Story (ies)
     **********************/

    @Override
    public Loader<List<Story>> onCreateLoader(int i, Bundle bundle) {
        /* Create a new loader for the given string containing the query */
        return new StoryListLoader(this, StoryToSearch);

    }

    @Override
    public void onLoadFinished(Loader<List<Story>> loader, List<Story> stories) {

        ProgressBar circle = (ProgressBar) findViewById(R.id.progress);
        circle.setVisibility(View.INVISIBLE);
        // Clear the adapter of previous earthquake data
        mAdapter.clear();
        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (stories != null && !stories.isEmpty()) {
            mAdapter.addAll(stories);
            mAdapter.notifyDataSetChanged();
            //  Change the TextView update time
            TextView textToDisplay = (TextView) findViewById(R.id.last_update_date);
            textToDisplay.setText(nowDateHour());
        } else {
            // Set empty state text to display
            mEmptyStateTextView.setText(R.string.no_stories);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Story>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
    }

    /*
    * @return String containing date and hour
     */

    private String nowDateHour() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MMMM:yyyy HH:mm a");
        return (sdf.format(c.getTime()));
    }
}

