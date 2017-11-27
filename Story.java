package com.example.android.newsapp;

/**
 * {@link Story} represents the main information retrieved from Guardian needed to create a Story
 */

class Story {
    private String mTitle;
    private String mAuthor;
    private String mDate ;
    private String mSection;
    private String mStoryURL;

    /**
     * Create a new Story object.
     *
     * @param Title        is the Title of the Story, and the name of the corresponding JSON attribute
     * @param Author       is the name of the Author name in the Json data
     * @param StoryDate         is the  date of the Story
     * @param Section      is the Section Classification retrieved from JSON data
     * @param StoryURL     is the url for the
     */
    Story(String Title, String Author, String StoryDate, String Section, String StoryURL) {
        this.mTitle = Title;
        this.mAuthor = Author;
        this.mDate = StoryDate;
        this.mSection = Section;
        this.mStoryURL = StoryURL;
    }

    String getTitle() {
        return mTitle;
    }

    String getAuthor() {
        return mAuthor;
    }
    String getDate() {   return mDate;   }

    String getSection() {
        return mSection;
    }

    String getStoryURL() {
        return mStoryURL;
    }

}
