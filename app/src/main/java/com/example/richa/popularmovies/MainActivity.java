package com.example.richa.popularmovies;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.richa.popularmovies.Json.MovieJsonObject;
import com.example.richa.popularmovies.Json.TrailersReviews;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements MainTaskFragment.TaskCallbacks,
        SharedPreferences.OnSharedPreferenceChangeListener,
        MainViewFragment.callback,
        TrailersReviewsTaskFragment.TrailersReviewsTaskCallbacks,
        DetailViewFragment.localDbChangeListener{

    private static final String TAG_TASK_FRAGMENT = "task_fragment";
    private static final String TAG_VIEW_FRAGMENT = "view_fragment";
    private static final String TAG_DETAIL_TASK_FRAGMENT = "detail_task_fragment";
    private static final String TAG_DETAIL_VIEW_FRAGMENT = "detail_view_fragment";
    private static final String TAG_TRAILERS = "Trailers";
    private static final String TAG_TRAILERS_FRAGMENT = "trailer_view_fragment";
    private TrailersReviewsTaskFragment mTrailersTaskFragment;
    private TrailersReviewsViewFragment mTrailersReviewsViewFragment;
    private static final String TAG_MOVIES = "Movies";
    private ArrayList<MovieJsonObject> mMovies;
    private boolean mTwoPane;
    private boolean mLocalDbChangeFlag=false;
    private static final int REQ_CODE = 0;



    public boolean ismTwoPane() {
        return mTwoPane;
    }

    public void setmTwoPane(boolean mTwoPane) {
        this.mTwoPane = mTwoPane;
    }

    /*based on the design pattern documented here:
        http://www.androiddesignpatterns.com/2013/04/retaining-objects-across-config-changes.html */
    private MainTaskFragment mMainTaskFragment; //Fragment associated with Async Task
    private MainViewFragment mMainViewFragment; //Fragment associated with layout Views
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // mMovies = new ArrayList<MovieJsonObject>();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);

        fm = getSupportFragmentManager();
        mMainTaskFragment = (MainTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mMainTaskFragment == null) {
            loadTaskFragment();
        }
        mMainViewFragment = (MainViewFragment) fm.findFragmentByTag(TAG_VIEW_FRAGMENT);
        if (mMovies !=null) {
            loadViewFragment();
        }

        if (findViewById(R.id.detail_container) != null)
            mTwoPane = true;
        else
            mTwoPane = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if shared preference has changed then execute async task again
        if ((mMainTaskFragment.getmSharedPreferenceChangeFlag() == 1) ||(mLocalDbChangeFlag==true))
        {
            //Reload task fragment
            if (mMainTaskFragment != null) {
                fm.beginTransaction().remove(mMainTaskFragment).commit();
                loadTaskFragment();
            }
            mMainTaskFragment.setmSharedPreferenceChangeFlag(0);


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
            if (mMainViewFragment !=null) {

                fm.beginTransaction().remove(mMainViewFragment).commit();
                if (mTwoPane == true) {
                    DetailViewFragment detailViewFragment = (DetailViewFragment) fm.findFragmentByTag(TAG_DETAIL_VIEW_FRAGMENT);

                    if (detailViewFragment != null)
                        fm.beginTransaction().remove(detailViewFragment).commit();

                    TrailersReviewsViewFragment trailersReviewsViewFragment = (TrailersReviewsViewFragment) fm.findFragmentByTag(TAG_TRAILERS_FRAGMENT);

                    if (detailViewFragment != null)
                        fm.beginTransaction().remove(trailersReviewsViewFragment).commit();
                }
            }
            loadViewFragment();
            if (movieJsonObjects.size()==0)
                Toast.makeText(this,"No movies found. Please change the sort order setting.",Toast.LENGTH_SHORT).show();

        }else
            Toast.makeText(this,"No movies found.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        mMainTaskFragment.setmSharedPreferenceChangeFlag(1);
    }

    private void loadViewFragment(){
        mMainViewFragment = new MainViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(TAG_MOVIES,mMovies);
        mMainViewFragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.container, mMainViewFragment, TAG_VIEW_FRAGMENT).commit();
    }

    private void loadTaskFragment(){
        mMainTaskFragment = new MainTaskFragment();
        fm.beginTransaction().add(mMainTaskFragment, TAG_TASK_FRAGMENT).commit();
    }

    @Override
    public void onItemSelected(MovieJsonObject movieJsonObject) {
        if (mTwoPane ==true){
            Bundle args = new Bundle();
            args.putParcelable("MovieObject",movieJsonObject);

            DetailViewFragment fragment = new DetailViewFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment, TAG_DETAIL_VIEW_FRAGMENT)
                    .commit();
            loadTrailersTaskFragment(movieJsonObject.getId());


        }else{
            Intent i = new Intent(this,DetailActivity.class);
            Bundle b = new Bundle();
            b.putParcelable("MovieObject",movieJsonObject);
            i.putExtras(b);
            startActivityForResult(i, REQ_CODE);
        }
    }

    @Override
    public void onPostExecute(TrailersReviews trailersReviews) {
        if (trailersReviews!=null ){
            loadTrailersFragment(trailersReviews);
        }
    }

    private void loadTrailersTaskFragment(String movieId){
        mTrailersTaskFragment = new TrailersReviewsTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putString("MovieId",movieId);
        mTrailersTaskFragment.setArguments(bundle);
        fm.beginTransaction().add(mTrailersTaskFragment, TAG_DETAIL_TASK_FRAGMENT).commit();
    }
    private void loadTrailersFragment(TrailersReviews trailersReviews){
        mTrailersReviewsViewFragment = new TrailersReviewsViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG_TRAILERS, trailersReviews);
        mTrailersReviewsViewFragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.detail_container, mTrailersReviewsViewFragment,TAG_TRAILERS_FRAGMENT).commit();
    }

    @Override
    public void onDbChange() {
        //if a movie is removed from the local favorite movie db then refresh main grid view to remove the movie from grid as well
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrderPref = sharedPref.getString(getString(R.string.pref_sort_order_key), getString(R.string.pref_sort_order_default));
        if (sortOrderPref.equals("3"))
            mLocalDbChangeFlag = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode ==REQ_CODE) && (resultCode == RESULT_OK)){
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            String sortOrderPref = sharedPref.getString(getString(R.string.pref_sort_order_key), getString(R.string.pref_sort_order_default));
            if (sortOrderPref.equals("3"))
                mLocalDbChangeFlag = true;
        }
    }
}
