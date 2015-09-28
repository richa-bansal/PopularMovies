package com.example.richa.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.richa.popularmovies.FavoriteMoviesData.MovieContract;
import com.example.richa.popularmovies.FavoriteMoviesData.MovieQueryHandler;
import com.example.richa.popularmovies.Json.MovieJsonObject;
import com.squareup.picasso.Picasso;



/**
 * Created by Richa on 9/17/15.
 */
public class DetailViewFragment extends Fragment {
    private MovieJsonObject movieObject;

    public interface localDbChangeListener{
        public void onDbChange();
    }

    public DetailViewFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arg = getArguments();
        movieObject = arg.getParcelable("MovieObject");

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView titleTV = (TextView)rootView.findViewById(R.id.titleTextView);
        TextView overviewTV = (TextView)rootView.findViewById(R.id.overviewTextView);
        overviewTV.setMovementMethod(new ScrollingMovementMethod());
        TextView releaseDateTV = (TextView)rootView.findViewById(R.id.releaseDate);
        RatingBar ratingBar = (RatingBar)rootView.findViewById(R.id.ratingBar);
        TextView voteTV = (TextView)rootView.findViewById(R.id.voteTextView);
        ImageView imageView = (ImageView)rootView.findViewById(R.id.posterThumbnail);
        final ToggleButton button = (ToggleButton)rootView.findViewById(R.id.favButton);


        if (movieObject.getLocalDbRow_id() >0) {

            //check if the movie exists in the local db. if yes
            button.setChecked(true);
            button.setBackgroundColor(Color.CYAN);


        }else {
            //otherwise
            button.setChecked(false);
            button.setBackgroundColor(Color.GRAY);
        }

        button.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    addMovieToDataBase();
                    buttonView.setBackgroundColor(Color.CYAN);
                    ((localDbChangeListener)getActivity()).onDbChange();

                } else {
                    // The toggle is disabled

                    removeMovieFromDatabase();
                    buttonView.setBackgroundColor(Color.GRAY);
                    ((localDbChangeListener)getActivity()).onDbChange();
                }
            }
        });


        titleTV.setText(movieObject.getOriginal_title());

        overviewTV.setText(movieObject.getOverview());
        releaseDateTV.setText(movieObject.getRelease_date().substring(0,4));
        String rating = movieObject.getVote_average();
        voteTV.setText(rating+"/10");
        ratingBar.setRating(Float.parseFloat(rating));
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + movieObject.getPoster_path()).into(imageView);
        return rootView;
    }


    public void addMovieToDataBase(){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MovieQueryHandler movieHandler = new MovieQueryHandler(contentResolver);
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieObject.getId());
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, movieObject.getOriginal_title());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieObject.getRelease_date());
        values.put(MovieContract.MovieEntry.COLUMN_RATING, movieObject.getVote_average());
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movieObject.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movieObject.getPoster_path());

        movieHandler.startInsert(0,null,MovieContract.MovieEntry.CONTENT_URI, values);


    }

    public void removeMovieFromDatabase(){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MovieQueryHandler movieHandler = new MovieQueryHandler(contentResolver);

           String where = MovieContract.MovieEntry.COLUMN_MOVIE_ID+"="+movieObject.getId();

            movieHandler.startDelete(0,null,MovieContract.MovieEntry.CONTENT_URI,where,null);

    }


}

