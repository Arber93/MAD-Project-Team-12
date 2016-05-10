package it.polito.mad.team12.restaurantmanager;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;


public class EditOpenings extends DialogFragment implements View.OnClickListener{

    public EditOpenings() {

    }

    EditText monTo,monFro;
    EditText tuTo,tuFro;
    EditText weTo,weFro;
    EditText thTo,thFro;
    EditText frTo,frFro;
    EditText saTo,saFro;
    EditText suTo,suFro;
    Firebase mRootRef,restaurant;
    Button confirm, cancel;

    CheckBox monC,tueC,wedC,thuC,friC,satC,sunC;



    private View myFragment;
    private RestaurantDetails desR;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragment= inflater.inflate(R.layout.fragment_layout_edit_openings, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        confirm = (Button) myFragment.findViewById(R.id.fleo_save_button);
        cancel =(Button) myFragment.findViewById(R.id.fleo_cancel_button);

        monTo=(EditText) myFragment.findViewById(R.id.monday_to);
        monFro=(EditText) myFragment.findViewById(R.id.monday_from);

        tuTo=(EditText) myFragment.findViewById(R.id.tuesday_to);
        tuFro=(EditText) myFragment.findViewById(R.id.tuesday_from);

        weTo=(EditText) myFragment.findViewById(R.id.wednesday_to);
        weFro=(EditText) myFragment.findViewById(R.id.wednesday_from);

        thTo=(EditText) myFragment.findViewById(R.id.thursday_to);
        thFro=(EditText) myFragment.findViewById(R.id.thursday_from);

        frTo=(EditText) myFragment.findViewById(R.id.friday_to);
        frFro=(EditText) myFragment.findViewById(R.id.friday_from);

        saTo=(EditText) myFragment.findViewById(R.id.saturday_to);
        saFro=(EditText) myFragment.findViewById(R.id.saturday_from);

        suTo=(EditText) myFragment.findViewById(R.id.sunday_to);
        suFro=(EditText) myFragment.findViewById(R.id.sunday_from);

        monC = (CheckBox) myFragment.findViewById(R.id.monday_closed);
        monC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true){
                                monTo.setEnabled(false);
                                monFro.setEnabled(false);
                        }else{
                                monTo.setEnabled(true);
                                monFro.setEnabled(true);
                        }
            }
        });

        tueC = (CheckBox) myFragment.findViewById(R.id.tuesday_closed);
        tueC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true){
                            tuTo.setEnabled(false);
                            tuFro.setEnabled(false);
                        }else{
                            tuTo.setEnabled(true);
                            tuFro.setEnabled(true);
                        }
            }
        });

        wedC = (CheckBox) myFragment.findViewById(R.id.wednesday_closed);
        wedC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true){
                            weTo.setEnabled(false);
                            weFro.setEnabled(false);
                        }else{
                            weTo.setEnabled(true);
                            weFro.setEnabled(true);
                        }
            }
        });

        thuC = (CheckBox) myFragment.findViewById(R.id.thursday_closed);
        thuC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            thFro.setEnabled(false);
                            thTo.setEnabled(false);
                        }else{
                            thFro.setEnabled(true);
                            thTo.setEnabled(true);
                        }
            }
        });

        friC = (CheckBox) myFragment.findViewById(R.id.friday_closed);
        friC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            frFro.setEnabled(false);
                            frTo.setEnabled(false);
                        }else{
                            frFro.setEnabled(true);
                            frTo.setEnabled(true);
                        }
            }
        });

        satC = (CheckBox) myFragment.findViewById(R.id.saturday_closed);
        satC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            saFro.setEnabled(false);
                            saTo.setEnabled(false);
                        }else{
                            saFro.setEnabled(true);
                            saTo.setEnabled(true);
                        }
            }
        });

        sunC = (CheckBox) myFragment.findViewById(R.id.sunday_closed);
        sunC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            suFro.setEnabled(false);
                            suTo.setEnabled(false);
                        }else{
                            suFro.setEnabled(true);
                            suTo.setEnabled(true);
                        }
            }
        });




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

        String restaurant11= "Tutto PizzaCorso Duca Degli Abruzzi 19";
        mRootRef = new Firebase("https://popping-inferno-6667.firebaseio.com/restaurants");   //ROOT of Firebase Restaurants
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


    @Override
    public void onClick(View v) {
        if (v == cancel){
            dismiss();  //exits from dialog fragment
        }
        if (v == confirm){
            // save things into JSON updating it


        if (monC.isChecked()) {
            //MONDAY CLOSED
            desR.setMonclosed(true);
        } else {
            if (desR.isMonclosed() && monFro.getText().length() == 0 && monTo.getText().length() == 0 ){

            }else {
                desR.setMonclosed(false);
                if (monFro.isEnabled() && monFro.getText().length() > 0)
                    desR.setMondayFrom(monFro.getText().toString());
                if (monTo.isEnabled() && monTo.getText().length() > 0)
                    desR.setMondayTo(monTo.getText().toString());
            }
        }

        if (tueC.isChecked()) {
            desR.setTueclosed(true);
        }else{
            if (desR.isTueclosed() && tuFro.getText().length() == 0 && tuTo.getText().length() == 0){

            }else {
                desR.setTueclosed(false);
                if (tuFro.getText().length() > 0) desR.setTuesdayFrom(tuFro.getText().toString());
                if (tuTo.getText().length() > 0) desR.setTuesdayTo(tuTo.getText().toString());
            }
        }

        if (wedC.isChecked()){
            desR.setWedclosed(true);
        } else{
            if (desR.isWedclosed() && weFro.getText().length() == 0 && weTo.getText().length() == 0){

            }else {
                desR.setWedclosed(false);
                if (weFro.getText().length() > 0) desR.setWednesdayFrom(weFro.getText().toString());
                if (weTo.getText().length() > 0) desR.setWednesdayTo(weTo.getText().toString());
            }
        }

        if (thuC.isChecked()){
            desR.setThurclosed(true);
        } else{
            if (desR.isThurclosed() && thFro.getText().length() == 0 && thTo.getText().length() == 0 ){

            }else {
                desR.setThurclosed(false);
                if (thFro.getText().length() > 0) desR.setThursdayFrom(thFro.getText().toString());
                if (thTo.getText().length() > 0) desR.setThursdayTo(thTo.getText().toString());
            }
        }

        if (friC.isChecked()){
                desR.setFriclosed(true);
        } else{
            if (desR.isFriclosed() && frFro.getText().length() == 0 && frTo.getText().length() == 0){

            }else {
                desR.setFriclosed(false);
                if (frFro.getText().length() > 0) desR.setFridayFrom(frFro.getText().toString());
                if (frTo.getText().length() > 0) desR.setFridayTo(frTo.getText().toString());
            }
        }

        if (satC.isChecked()){
                desR.setSatclosed(true);
        } else{
            if (desR.isSatclosed() && saFro.getText().length() == 0 && saTo.getText().length() ==0){

            }else {
                desR.setSatclosed(false);
                if (saFro.getText().length() > 0) desR.setSaturdayFrom(saFro.getText().toString());
                if (saTo.getText().length() > 0) desR.setSaturdayTo(saTo.getText().toString());
            }
        }

        if (sunC.isChecked()){
                desR.setSunclosed(true);
        } else{
            if (desR.isSunclosed()  && suFro.getText().length() == 0 && suTo.getText().length() == 0) {   //IF it was closed and nothing changed keep it closed

            }else {
                desR.setSunclosed(false);
                if (suFro.getText().length() > 0) desR.setSundayFrom(suFro.getText().toString());
                if (suTo.getText().length() > 0) desR.setSundayTo(suTo.getText().toString());
            }
        }

        restaurant.setValue(desR);

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
