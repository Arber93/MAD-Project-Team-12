package it.polito.mad.team12.restaurantmanager;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by Andrea on 08/05/2016.
 */
public class CustomerRestaurantShowMap extends DialogFragment {

    private View myFragment;

    public CustomerRestaurantShowMap(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myFragment = inflater.inflate(R.layout.customer_restaurant_showmap, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return myFragment;
    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

    }

}
