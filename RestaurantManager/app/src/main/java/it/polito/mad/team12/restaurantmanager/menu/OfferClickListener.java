package it.polito.mad.team12.restaurantmanager.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;

import it.polito.mad.team12.restaurantmanager.Utility;

public class OfferClickListener implements View.OnClickListener {
    private OfferData offerData;
    private Context context;
    private String restaurantName;

    private class OfferValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                offerData = dataSnapshot.getValue(OfferData.class);
            }
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }

    public OfferClickListener (String restaurantName, String offerName, Context context) {
        this.context = context;
        this.restaurantName = restaurantName;

        Utility.getMenuOffersFrom(restaurantName).orderByKey().equalTo(offerName).addListenerForSingleValueEvent(new OfferValueEventListener());
    }

    public OfferClickListener (String restaurantName, OfferData offerData, Context context) {
        this.context = context;
        this.offerData = offerData;
        this.restaurantName = restaurantName;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ShowOfferDetailsActivity.class);
        Bundle extras = new Bundle();

        Gson gson = new Gson();
        String data = gson.toJson(offerData);
        extras.putString(Utility.OFFER_DATA_KEY, data);
        extras.putString(Utility.RESTAURANT_ID_KEY, restaurantName);

        intent.putExtras(extras);
        context.startActivity(intent);
    }
}
