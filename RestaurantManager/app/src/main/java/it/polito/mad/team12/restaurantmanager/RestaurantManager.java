package it.polito.mad.team12.restaurantmanager;

import com.firebase.client.Firebase;

public class RestaurantManager extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
