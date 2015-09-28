package com.example.richa.popularmovies;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.richa.popularmovies.Json.MovieJsonObject;
import com.example.richa.popularmovies.Json.TrailersReviews;


public class DetailActivity extends ActionBarActivity implements TrailersReviewsTaskFragment.TrailersReviewsTaskCallbacks,
DetailViewFragment.localDbChangeListener{
    private static final String TAG_DETAIL_TASK_FRAGMENT = "detail_task_fragment";
    private static final String TAG_DETAIL_VIEW_FRAGMENT = "detail_view_fragment";
    private static final String TAG_TRAILERS = "Trailers";
    private static final String TAG_TRAILERS_FRAGMENT = "trailer_view_fragment";



    /*based on the design pattern documented here:
    http://www.androiddesignpatterns.com/2013/04/retaining-objects-across-config-changes.html */
    private TrailersReviewsTaskFragment mTaskFragment; //Fragment associated with Async Task
    private TrailersReviewsViewFragment mTrailersReviewsViewFragment;
    private DetailViewFragment mDetailViewFragment;

    private FragmentManager fm;
    private String movieId;
    private MovieJsonObject movieObject;
    private TrailersReviews mTrailersReviews;
    private boolean mLocalDbChangeFlag = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        movieObject = b.getParcelable("MovieObject");

        movieId = movieObject.getId();

        fm = getSupportFragmentManager();
        mTaskFragment = (TrailersReviewsTaskFragment) fm.findFragmentByTag(TAG_DETAIL_TASK_FRAGMENT);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null) {
            loadTaskFragment();
        }
        mDetailViewFragment = (DetailViewFragment)fm.findFragmentByTag(TAG_DETAIL_VIEW_FRAGMENT);
        //load DetailViewFragment
        if (mDetailViewFragment == null) {
            mDetailViewFragment = new DetailViewFragment();
            Bundle arg = new Bundle();
            arg.putParcelable("MovieObject",movieObject);
            mDetailViewFragment.setArguments(arg);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mDetailViewFragment, TAG_DETAIL_VIEW_FRAGMENT)
                    .commit();
        }
        //load TrailersFragment

        if (mTrailersReviews!=null)
            loadTrailersFragment();


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

    private void loadTaskFragment(){
        mTaskFragment = new TrailersReviewsTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putString("MovieId",movieId);
        mTaskFragment.setArguments(bundle);
        fm.beginTransaction().add(mTaskFragment, TAG_DETAIL_TASK_FRAGMENT).commit();
    }
    private void loadTrailersFragment(){
        mTrailersReviewsViewFragment = new TrailersReviewsViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG_TRAILERS, mTrailersReviews);
        mTrailersReviewsViewFragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.container, mTrailersReviewsViewFragment,TAG_TRAILERS_FRAGMENT).commit();
    }


    @Override
    public void onPostExecute(TrailersReviews trailersReviews) {

        mTrailersReviewsViewFragment = (TrailersReviewsViewFragment) fm.findFragmentByTag(TAG_TRAILERS_FRAGMENT);
        if (trailersReviews!=null ){
            mTrailersReviews = trailersReviews;
            if (mTrailersReviewsViewFragment !=null){
                fm.beginTransaction().remove(mTrailersReviewsViewFragment).commit();
            }
            loadTrailersFragment();
        }
    }

    @Override
    public void onDbChange() {
        mLocalDbChangeFlag = true;
        Intent resultIntent = new Intent();
        resultIntent.putExtra("LocalDbChangeFlag",mLocalDbChangeFlag);
        setResult(RESULT_OK, resultIntent);
    }
}
