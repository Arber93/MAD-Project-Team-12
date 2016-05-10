package it.polito.mad.team12.restaurantmanager.review;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import it.polito.mad.team12.restaurantmanager.R;

/**
 * Created by Antonio on 10/04/16.
 */
public class ReviewRecycleAdapter extends RecyclerView.Adapter<ReviewRecycleAdapter.MyViewReviewHolder> {

    private List<Review> mData;
    private LayoutInflater mInflater;
    OnItemClickListener mItemClickListener;

    public ReviewRecycleAdapter(Context context, List<Review> data) {
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
    }

    public void add(Review item, int position) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Review item) {
        int position = mData.indexOf(item);
        mData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_review, parent, false);
        MyViewReviewHolder holder = new MyViewReviewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewReviewHolder holder, int position) {
        Review currentObj = mData.get(position);
        holder.setData(currentObj);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewReviewHolder extends RecyclerView.ViewHolder{

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
                    if(mItemClickListener != null)
                        mItemClickListener.onItemClick(v);
                }
            });
        }

        public void setData(Review current) {
            this.title.setText(current.getTitle());
            this.ratingBar.setRating(Float.parseFloat(current.getStars()));
            this.user.setText(current.getUserID());
            this.text.setText(current.getText());
            this.data.setText(current.getDataReview());
            this.reviewID.setText(current.getReviewID());
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
