package com.example.richa.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.richa.popularmovies.Json.MovieJsonObject;

import java.util.ArrayList;

/**
 * Created by Richa on 8/28/15.
 */
public class MainViewFragment extends Fragment {
    private MovieAdapter mAdapter;
    private ArrayList<MovieJsonObject> mMovies;
    private static final String TAG_MOVIES = "Movies";


    public MainViewFragment() {

    }

    public interface callback{
        public void onItemSelected(MovieJsonObject movieJsonObject);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);
        mMovies = getArguments().getParcelableArrayList(TAG_MOVIES);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridview = (GridView) rootView.findViewById(R.id.gridView);
        if (getScreenOrientation()==Configuration.ORIENTATION_PORTRAIT) {
            Configuration config = getActivity().getResources().getConfiguration();
            if (config.smallestScreenWidthDp >= 600)
               gridview.setNumColumns(6);
            else
                gridview.setNumColumns(3);
        }
        else
            gridview.setNumColumns(5);
        mAdapter = new MovieAdapter(getActivity(),mMovies);
        gridview.setAdapter(mAdapter);



        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


                ((callback)getActivity()).onItemSelected(mAdapter.getItem(position));

            }
        });
        return rootView;
    }

    public int getScreenOrientation()
    {
        int orientation;
        if(getResources().getDisplayMetrics().widthPixels<getResources().getDisplayMetrics().heightPixels){
            orientation = Configuration.ORIENTATION_PORTRAIT;
        }else {
            orientation = Configuration.ORIENTATION_LANDSCAPE;
        }

        return orientation;
    }


}
