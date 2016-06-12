package it.polito.mad.team12.restaurantmanager.review;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import it.polito.mad.team12.restaurantmanager.R;
import it.polito.mad.team12.restaurantmanager.Utility;

/**
 * Created by Antonio on 09/05/16.
 */
public class ReviewDialogInsert extends DialogFragment {

    private View myFragmentId;
    ReviewDialogInsert oReviewDialogInsert;
    String restaurantID;
    String userID;

    public static ReviewDialogInsert newInstance(String userID,String restaurantID) {

        Bundle args = new Bundle();
        args.putString("user", userID);
        args.putString("restaurantID", restaurantID);

        ReviewDialogInsert fragment = new ReviewDialogInsert();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragmentId = inflater.inflate(R.layout.reviews_insert, container, false);

        oReviewDialogInsert=this;

        (((EditText) myFragmentId.findViewById(R.id.review_insert_title))).setText("");
        ((RatingBar) myFragmentId.findViewById(R.id.review_ratingBar_insert)).setRating(0f);
        ((RatingBar) myFragmentId.findViewById(R.id.review_ratingBar_insert)).setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ((TextView) myFragmentId.findViewById(R.id.review_score)).setText(rating + "");
            }
        });

        userID = getArguments().getString("user");
        restaurantID = getArguments().getString("restaurantID");
        ((EditText) myFragmentId.findViewById(R.id.review_insert)).setText("");
        ((TextView) myFragmentId.findViewById(R.id.review_score)).setText("0");

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"fontawesome-webfont.ttf");
        ((Button) myFragmentId.findViewById(R.id.review_insert_save)).setTypeface(font);
        ((Button) myFragmentId.findViewById(R.id.review_insert_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((((EditText) myFragmentId.findViewById(R.id.review_insert_title))).getText().toString().trim().length()==0){
                    ((EditText) myFragmentId.findViewById(R.id.review_insert_title)).setError(getResources().getString(R.string.mandatory));
                    return;
                }
                if((((EditText) myFragmentId.findViewById(R.id.review_insert))).getText().toString().trim().length()==0){
                    ((EditText) myFragmentId.findViewById(R.id.review_insert)).setError(getResources().getString(R.string.mandatory));
                    return;
                }
                if((((RatingBar) myFragmentId.findViewById(R.id.review_ratingBar_insert))).getRating()==0f){
                    ((TextView) myFragmentId.findViewById(R.id.review_score_info)).setError(getResources().getString(R.string.mandatory));
                    return;
                }

                Review r = new Review();
                r.setReply("");
                r.setDataReply("");
                r.setRestaurantID(restaurantID);
                r.setUserID(userID);
                r.setTitle(((EditText) myFragmentId.findViewById(R.id.review_insert_title)).getText().toString().trim());
                r.setText(((EditText) myFragmentId.findViewById(R.id.review_insert)).getText().toString().trim());
                r.setStars(((RatingBar) myFragmentId.findViewById(R.id.review_ratingBar_insert)).getRating() + "");
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ITALIAN);
                r.setDataReview(sdf.format(new Date(System.currentTimeMillis())));


                Firebase fb = Utility.getFirebaseRoot().child("reviews");
                r.setReviewID(fb.child("reviews").push().getKey());
                fb.child("reviews/"+r.getReviewID()).setValue(r);

                oReviewDialogInsert.dismiss();
            }
        });

        ((TextView) myFragmentId.findViewById(R.id.review_close_dialog)).setTypeface(font);
        ((TextView) myFragmentId.findViewById(R.id.review_close_dialog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oReviewDialogInsert.dismiss();
            }
        });

        return myFragmentId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }
}
