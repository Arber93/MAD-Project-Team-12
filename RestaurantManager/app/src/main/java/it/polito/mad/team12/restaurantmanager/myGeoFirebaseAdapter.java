package it.polito.mad.team12.restaurantmanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Andrea on 07/05/2016.
 */
public class myGeoFirebaseAdapter extends RecyclerView.Adapter<myGeoFirebaseAdapter.GeoResViewHolder> {


    static OnItemClickListener restListen;

    private LinkedList<RestaurantDetails> allR = new LinkedList<RestaurantDetails>();
    private LayoutInflater inflater;

    public myGeoFirebaseAdapter(Context c,LinkedList<RestaurantDetails> dr){
        this.inflater = LayoutInflater.from(c);
        this.allR=dr;
    }

    @Override
    public myGeoFirebaseAdapter.GeoResViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (View)LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_recycler_item, parent, false);
        GeoResViewHolder holder = new GeoResViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(myGeoFirebaseAdapter.GeoResViewHolder holder, int position) {

        RestaurantDetails item = allR.get(position);
        holder.setData(item, position);
    }

    @Override
    public int getItemCount() {
        return allR.size();
    }

    public class GeoResViewHolder extends RecyclerView.ViewHolder {

        TextView name, cat;
        String address;
        ImageView logo;


        public GeoResViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.cr_restaurant_name);
            cat = (TextView) v.findViewById(R.id.cr_restaurant_description);
            logo = (ImageView) v.findViewById(R.id.cr_restaurant_image);
            address = new String();
            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (restListen != null) restListen.onItemClick(v, name.getText().toString()+address);
                }
            });


        }


        public void setData(RestaurantDetails item, int position) {

            String lang= Locale.getDefault().getLanguage();
            if (lang.equals("it")){
                cat.setText(item.getCategoryFoodIT());

            }else {

                cat.setText(item.getCategoryFood());
            }

            name.setText(item.getRestaurantName());
            address=(item.getRestaurantAddress());


            String imagelogo = item.getRestaurantLogo();
            if (imagelogo.equals("null") == false){
                Bitmap imageforLogo = decodeBase64(imagelogo);
                logo.setImageBitmap(imageforLogo);
            }
        }

    }


    public interface OnItemClickListener{
        public void onItemClick(View view, String name);
    }

    public void SetOnItemClickListener(final OnItemClickListener restListen){
        this.restListen = restListen;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


}