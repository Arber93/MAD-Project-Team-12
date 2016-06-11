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

public class ItemClickListener implements View.OnClickListener {
    private final String restaurantName;
    private ItemData itemData;
    private Context context;

    private class ItemValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                itemData = dataSnapshot.getValue(ItemData.class);
            }
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }

    public ItemClickListener(String restaurantName, String itemName, Context context) {
        this.context = context;
        this.restaurantName = restaurantName;

        Utility.getMenuItemsFrom(restaurantName, true).orderByKey().equalTo(itemName).addListenerForSingleValueEvent(new ItemValueEventListener());
        Utility.getMenuItemsFrom(restaurantName, false).orderByKey().equalTo(itemName).addListenerForSingleValueEvent(new ItemValueEventListener());
    }

    public ItemClickListener (String restaurantName, ItemData itemData, Context context) {
        this.context = context;
        this.itemData = itemData;
        this.restaurantName = restaurantName;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ShowItemDetailsActivity.class);
        Bundle extras = new Bundle();

        Gson gson = new Gson();
        String data = gson.toJson(itemData);
        extras.putString(Utility.ITEM_DATA_KEY, data);
        extras.putString(Utility.RESTAURANT_ID_KEY, restaurantName);

        intent.putExtras(extras);
        context.startActivity(intent);
    }
}
