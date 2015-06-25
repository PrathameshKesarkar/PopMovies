package com.example.prathamesh.popmovies;

import java.util.Date;

/**
 * Created by prathamesh on 6/18/15.
 */
public class MoviesDataModel {
    private String mPlot;
    private String mMoviePosterString;
    private String mUserRating;
    private Date mReleaseDate;
    private String mOrignalTitle;
    private String mBackDrop;

    public String getBackDrop() {
        return mBackDrop;
    }

    public void setBackDrop(String backDrop) {
        mBackDrop = backDrop;
    }

    public String getOrignalTitle() {
        return mOrignalTitle;
    }

    public void setOrignalTitle(String orignalTitle) {
        mOrignalTitle = orignalTitle;
    }


    public String getPlot() {
        return mPlot;
    }

    public void setPlot(String plot) {
        mPlot = plot;
    }

    public String getMoviePosterString() {
        return mMoviePosterString;
    }

    public void setMoviePosterString(String moviePosterString) {
        mMoviePosterString = moviePosterString;
    }

    public String getUserRating() {
        return mUserRating;
    }

    public void setUserRating(String userRating) {
        mUserRating = userRating;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        mReleaseDate = releaseDate;
    }



}
