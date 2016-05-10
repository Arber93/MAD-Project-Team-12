package it.polito.mad.team12.restaurantmanager.review;

import android.app.Dialog;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

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

    public static ReviewDialogFragment newInstance(View detail,String restaurantID) {

        Bundle args = new Bundle();
        args.putString("title", ((TextView) detail.findViewById(R.id.review_title)).getText().toString());
        args.putString("user",((TextView) detail.findViewById(R.id.review_user)).getText().toString());
        args.putString("data",((TextView) detail.findViewById(R.id.review_date)).getText().toString());
        args.putString("text",((TextView) detail.findViewById(R.id.review_text)).getText().toString());
        args.putString("restaurantID",restaurantID);
        args.putFloat("ratingBar",((RatingBar) detail.findViewById(R.id.review_ratingBar)).getRating());

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
        final String restaurantID = getArguments().getString("restaurantID");
        for(Review r : ReviewUtility.getReviews(restaurantID)){
            if(r.getTitle().equals(getArguments().getString("title")) && r.getReply().trim().length()>0){
                ((TextView) myFragmentId.findViewById(R.id.review_reply)).setText(r.getReply());
                (myFragmentId.findViewById(R.id.review_reply_et)).setEnabled(false);
                (myFragmentId.findViewById(R.id.sendReview )).setEnabled(false);
                break;
            }
        }

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"fontawesome-webfont.ttf");
        final Button button = (Button)myFragmentId.findViewById(R.id.sendReview );
        button.setTypeface(font);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((EditText) myFragmentId.findViewById(R.id.review_reply_et)).getText().toString().trim().length()>0) {
                    ((TextView) myFragmentId.findViewById(R.id.review_reply)).setText(((EditText) myFragmentId.findViewById(R.id.review_reply_et)).getText());
                    ((EditText) myFragmentId.findViewById(R.id.review_reply_et)).setText("");
                    (myFragmentId.findViewById(R.id.review_reply_et)).setEnabled(false);
                    button.setEnabled(false);
                    Log.d("->", restaurantID + " - " + (((TextView) myFragmentId.findViewById(R.id.review_title))).getText().toString());
                    for (Review r : ReviewUtility.getReviews(restaurantID)) {
                        if (r.getTitle().equals((((TextView) myFragmentId.findViewById(R.id.review_title))).getText().toString())) {
                            r.setReply(((TextView) myFragmentId.findViewById(R.id.review_reply)).getText().toString());
                            break;
                        }
                    }
                }
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
