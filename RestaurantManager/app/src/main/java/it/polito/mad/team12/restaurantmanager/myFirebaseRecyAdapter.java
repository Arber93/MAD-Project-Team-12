package it.polito.mad.team12.restaurantmanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.Locale;

/**
 * Created by Andrea on 05/05/2016.
 */
public class myFirebaseRecyAdapter extends FirebaseRecyclerAdapter<RestaurantDetails,myFirebaseRecyAdapter.DetailsHolder> {


    static OnItemClickListener restListen;

    public myFirebaseRecyAdapter(Class<RestaurantDetails> modelClass, int modelLayout, Class<DetailsHolder> viewHolderClass, Firebase ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);

    }


    public myFirebaseRecyAdapter(Class<RestaurantDetails> modelClass, int modelLayout, Class<DetailsHolder> viewHolderClass, Query ref){
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(DetailsHolder detailsHolder, RestaurantDetails restaurantDetails, int i) {


        String lang= Locale.getDefault().getLanguage();
        if (lang.equals("it")){
            detailsHolder.cat.setText(restaurantDetails.getCategoryFoodIT());

        }else {

            detailsHolder.cat.setText(restaurantDetails.getCategoryFood());
        }

        detailsHolder.name.setText(restaurantDetails.getRestaurantName());
        detailsHolder.address=(restaurantDetails.getRestaurantAddress());

        String imagelogo = restaurantDetails.getRestaurantLogo();
        if (imagelogo.equals("null") == false){
            Bitmap imageforLogo = decodeBase64(imagelogo);
            detailsHolder.logo.setImageBitmap(imageforLogo);
        }
    }


    public static class DetailsHolder extends RecyclerView.ViewHolder {
        TextView name, cat;
        String address;
        ImageView logo;

        public DetailsHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.cr_restaurant_name);
            cat = (TextView) v.findViewById(R.id.cr_restaurant_description);
            logo = (ImageView) v.findViewById(R.id.cr_restaurant_image);
            address = new String();
            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (restListen != null) restListen.onItemClick(v, name.getText().toString() + " " + address);
                }
            });

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
