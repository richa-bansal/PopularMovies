package com.example.richa.popularmovies;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.example.richa.popularmovies.Json.MovieDetailJsonParser;
import com.example.richa.popularmovies.Json.ReviewJsonObject;
import com.example.richa.popularmovies.Json.TrailerJsonObject;
import com.example.richa.popularmovies.Json.TrailersReviews;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Richa on 9/17/15.
 */
public class TrailersReviewsTaskFragment extends Fragment{

    /**
     * Callback interface through which the fragment will report the
     * task's progress and results back to the Activity.
     */
    interface TrailersReviewsTaskCallbacks {
        void onPostExecute(TrailersReviews trailersReviews);
    }

    private TrailersReviewsTaskCallbacks mCallbacks;
    private FetchDetailDataTask mTask;
    private String movieId;


    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TrailersReviewsTaskCallbacks) activity;
    }

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);
        movieId = getArguments().getString("MovieId");

        // Create and execute the background task.
        if (Utils.isNetworkAvailable(getActivity())) {
            mTask = new FetchDetailDataTask();
            mTask.execute(movieId);
        }else
            Toast.makeText(getActivity(), "No Network Connection.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }



    private class FetchDetailDataTask extends AsyncTask<String,Void, TrailersReviews> {

        private final String LOG_TAG = getClass().getSimpleName();
        private final String API_KEY = "";


        @Override
        protected TrailersReviews doInBackground(String... params) {


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;
            try {


                Uri.Builder builder = new Uri.Builder();
                Uri builtUri = builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath(params[0])
                        .appendQueryParameter("api_key", API_KEY)
                        .appendQueryParameter("append_to_response", "trailers,reviews")
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
                Log.e(LOG_TAG, "Error " + e);
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
                ArrayList<TrailerJsonObject> trailerJsonObjects = MovieDetailJsonParser.getMovieTrailersFromJson(movieJsonStr);
               ArrayList<ReviewJsonObject> reviewJsonObjects = MovieDetailJsonParser.getMovieReviewsFromJson(movieJsonStr);
               TrailersReviews trailersReviews = new TrailersReviews();
               trailersReviews.setReviewJsonObjects(reviewJsonObjects);
               trailersReviews.setTrailerJsonObjects(trailerJsonObjects);
               return trailersReviews;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Log.d(LOG_TAG,movieJsonStr);
            return null;
        }

        @Override
        protected void onPostExecute(TrailersReviews trailersReviews) {
            if (mCallbacks !=null)
                mCallbacks.onPostExecute(trailersReviews);
        }
    }

}

