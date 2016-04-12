package it.polito.mad.team12.restaurantmanager.review;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import it.polito.mad.team12.restaurantmanager.R;

/**
 * Created by Antonio on 10/04/16.
 */
public class MyViewReviewHolder extends RecyclerView.ViewHolder {

    TextView title;
    RatingBar ratingBar;
    TextView user;
    TextView text;
    TextView data;
    TextView link;


    public MyViewReviewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.review_title);
        ratingBar = (RatingBar) itemView.findViewById(R.id.review_ratingBar);
        user = (TextView) itemView.findViewById(R.id.review_user);
        data = (TextView) itemView.findViewById(R.id.review_date);
        text = (TextView) itemView.findViewById(R.id.review_text);
        link = (TextView) itemView.findViewById(R.id.review_link);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Click on -> ", title.getText().toString());
            }
        });
    }

    public void setData(Review current) {
        this.title.setText(current.getTitle());
        this.ratingBar.setRating(current.getStars());
        this.user.setText(current.getUserID());
        this.text.setText(current.getText());
        this.data.setText(current.getDataReview());
    }
}
