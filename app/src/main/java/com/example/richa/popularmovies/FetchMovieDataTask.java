package com.example.richa.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Adapter;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Richa on 8/24/15.
 */
public class FetchMovieDataTask extends AsyncTask<String,Void,ArrayList<MovieJsonObject>> {
    private MovieAdapter mAdapter;
    private final String LOG_TAG = getClass().getSimpleName();
    private final String API_KEY = "";

    public FetchMovieDataTask(MovieAdapter adapter){
        mAdapter = adapter;
    }
    @Override
    protected ArrayList<MovieJsonObject> doInBackground(String... params) {

        String sortOrder;
        if (params[0].equals("2"))
            sortOrder = "vote_average.desc";
        else
            sortOrder = "popularity.desc";
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;
        try {

            //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=01e1a5dfefecda457b4ebe0d8d218a5c
            Uri.Builder builder = new Uri.Builder();
            Uri builtUri = builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("sort_by", sortOrder)
                    .appendQueryParameter("api_key", API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
// Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
// But it does make debugging a *lot* easier if you print out the completed
// buffer for debugging.
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
// Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();
            //Log.d(LOG_TAG,movieJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error " +e);
            return null;
        }  finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
       try {
            return MovieJsonParser.getMovieDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieJsonObject> movieJsonObjects) {
        if (movieJsonObjects !=null){
        mAdapter.clear();
        mAdapter.addAll(movieJsonObjects );
        mAdapter.notifyDataSetChanged();}
    }
}
