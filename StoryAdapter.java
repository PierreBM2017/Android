package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.android.newsapp.R.id.story_author;
import static com.example.android.newsapp.R.id.story_date;
import static com.example.android.newsapp.R.id.story_section;
import static com.example.android.newsapp.R.id.story_title;

// import static com.example.android.newsapp.R.id.story_thumbnail;

/**
 * {@link StoryAdapter} is an {@link ArrayAdapter} that can provide the layout for each list item
 * based on a data source, which is a list of {@link Story} objects.
 */
class StoryAdapter extends ArrayAdapter<Story> {

    // Not used : public static final String LOG_TAG = StoryAdapter.class.getName();

    StoryAdapter(Context context, ArrayList<Story> Stories) {
        super(context, 0, Stories);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.story_item, parent, false);

            // Get the {@link Storys} object located at this position in the list
            Story currentStory = getItem(position);

            if (currentStory != null) {
                // get the Title from the current Story Object and set this text on the TextView
                TextView StoryTextView = (TextView) listItemView.findViewById(story_title);
                StoryTextView.setText(currentStory.getTitle());

                // get the Section from the current Story Object and set this text on the TextView
                TextView SectionTextView = (TextView) listItemView.findViewById(story_section);
                SectionTextView.setText(currentStory.getSection());

                // get the Author from the current Story Object and set this text on the TextView
                // check if available
                TextView AuthorTextView = (TextView) listItemView.findViewById(story_author);
                AuthorTextView.setText(currentStory.getAuthor());
                // get the Description from the current Story Object and set this text on the TextView
                // check if available
                TextView StoryDateTextView = (TextView) listItemView.findViewById(story_date);
                StoryDateTextView.setText(currentStory.getDate());

            }
        }
        return listItemView;
    }

}