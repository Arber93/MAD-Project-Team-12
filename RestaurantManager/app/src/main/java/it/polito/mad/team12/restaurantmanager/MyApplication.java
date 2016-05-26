package it.polito.mad.team12.restaurantmanager;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.firebase.client.Firebase;

/**
 * Created by HomePC on 04/05/2016.
 */
public class MyApplication extends Application {


    public void onCreate(){
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
