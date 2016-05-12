package it.polito.mad.team12.restaurantmanager.review;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import it.polito.mad.team12.restaurantmanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsInsertFragment extends Fragment {
    /* This fragment will host all reviews allowing the manager to reply if he wants to. */
    private View myFragmentId;
    private RatingBar ratingBar;
    private TextView numberOfReviews;
    private TextView nameRestaurant;
    private ImageView restaurantImage;
    private TextView rating;
    private TextView score;
    String restaurantID = "Nome Ristorante1";
    String userID = "Antonio";


    /****
     * Before call the Review fragment, it must call this method
     * ****   try {
     * ReviewUtility.loadJSONFromAsset(getAssets().open("reviews.json"));
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * ****   For read and load the review by the json file
     */


    //********** When it create a ReviewsFragment, it must create a Bundle and it passed by argument
    //          with the name of the restaurant
    //This is an example:
    //Bundle bundle = new Bundle();
    //bundle.putString("restaurantID","Nome Ristorante1");
    //reviewsFragment.setArguments(bundle);
    public ReviewsInsertFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentId = inflater.inflate(R.layout.fragment_reviews_insert, container, false);

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

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"fontawesome-webfont.ttf");
        ((TextView) myFragmentId.findViewById(R.id.review_write_icon)).setTypeface(font);

        ((TextView) myFragmentId.findViewById(R.id.review_write)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = ReviewDialogInsert.newInstance(userID, restaurantID);
                newFragment.show(getFragmentManager(), "dialog");
            }
        });
        ((TextView) myFragmentId.findViewById(R.id.review_write_icon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = ReviewDialogInsert.newInstance(userID,restaurantID);
                newFragment.show(getFragmentManager(),"dialog");
            }
        });

        return myFragmentId;
    }

    private void setUpRecyclerView(String retaurantID) {
        RecyclerView recyclerView = (RecyclerView) myFragmentId.findViewById(R.id.review_recyclerViewMain);
        recyclerView.setHasFixedSize(true);
        ReviewRecycleAdapter adapter = new ReviewRecycleAdapter(getContext(), ReviewUtility.getReviews(retaurantID));
        adapter.SetOnItemClickListener(new ReviewRecycleAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v) {
                DialogFragment newFragment = ReviewDialogIFragment.newInstance(v,restaurantID);
                newFragment.show(getFragmentManager(),"dialog");
            }
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getContext());
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

}
