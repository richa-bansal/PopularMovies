package com.example.richa.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.richa.popularmovies.Json.MovieJsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Richa on 8/25/15.
 */
public class MovieAdapter extends ArrayAdapter<MovieJsonObject>{

    public MovieAdapter(Context context, ArrayList<MovieJsonObject> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(this.getContext());
            imageView.setAdjustViewBounds(true);
           //imageView.setLayoutParams(new GridView.LayoutParams(550,760));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(this.getContext()).load("http://image.tmdb.org/t/p/w185/"+ this.getItem(position).getPoster_path() ).into(imageView);
        return imageView;
    }
}
