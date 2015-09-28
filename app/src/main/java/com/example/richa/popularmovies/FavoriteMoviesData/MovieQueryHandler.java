package com.example.richa.popularmovies.FavoriteMoviesData;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Richa on 9/24/15.
 */
public class MovieQueryHandler extends AsyncQueryHandler{
    public static final String LOG_TAG = MovieQueryHandler.class.getSimpleName();
    public MovieQueryHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        super.onInsertComplete(token, cookie, uri);
        if (uri != null)
            Log.d(LOG_TAG, "Movie inserted successfully");
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        super.onDeleteComplete(token, cookie, result);
        if (result>0)
            Log.d(LOG_TAG, "Row deleted successfully.");
    }
}
