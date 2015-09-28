package com.example.richa.popularmovies.Json;

import com.example.richa.popularmovies.Json.ReviewJsonObject;
import com.example.richa.popularmovies.Json.TrailerJsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Richa on 9/17/15.
 */
public class MovieDetailJsonParser {
    public static ArrayList<TrailerJsonObject> getMovieTrailersFromJson(String movieJsonString)
            throws JSONException {
        TrailerJsonObject trailerJsonObject;
        ArrayList<TrailerJsonObject> listTrailersObject = new ArrayList<TrailerJsonObject>();
        JSONObject jsonObject = new JSONObject(movieJsonString);
        JSONObject trailersJsonObject = jsonObject.getJSONObject("trailers");
        JSONArray youtubeJsonArray = trailersJsonObject.getJSONArray("youtube");
        for(int i=0;i<youtubeJsonArray.length();i++){
            trailerJsonObject= new TrailerJsonObject();
            JSONObject youtubeJsonObject = (JSONObject)youtubeJsonArray.get(i);
            trailerJsonObject.setName(youtubeJsonObject.getString("name"));
            trailerJsonObject.setSize(youtubeJsonObject.getString("size"));
            trailerJsonObject.setSource(youtubeJsonObject.getString("source"));
            trailerJsonObject.setType(youtubeJsonObject.getString("type"));
            listTrailersObject.add(trailerJsonObject);
        }


        return listTrailersObject;
    }

    public static ArrayList<ReviewJsonObject> getMovieReviewsFromJson(String movieJsonString)
            throws JSONException {
        ReviewJsonObject reviewJsonObject;
        ArrayList<ReviewJsonObject> listReviewsObject = new ArrayList<ReviewJsonObject>();
        JSONObject jsonObject = new JSONObject(movieJsonString);
        JSONObject reviewsJsonObject = jsonObject.getJSONObject("reviews");
        JSONArray resultJsonArray = reviewsJsonObject.getJSONArray("results");
        for(int i=0;i<resultJsonArray.length();i++){
            reviewJsonObject= new ReviewJsonObject();
            JSONObject resultJsonObject = (JSONObject)resultJsonArray.get(i);
            reviewJsonObject.setId(resultJsonObject.getString("id"));
            reviewJsonObject.setAuthor(resultJsonObject.getString("author"));
            reviewJsonObject.setContent(resultJsonObject.getString("content"));
            reviewJsonObject.setUrl(resultJsonObject.getString("url"));
            listReviewsObject.add(reviewJsonObject);
        }


        return listReviewsObject;
    }
}
