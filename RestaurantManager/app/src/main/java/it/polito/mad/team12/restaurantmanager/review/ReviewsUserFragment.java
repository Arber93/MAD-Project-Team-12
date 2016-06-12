package it.polito.mad.team12.restaurantmanager.review;


import android.content.SharedPreferences;
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

import it.polito.mad.team12.restaurantmanager.CustomerMainActivity;
import it.polito.mad.team12.restaurantmanager.CustomerRestaurantActivityMain;
import it.polito.mad.team12.restaurantmanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsUserFragment extends Fragment {
    /* This fragment will host all reviews allowing the manager to reply if he wants to. */
    private View myFragmentId;
    String userID;

    public ReviewsUserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentId = inflater.inflate(R.layout.fragment_user_reviews, container, false);
        CustomerMainActivity act = (CustomerMainActivity) getActivity();
        SharedPreferences pref = act.getSharedPreferences("testapp", act.MODE_PRIVATE);
        this.userID = pref.getString("nameU", null);
        setUpRecyclerView(userID);

        return myFragmentId;
    }

    private void setUpRecyclerView(final String userID) {
        RecyclerView recyclerView = (RecyclerView) myFragmentId.findViewById(R.id.review_recyclerViewMain);
        recyclerView.setHasFixedSize(true);
        ReviewRecycleAdapterUser adapter = new ReviewRecycleAdapterUser(getContext(), ReviewUtility.getReviewsForUser(userID));
        adapter.SetOnItemClickListener(new ReviewRecycleAdapterUser.OnItemClickListener() {

            @Override
            public void onItemClick(View v) {
                DialogFragment newFragment = ReviewDialogUFragment.newInstance(v,userID);
                newFragment.show(getFragmentManager(),"dialog");
            }
        });
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getContext());
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

}
