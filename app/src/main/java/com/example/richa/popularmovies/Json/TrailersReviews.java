package com.example.richa.popularmovies.Json;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.richa.popularmovies.Json.ReviewJsonObject;
import com.example.richa.popularmovies.Json.TrailerJsonObject;

import java.util.ArrayList;

/**
 * Created by Richa on 9/18/15.
 */
public class TrailersReviews implements Parcelable {

    private ArrayList<TrailerJsonObject> trailerJsonObjects;
    private ArrayList<ReviewJsonObject> reviewJsonObjects;

    public ArrayList<TrailerJsonObject> getTrailerJsonObjects() {
        return trailerJsonObjects;
    }

    public void setTrailerJsonObjects(ArrayList<TrailerJsonObject> trailerJsonObjects) {
        this.trailerJsonObjects = trailerJsonObjects;
    }

    public ArrayList<ReviewJsonObject> getReviewJsonObjects() {
        return reviewJsonObjects;
    }

    public void setReviewJsonObjects(ArrayList<ReviewJsonObject> reviewJsonObjects) {
        this.reviewJsonObjects = reviewJsonObjects;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
