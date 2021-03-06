package com.example.richa.popularmovies.Json;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Richa on 8/24/15.
 */
public class MovieJsonObject implements Parcelable{
    private String adult;
    private String backdrop_path;
    private String id;
    private String original_language;
    private String original_title;
    private String overview;
    private String release_date;
    private String poster_path;
    private String title;
    private String video;
    private String vote_average;
    private String vote_count;
    private long localDbRow_id;

    public MovieJsonObject() {
    }

    public long getLocalDbRow_id() {
        return localDbRow_id;
    }

    public void setLocalDbRow_id(long localDbRow_id) {
        this.localDbRow_id = localDbRow_id;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(adult);
        dest.writeString(backdrop_path);
        dest.writeString(id);
        dest.writeString(original_language);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(poster_path);
        dest.writeString(title);
        dest.writeString(video);
        dest.writeString(vote_average);
        dest.writeString(vote_count);
        dest.writeLong(localDbRow_id);
    }

   public static final Parcelable.Creator<MovieJsonObject> CREATOR
            = new Parcelable.Creator<MovieJsonObject>() {
        public MovieJsonObject createFromParcel(Parcel in) {
            return new MovieJsonObject(in);
        }

        public MovieJsonObject[] newArray(int size) {
            return new MovieJsonObject[size];
        }
    };

    private MovieJsonObject(Parcel in) {
        adult = in.readString();
        backdrop_path = in.readString();
        id =in.readString();
        original_language = in.readString();
        original_title= in.readString();
        overview= in.readString();
        release_date= in.readString();
        poster_path= in.readString();
        title= in.readString();
        video= in.readString();
        vote_average= in.readString();
        vote_count= in.readString();
        localDbRow_id= in.readLong();
    }

}
