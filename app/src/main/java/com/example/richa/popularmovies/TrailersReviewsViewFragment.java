package com.example.richa.popularmovies;



import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.richa.popularmovies.Json.TrailersReviews;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrailersReviewsViewFragment extends Fragment {

    private TrailersReviews mTrailersReviews;
    private static final String TAG_TRAILERS = "Trailers";
    private ShareActionProvider mShareActionProvider;
    private Intent sendIntent;
    private String firstTrailerId;

    public TrailersReviewsViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTrailersReviews = getArguments().getParcelable(TAG_TRAILERS);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_trailers, container, false);
        LinearLayout linearLayoutTrailers = (LinearLayout)rootView.findViewById(R.id.listTrailers);
        LinearLayout linearLayoutReviews = (LinearLayout)rootView.findViewById(R.id.listReviews);

       for (int i=0;i<mTrailersReviews.getTrailerJsonObjects().size();i++) {
           final int j = i;
           View listItem = inflater.inflate(R.layout.list_trailers, null);
           TextView textView = (TextView) listItem.findViewById(R.id.trailerName);
           textView.setText(mTrailersReviews.getTrailerJsonObjects().get(i).getName());
           textView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   watchYoutubeVideo(mTrailersReviews.getTrailerJsonObjects().get(j).getSource());
               }
           });
           ImageButton button = (ImageButton) listItem.findViewById(R.id.playButton);
           button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   watchYoutubeVideo(mTrailersReviews.getTrailerJsonObjects().get(j).getSource());
               }
           });

           linearLayoutTrailers.addView(listItem);


           if (i==0) {
               firstTrailerId = mTrailersReviews.getTrailerJsonObjects().get(j).getSource();

           }
       }

       if (mTrailersReviews.getTrailerJsonObjects().size()==0){
           TextView tv = new TextView(getActivity());
           tv.setText("No Trailers Available");
           tv.setLayoutParams(new ViewGroup.LayoutParams(
                   ViewGroup.LayoutParams.WRAP_CONTENT,
                   ViewGroup.LayoutParams.WRAP_CONTENT));
           linearLayoutTrailers.addView(tv);
           ViewGroup.MarginLayoutParams lpt =(ViewGroup.MarginLayoutParams)tv.getLayoutParams();
           lpt.setMargins(20,lpt.topMargin,lpt.rightMargin,lpt.bottomMargin);

       }

       for (int i=0;i<mTrailersReviews.getReviewJsonObjects().size();i++){
           final int j = i;
           View listItemReview = inflater.inflate(R.layout.list_reviews, null);
           TextView textViewAuthor = (TextView)listItemReview.findViewById(R.id.author);
           TextView textViewReview = (TextView)listItemReview.findViewById(R.id.review);
           textViewAuthor.setText("By: "+mTrailersReviews.getReviewJsonObjects().get(j).getAuthor());
           textViewReview.setText(mTrailersReviews.getReviewJsonObjects().get(j).getContent());
           linearLayoutReviews.addView(listItemReview);
       }
        if (mTrailersReviews.getReviewJsonObjects().size()==0){
            TextView tv = new TextView(getActivity());
            tv.setText("No Reviews Available");
            tv.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            linearLayoutReviews.addView(tv);
            ViewGroup.MarginLayoutParams lpt =(ViewGroup.MarginLayoutParams)tv.getLayoutParams();
            lpt.setMargins(20,lpt.topMargin,lpt.rightMargin,lpt.bottomMargin);
        }

        return rootView;
    }

    private void watchYoutubeVideo(String id){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Intent intent=new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v="+id));
            startActivity(intent);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_trailer_fragment, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        //Create shareIntent with first trailer
        sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" +firstTrailerId );
        sendIntent.setType("text/plain");
        setShareIntent(sendIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

}
