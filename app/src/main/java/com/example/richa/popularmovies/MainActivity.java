package com.example.richa.popularmovies;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements TaskFragment.TaskCallbacks, SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG_TASK_FRAGMENT = "task_fragment";
    private static final String TAG_VIEW_FRAGMENT = "view_fragment";
    private static final String TAG_MOVIES = "Movies";
    private ArrayList<MovieJsonObject> mMovies;

    /*based on the design pattern documented here:
    http://www.androiddesignpatterns.com/2013/04/retaining-objects-across-config-changes.html */
    private TaskFragment mTaskFragment; //Fragment associated with Async Task
    private ViewFragment mViewFragment; //Fragment associated with layout Views
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // mMovies = new ArrayList<MovieJsonObject>();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        fm = getSupportFragmentManager();
        mTaskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null) {
            loadTaskFragment();
        }
        mViewFragment = (ViewFragment) fm.findFragmentByTag(TAG_VIEW_FRAGMENT);
        if (mMovies !=null) {
            loadViewFragment();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //if shared preference has changed then execute async task again
        if (mTaskFragment.getmSharedPreferenceChangeFlag() == 1) {
            //Reload task fragment
            if (mTaskFragment != null) {
                fm.beginTransaction().remove(mTaskFragment).commit();
                loadTaskFragment();
            }
            mTaskFragment.setmSharedPreferenceChangeFlag(0);


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent i = new Intent(this,SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostExecute(ArrayList<MovieJsonObject> movieJsonObjects) {

        if (movieJsonObjects !=null) {
            mMovies = movieJsonObjects;
            //Reload view fragment with updated data
            if (mViewFragment !=null) {

                fm.beginTransaction().remove(mViewFragment).commit();
            }
                loadViewFragment();

        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        mTaskFragment.setmSharedPreferenceChangeFlag(1);
    }

    private void loadViewFragment(){
        mViewFragment = new ViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(TAG_MOVIES,mMovies);
        mViewFragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.container, mViewFragment, TAG_VIEW_FRAGMENT).commit();
    }

    private void loadTaskFragment(){
        mTaskFragment = new TaskFragment();
        fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
    }
}
