package it.polito.mad.team12.restaurantmanager;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

public class EditPhoneDetails extends DialogFragment implements View.OnClickListener{

    public EditPhoneDetails(){

    }

    RestaurantDetails desR;

    private View myFragment;
    EditText newPhone;
    Button confirm, cancel;
    Firebase mRootRef,restaurant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myFragment=inflater.inflate(R.layout.fragment_layout_edit_phone, container,false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        newPhone = (EditText) myFragment.findViewById(R.id.phoneEdited);

        confirm = (Button) myFragment.findViewById(R.id.flep_save_button);
        cancel = (Button) myFragment.findViewById(R.id.flep_cancel_button);

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);


        restaurant.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                desR = dataSnapshot.getValue(RestaurantDetails.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        return myFragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getContext());


        MainActivity act = (MainActivity) getActivity();
        String restaurant11 = act.retrieveRestID(); // I RETRIEVE THE INFO OF THE RESTAURANT FROM HERE

        mRootRef = Utility.getFirebaseRestaurantsRef();   //ROOT of Firebase Restaurants
        restaurant = mRootRef.child(restaurant11);      //access the specified restaurant
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    public void onClick(View v) {
        if (v == cancel){
            dismiss();  //exits from dialog fragment
        }
        if (v == confirm){
            // save things into JSON updating it
           if (newPhone.getText().length() > 0 ) desR.setTelephone(newPhone.getText().toString());

            restaurant.setValue(desR);

            //exit from dialog fragment
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction;

            transaction = manager.beginTransaction();
            transaction.replace(R.id.flContent, new DetailsFragment());
            transaction.commit();
            dismiss();
        }
    }

    private void saveDataToJsonFile(){
        Gson gson = new Gson();
        Type DetailsType = new TypeToken<RestaurantDetails>(){}.getType();
        String data = gson.toJson(desR, DetailsType);
        String filename = "details.xml";

        FileOutputStream outputStream;

        try {
            outputStream = getActivity().getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    private void loadDataFromJsonFile(){
        String filename = "details.xml";
        FileInputStream fis;
        InputStreamReader isr;
        BufferedReader bufferedReader;
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            fis = getActivity().openFileInput(filename);
            isr = new InputStreamReader(fis);
            bufferedReader = new BufferedReader(isr);

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            fis.close();
        }catch (IOException e) {
            // TODO handle exception
        }

        String data = sb.toString();

        Gson gson = new Gson();
        Type DetailsType = new TypeToken<RestaurantDetails>(){}.getType();
        desR=gson.fromJson(data, DetailsType);

    }
}
