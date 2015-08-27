package com.example.richa.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Richa on 8/24/15.
 */
public class MovieJsonParser {
    public static ArrayList<MovieJsonObject> getMovieDataFromJson(String movieJsonString)
    throws JSONException
    {
        MovieJsonObject movieJsonObject;
        ArrayList<MovieJsonObject> listMovieObject = new ArrayList<MovieJsonObject>();
        JSONObject jsonObject = new JSONObject(movieJsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("results");


        for(int i=0;i<jsonArray.length();i++){
            movieJsonObject = new MovieJsonObject();
            JSONObject jsonObjectMovie = (JSONObject)jsonArray.get(i);
            movieJsonObject.setAdult(jsonObjectMovie.getString("adult"));
            movieJsonObject.setId(jsonObjectMovie.getString("id"));
            movieJsonObject.setPoster_path(jsonObjectMovie.getString("poster_path"));
            movieJsonObject.setTitle(jsonObjectMovie.getString("title"));
            movieJsonObject.setOriginal_title(jsonObjectMovie.getString("original_title"));
            movieJsonObject.setOverview(jsonObjectMovie.getString("overview"));
            movieJsonObject.setRelease_date(jsonObjectMovie.getString("release_date"));
            movieJsonObject.setVote_average(jsonObjectMovie.getString("vote_average"));
            listMovieObject.add(movieJsonObject);
        }
        return listMovieObject;
    }

}
