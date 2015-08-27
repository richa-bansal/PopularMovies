package com.example.richa.popularmovies;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            TextView titleTV = (TextView)rootView.findViewById(R.id.titleTextView);
            TextView overviewTV = (TextView)rootView.findViewById(R.id.overviewTextView);
            TextView releaseDateTV = (TextView)rootView.findViewById(R.id.releaseDate);
            TextView voteTV = (TextView)rootView.findViewById(R.id.voteTextView);
            ImageView imageView = (ImageView)rootView.findViewById(R.id.posterThumbnail);

            Intent i = getActivity().getIntent();

            titleTV.setText(i.getStringExtra("Title"));
            overviewTV.setText(i.getStringExtra("Overview"));
            releaseDateTV.setText(i.getStringExtra("Release").substring(0,4));
            voteTV.setText(i.getStringExtra("Rating")+"/10");
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + i.getStringExtra("PosterPath")).into(imageView);

            return rootView;
        }
    }
}
