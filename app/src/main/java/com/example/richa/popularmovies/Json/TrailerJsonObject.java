package com.example.richa.popularmovies.Json;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Richa on 9/17/15.
 */
public class TrailerJsonObject implements Parcelable {
    private String name;
    private String size;
    private String source;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(size);
        dest.writeString(source);
        dest.writeString(type);

    }
}
