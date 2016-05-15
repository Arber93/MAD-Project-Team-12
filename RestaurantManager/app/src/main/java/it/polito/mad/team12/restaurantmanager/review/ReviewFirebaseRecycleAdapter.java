package it.polito.mad.team12.restaurantmanager.review;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;

import it.polito.mad.team12.restaurantmanager.R;

/**
 * Created by Antonio on 10/05/16.
 */
public class ReviewFirebaseRecycleAdapter extends FirebaseRecyclerAdapter<Review,ReviewFirebaseRecycleAdapter.MyViewReviewHolder> {

    static OnItemClickListener mItemClickListener;

    public ReviewFirebaseRecycleAdapter(Class<Review> modelClass, int modelLayout, Class<MyViewReviewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    public ReviewFirebaseRecycleAdapter(Class<Review> modelClass, int modelLayout, Class<MyViewReviewHolder> viewHolderClass, Firebase ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(MyViewReviewHolder myViewReviewHolder, Review review, int i) {

        myViewReviewHolder.title.setText(review.getTitle());
        myViewReviewHolder.ratingBar.setRating(Float.parseFloat(review.getStars()));
        myViewReviewHolder.user.setText(review.getUserID());
        myViewReviewHolder.text.setText(review.getText());
        myViewReviewHolder.data.setText(review.getDataReview());
        myViewReviewHolder.reviewID.setText(review.getReviewID());

    }


    public static class MyViewReviewHolder extends RecyclerView.ViewHolder{

        TextView title;
        RatingBar ratingBar;
        TextView user;
        TextView text;
        TextView data;
        TextView link;
        TextView reviewID;

        public MyViewReviewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.review_title);
            ratingBar = (RatingBar) itemView.findViewById(R.id.review_ratingBar);
            user = (TextView) itemView.findViewById(R.id.review_user);
            data = (TextView) itemView.findViewById(R.id.review_date);
            text = (TextView) itemView.findViewById(R.id.review_text);
            link = (TextView) itemView.findViewById(R.id.review_link);
            reviewID = (TextView) itemView.findViewById(R.id.review_id);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null)
                        mItemClickListener.onItemClick(v);
                }
            });
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
