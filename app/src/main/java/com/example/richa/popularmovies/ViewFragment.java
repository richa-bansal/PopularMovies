package com.example.richa.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by Richa on 8/28/15.
 */
public class ViewFragment extends Fragment {
    private MovieAdapter mAdapter;
    private ArrayList<MovieJsonObject> mMovies;
    private static final String TAG_MOVIES = "Movies";

    public ViewFragment() {

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
        mAdapter = new MovieAdapter(getActivity(),mMovies);
        gridview.setAdapter(mAdapter);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent i = new Intent(getActivity(),DetailActivity.class);
                i.putExtra("Title", mAdapter.getItem(position).getOriginal_title());
                i.putExtra("Overview",mAdapter.getItem(position).getOverview());
                i.putExtra("Release",mAdapter.getItem(position).getRelease_date());
                i.putExtra("Rating",mAdapter.getItem(position).getVote_average());
                i.putExtra("PosterPath",mAdapter.getItem(position).getPoster_path());

                startActivity(i);
            }
        });
        return rootView;
    }


}
