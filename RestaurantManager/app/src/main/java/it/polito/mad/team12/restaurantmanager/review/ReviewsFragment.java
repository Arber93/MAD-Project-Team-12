package it.polito.mad.team12.restaurantmanager.review;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.io.FileInputStream;
import java.io.IOException;

import it.polito.mad.team12.restaurantmanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment {
    /* This fragment will host all reviews allowing the manager to reply if he wants to. */
    private View myFragmentId;
    private RatingBar ratingBar;
    private TextView numberOfReviews;
    private TextView nameRestaurant;
    private ImageView restaurantImage;
    private TextView rating;
    private TextView score;
    String restaurantID = "Nome Ristorante1";


    public ReviewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentId = inflater.inflate(R.layout.fragment_reviews, container, false);
        ratingBar = (RatingBar) myFragmentId.findViewById(R.id.review_rate);
        numberOfReviews = (TextView) myFragmentId.findViewById(R.id.review_number);
        nameRestaurant = (TextView) myFragmentId.findViewById(R.id.review_nameRestaurant);
        restaurantImage = (ImageView) myFragmentId.findViewById(R.id.review_restauratnImage);
        score = (TextView) myFragmentId.findViewById(R.id.review_score);
        rating = (TextView) myFragmentId.findViewById(R.id.review_info2);

        nameRestaurant.setText(restaurantID);

        setUpRecyclerView(restaurantID);

        ratingBar.setRating(ReviewUtility.getStarsRestaurant(restaurantID));
        numberOfReviews.setText(ReviewUtility.numberOfReviews(restaurantID).toString());
        if (numberOfReviews.getText().equals("1"))
            rating.setText(getResources().getText(R.string.review_info3s));
        else
            rating.setText(getResources().getText(R.string.review_info3p));
        score.setText(ratingBar.getRating() + "");
        String nome = ReviewUtility.getImageName(restaurantID);
        String uri = ":mipmap/" + nome;

        int imageResource = getResources().getIdentifier(getActivity().getPackageName() + uri, null, null);
        restaurantImage.setImageResource(imageResource);

        return myFragmentId;
    }

    private void setUpRecyclerView(String retaurantID) {
        RecyclerView recyclerView = (RecyclerView) myFragmentId.findViewById(R.id.review_recyclerViewMain);
        recyclerView.setHasFixedSize(true);
        ReviewRecycleAdapter adapter = new ReviewRecycleAdapter(getContext(), ReviewUtility.getReviews(retaurantID));
        adapter.SetOnItemClickListener(new ReviewRecycleAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v) {
                DialogFragment newFragment = ReviewDialogFragment.newInstance(v,restaurantID);
                newFragment.show(getFragmentManager(),"dialog");
            }
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getContext());
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        /*Firebase myFirebaseRef = new Firebase("https://restaurantaf.firebaseio.com/reviews");

        ReviewFirebaseRecycleAdapter adapterFirebase = new ReviewFirebaseRecycleAdapter(Review.class,
                R.layout.item_review,
                ReviewFirebaseRecycleAdapter.MyViewReviewHolder.class,
                myFirebaseRef);

        adapterFirebase.notifyDataSetChanged();
        recyclerView.setAdapter(adapterFirebase);
        adapterFirebase.notifyDataSetChanged();

        adapterFirebase.SetOnItemClickListener(new ReviewFirebaseRecycleAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v) {
                DialogFragment newFragment = ReviewDialogFragment.newInstance(v,restaurantID);
                newFragment.show(getFragmentManager(),"dialog");
            }
        });*/

    }

}
