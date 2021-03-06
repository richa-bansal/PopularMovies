package com.example.richa.popularmovies;

import android.app.Activity;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.widget.Toast;

import com.example.richa.popularmovies.FavoriteMoviesData.MovieContract;
import com.example.richa.popularmovies.Json.MovieJsonObject;
import com.example.richa.popularmovies.Json.MovieJsonParser;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Richa on 8/28/15.
 */
public class MainTaskFragment extends Fragment {



    /**
     * Callback interface through which the fragment will report the
     * task's progress and results back to the Activity.
     */
    interface TaskCallbacks {
        void onPostExecute(ArrayList<MovieJsonObject> movieJsonObjects);
    }

    private TaskCallbacks mCallbacks;
    private FetchMovieDataTask mTask;
    private FetchFavoriteMoviesTask mFavMovTask;

    private static final int MOVIE_LOADER = 0;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH
    };

    static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_TITLE = 2;
    static final int COL_RELEASE_DATE = 3;
    static final int COL_RATING = 4;
    static final int COL_OVERVIEW = 5;
    static final int COL_POSTER_PATH = 6;


    private int mSharedPreferenceChangeFlag = 0; // 1 means that Shared preference has changed and 0 means no change

    public int getmSharedPreferenceChangeFlag() {
        return mSharedPreferenceChangeFlag;
    }

    public void setmSharedPreferenceChangeFlag(int mSharedPreferenceChangeFlag) {
        this.mSharedPreferenceChangeFlag = mSharedPreferenceChangeFlag;
    }

    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks) activity;
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
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrderPref = sharedPref.getString(getString(R.string.pref_sort_order_key), getString(R.string.pref_sort_order_default));
        // Create and execute the background task.

        if (Utils.isNetworkAvailable(getActivity())) {
            if (sortOrderPref.equals("3")){
                //fetch favorite movie data from content provider
               // getLoaderManager().initLoader(MOVIE_LOADER, null, this);
                mFavMovTask = new FetchFavoriteMoviesTask();
                mFavMovTask.execute();
            }else{
                mTask = new FetchMovieDataTask();
                mTask.execute(sortOrderPref);
            }

        } else
            Toast.makeText(getActivity(), "No Network Connection", Toast.LENGTH_SHORT).show();

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



    private class FetchMovieDataTask extends AsyncTask<String,Void,ArrayList<MovieJsonObject>> {

        private final String LOG_TAG = getClass().getSimpleName();
        private final String API_KEY = "";


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
                ArrayList<MovieJsonObject> movies = MovieJsonParser.getMovieDataFromJson(movieJsonStr);

                //check if movies exists in local favorite movie database and update localDbRow_id

                for (MovieJsonObject movie:movies){

                    ContentResolver contentResolver = getActivity().getContentResolver();
                    String selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "="+movie.getId();
                    String[] MOVIE_COLUMNS = {
                            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,

                    };

                    Cursor cursor = contentResolver.query(MovieContract.MovieEntry.CONTENT_URI,MOVIE_COLUMNS,selection,null,null);

                    if (cursor.moveToFirst()){
                        movie.setLocalDbRow_id(cursor.getLong(0));
                    }
                    cursor.close();
                }


                return movies;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    @Override
    protected void onPostExecute(ArrayList<MovieJsonObject> movieJsonObjects) {

        if (mCallbacks != null) {
            mCallbacks.onPostExecute(movieJsonObjects);
        }
    }
    }


    private class FetchFavoriteMoviesTask extends AsyncTask<Void,Void,ArrayList<MovieJsonObject>>{

        @Override
        protected ArrayList<MovieJsonObject> doInBackground(Void... params) {
            Uri movieUri = MovieContract.MovieEntry.CONTENT_URI;
            ContentResolver contentResolver = getActivity().getContentResolver();
            Cursor data = contentResolver.query(movieUri,MOVIE_COLUMNS,null,null,null);

            ArrayList<MovieJsonObject> movies = new ArrayList<MovieJsonObject>();
            MovieJsonObject movieObject;
            if (data.moveToFirst()){
                for (int i=0; i<data.getCount();i++){
                    movieObject = new MovieJsonObject();
                    movieObject.setId(data.getString(COL_MOVIE_ID));
                    movieObject.setOriginal_title(data.getString(COL_TITLE));
                    movieObject.setRelease_date(data.getString(COL_RELEASE_DATE));
                    movieObject.setVote_average(data.getString(COL_RATING));
                    movieObject.setOverview(data.getString(COL_OVERVIEW));
                    movieObject.setPoster_path(data.getString(COL_POSTER_PATH));
                    movieObject.setLocalDbRow_id(data.getLong(COL_ID));
                    movies.add(movieObject);
                    data.moveToNext();
                }
                data.close();
            }
            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieJsonObject> movieJsonObjects) {
            if (mCallbacks != null) {
                mCallbacks.onPostExecute(movieJsonObjects);
            }
        }
    }
}
