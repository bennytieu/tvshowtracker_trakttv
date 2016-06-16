package model;


import iprog.showtracker.R;

/**
 * Properties for a TV-Show stored in one class
 */
public class TVShow {
    private int mID;
    private String mTitle;
    private String mOverview;
    private String mYear;
    private String mImgThumb;

    public TVShow() {
        // Set default values on a TV-show, assuming that the objects does not have any values
        setTitle("Title N/A");
        setOverview("Overview N/A");
        setYear("Year N/A");
        setImgThumb("drawable://" + R.drawable.example_thumb);
    }

    public void setID(int id) {
        mID = id;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setOverview(String overview) { mOverview = overview;}

    public void setYear(String year) {
        mYear = year;
    }

    public void setImgThumb(String imgThumb) {
        mImgThumb = imgThumb;
    }

    public int getID(){return mID;}

    public String getTitle() {
        return mTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getYear() {
        return mYear;
    }

    public String getImgThumb() {return mImgThumb;}


}
