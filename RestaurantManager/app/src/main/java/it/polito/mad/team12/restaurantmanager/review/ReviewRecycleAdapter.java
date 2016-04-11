package it.polito.mad.team12.restaurantmanager.review;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.polito.mad.team12.restaurantmanager.R;

/**
 * Created by Antonio on 10/04/16.
 */
public class ReviewRecycleAdapter extends RecyclerView.Adapter<MyViewReviewHolder>{

    private List<Review> mData;
    private LayoutInflater mInflater;

    public ReviewRecycleAdapter(Context context, List<Review> data){
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_review,parent,false);
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
}
