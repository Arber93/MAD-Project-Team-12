package it.polito.mad.team12.restaurantmanager.review;

import android.app.Dialog;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

/**
 * Created by Antonio on 26/04/16.
 */
public class ReviewDialogFragment extends DialogFragment {

    private View myFragmentId;
    TextView title;
    RatingBar ratingBar;
    TextView user;
    TextView text;
    TextView data;
    TextView link;

    String reviewID;

    ReviewDialogFragment oReviewDialogFragment;

    public static ReviewDialogFragment newInstance(View detail,String restaurantID) {

        Bundle args = new Bundle();
        args.putString("title", ((TextView) detail.findViewById(R.id.review_title)).getText().toString());
        args.putString("user",((TextView) detail.findViewById(R.id.review_user)).getText().toString());
        args.putString("data",((TextView) detail.findViewById(R.id.review_date)).getText().toString());
        args.putString("text", ((TextView) detail.findViewById(R.id.review_text)).getText().toString());
        args.putString("restaurantID", restaurantID);
        args.putFloat("ratingBar", ((RatingBar) detail.findViewById(R.id.review_ratingBar)).getRating());
        args.putString("reviewID", ((TextView) detail.findViewById(R.id.review_id)).getText().toString());

        ReviewDialogFragment fragment = new ReviewDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragmentId = inflater.inflate(R.layout.reviews_detail_replay, container, false);
        (((TextView) myFragmentId.findViewById(R.id.review_title))).setText(getArguments().getString("title"));
        ((RatingBar) myFragmentId.findViewById(R.id.review_ratingBar)).setRating(getArguments().getFloat("ratingBar"));
        ((TextView) myFragmentId.findViewById(R.id.review_user)).setText(getArguments().getString("user"));
        ((TextView) myFragmentId.findViewById(R.id.review_date)).setText(getArguments().getString("data"));
        ((TextView) myFragmentId.findViewById(R.id.review_text)).setText(getArguments().getString("text"));
        reviewID = getArguments().getString("reviewID");
        final String restaurantID = getArguments().getString("restaurantID");
        oReviewDialogFragment = this;

        if(ReviewUtility.getReview(reviewID).getReply().trim().length()>0) {
            ((TextView) myFragmentId.findViewById(R.id.review_reply)).setText(ReviewUtility.getReview(reviewID).getReply());
            ((TextView) myFragmentId.findViewById(R.id.review_dataReply)).setText(ReviewUtility.getReview(reviewID).getDataReply());
            (myFragmentId.findViewById(R.id.review_reply_et)).setVisibility(View.INVISIBLE);
            (myFragmentId.findViewById(R.id.sendReview)).setVisibility(View.INVISIBLE);
            (myFragmentId.findViewById(R.id.review_delete_button)).setVisibility(View.VISIBLE);
            (myFragmentId.findViewById(R.id.review_delete_text)).setVisibility(View.VISIBLE);
        }else{
            ((TextView) myFragmentId.findViewById(R.id.review_reply)).setText("");
            ((TextView) myFragmentId.findViewById(R.id.review_dataReply)).setText("");
            (myFragmentId.findViewById(R.id.review_reply_et)).setVisibility(View.VISIBLE);
            (myFragmentId.findViewById(R.id.sendReview)).setVisibility(View.VISIBLE);
            (myFragmentId.findViewById(R.id.review_delete_button)).setVisibility(View.INVISIBLE);
            (myFragmentId.findViewById(R.id.review_delete_text)).setVisibility(View.INVISIBLE);
        }

        ((TextView) myFragmentId.findViewById(R.id.review_close_dialog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oReviewDialogFragment.dismiss();
            }
        });

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"fontawesome-webfont.ttf");
        final Button button = (Button)myFragmentId.findViewById(R.id.sendReview );
        final Button deleteButton = (Button)myFragmentId.findViewById(R.id.review_delete_button);
        button.setTypeface(font);
        deleteButton.setTypeface(font);
        ((TextView) myFragmentId.findViewById(R.id.review_close_dialog)).setTypeface(font);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((EditText) myFragmentId.findViewById(R.id.review_reply_et)).getText().toString().trim().length() > 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ITALIAN);

                    ((TextView) myFragmentId.findViewById(R.id.review_reply)).setText(((EditText) myFragmentId.findViewById(R.id.review_reply_et)).getText());
                    ((EditText) myFragmentId.findViewById(R.id.review_reply_et)).setText("");
                    ((TextView) myFragmentId.findViewById(R.id.review_dataReply)).setText(sdf.format(new Date(System.currentTimeMillis())));
                    (myFragmentId.findViewById(R.id.review_reply_et)).setVisibility(View.INVISIBLE);
                    button.setVisibility(View.INVISIBLE);
                    (myFragmentId.findViewById(R.id.review_delete_button)).setVisibility(View.VISIBLE);
                    (myFragmentId.findViewById(R.id.review_delete_text)).setVisibility(View.VISIBLE);
                    (myFragmentId.findViewById(R.id.review_reply)).setVisibility(View.VISIBLE);
                    (myFragmentId.findViewById(R.id.review_dataReply)).setVisibility(View.VISIBLE);
                    ReviewUtility.getReview(reviewID).setReply(((TextView) myFragmentId.findViewById(R.id.review_reply)).getText().toString());
                    ReviewUtility.getReview(reviewID).setDataReply(((TextView) myFragmentId.findViewById(R.id.review_dataReply)).getText().toString());
                    Firebase myFirebaseRef = new Firebase("https://restaurantaf.firebaseio.com/");
                    myFirebaseRef.child("reviews/" + reviewID + "/reply").setValue(((TextView) myFragmentId.findViewById(R.id.review_reply)).getText().toString());
                    myFirebaseRef.child("reviews/" + reviewID + "/dataReply").setValue(((TextView) myFragmentId.findViewById(R.id.review_dataReply)).getText().toString());
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReviewUtility.getReview(reviewID).setReply("");
                ((TextView) myFragmentId.findViewById(R.id.review_reply)).setText("");
                ((TextView) myFragmentId.findViewById(R.id.review_dataReply)).setText("");
                (myFragmentId.findViewById(R.id.review_reply_et)).setVisibility(View.VISIBLE);//setEnabled(false);
                button.setVisibility(View.VISIBLE);//.setEnabled(false);
                (myFragmentId.findViewById(R.id.review_delete_button)).setVisibility(View.INVISIBLE);
                (myFragmentId.findViewById(R.id.review_delete_text)).setVisibility(View.INVISIBLE);
                (myFragmentId.findViewById(R.id.review_reply)).setVisibility(View.INVISIBLE);
                (myFragmentId.findViewById(R.id.review_dataReply)).setVisibility(View.INVISIBLE);
                Firebase myFirebaseRef = new Firebase("https://restaurantaf.firebaseio.com/");
                myFirebaseRef.child("reviews/" + reviewID + "/reply").setValue("");
                myFirebaseRef.child("reviews/" + reviewID + "/dataReply").setValue("");
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
