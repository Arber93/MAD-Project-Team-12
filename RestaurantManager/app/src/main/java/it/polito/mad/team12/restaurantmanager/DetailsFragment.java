package it.polito.mad.team12.restaurantmanager;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileOutputStream;
import java.lang.reflect.Type;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements View.OnClickListener {
    /*
     *    This fragment will contain information on the restaurant (such as location, timetables,
     *  photos, etc.). The manager must be able to edit this data through the use of dialog boxes.
     */

    private View myFragment;
    private TextView phoneE;
    private TextView mondays;
    private TextView tue;
    private TextView wed;
    private TextView thu;
    private TextView fri;
    private TextView sat;
    private TextView sun;
    private ViewFlipper fliIm;
    private Button next;
    private Button prev;
    private String monday;
    private String phone;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;
    private CheckBox vegan,vegetarian,glutenfree;

    private Button editHours, editPhone;
    private RestaurantDetails resDet= new RestaurantDetails();




    public DetailsFragment() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        File jsonFile = new File(getActivity().getFilesDir(), "details.xml");

        if(jsonFile.exists()) {
          loadDataFromJsonFile();
        } else {
            resDet.setTelephone("011950225");
            resDet.setMondayFrom("8:30");
            resDet.setMondayTo("18:00");
            resDet.setTuesdayFrom("8:30");
            resDet.setTuesdayTo("18:00");
            resDet.setWednesdayFrom("9:30");
            resDet.setWednesdayTo("13;00");
            resDet.setThursdayFrom("8:30");
            resDet.setThursdayTo("19:30");
            resDet.setFridayFrom("8:30");
            resDet.setFridayTo("24:00");
            resDet.setSaturdayFrom("9:00");
            resDet.setSaturdayTo("24:00");
            resDet.setSundayFrom("8:30");
            resDet.setSundayTo("12:00");

            saveDataToJsonFile();
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
        resDet=gson.fromJson(data, DetailsType);

    }

    private void saveDataToJsonFile(){
        Gson gson = new Gson();
        Type DetailsType = new TypeToken<RestaurantDetails>(){}.getType();
        String data = gson.toJson(resDet, DetailsType);
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState){
        // Inflate the layout for this fragment
        myFragment= inflater.inflate(R.layout.fragment_details, container, false);
        phoneE=(TextView) myFragment.findViewById(R.id.phoneNumber);
        mondays=(TextView) myFragment.findViewById(R.id.frag_details_monday);
        tue=(TextView) myFragment.findViewById(R.id.frag_details_tuesday);
        wed=(TextView) myFragment.findViewById(R.id.frag_details_wednesday);
        thu=(TextView) myFragment.findViewById(R.id.frag_details_thursday);
        fri=(TextView) myFragment.findViewById(R.id.frag_details_friday);
        sat=(TextView) myFragment.findViewById(R.id.frag_details_saturday);
        sun=(TextView) myFragment.findViewById(R.id.frag_details_sunday);
        fliIm=(ViewFlipper) myFragment.findViewById(R.id.viewFlip);
        next=(Button) myFragment.findViewById(R.id.nextImage);
        prev=(Button) myFragment.findViewById(R.id.prevImage);

        editHours=(Button) myFragment.findViewById(R.id.changeOpenings);
        editPhone=(Button) myFragment.findViewById(R.id.changePhone);

        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        editHours.setOnClickListener(this);
        editPhone.setOnClickListener(this);

        vegan= (CheckBox) myFragment.findViewById(R.id.fragment_details_checkbox_vegan);
        if (resDet.isVegan()) vegan.setChecked(true);
        vegan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {  //VEGAN YES
                                resDet.setVegan(true);
                                saveDataToJsonFile();
                } else {
                                resDet.setVegan(false);
                                saveDataToJsonFile();
                }

            }
        });

        vegetarian =(CheckBox) myFragment.findViewById(R.id.fragment_details_checkbox_vegetarian);
        if (resDet.isVegetarian()) vegetarian.setChecked(true);
        vegetarian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                                resDet.setVegetarian(true);
                                saveDataToJsonFile();
                } else {
                                resDet.setVegetarian(false);
                    saveDataToJsonFile();

                }

            }
        });

        glutenfree= (CheckBox) myFragment.findViewById(R.id.fragment_details_checkbox_gluten);
        if (resDet.isGlutenFree()) glutenfree.setChecked(true);
        glutenfree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                                resDet.setGlutenFree(true);
                    saveDataToJsonFile();

                } else {
                    resDet.setGlutenFree(false);
                    saveDataToJsonFile();

                }

            }
        });

       // Button editH = (Button) myFragment.findViewById(R.id.editHoursbutton);
       // editH.setOnClickListener(this);
/*
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("details");

            for (int i = 0; i < m_jArry.length(); i++) {

                JSONObject jsonObject = m_jArry.getJSONObject(i);

                 phone = jsonObject.optString("phonen").toString();
                 monday = jsonObject.optString("mondayh").toString();
                 tuesday = jsonObject.optString("tuesdayh").toString();
                 wednesday = jsonObject.optString("wednesdayh").toString();
                 thursday = jsonObject.optString("thursdayh").toString();
                 friday = jsonObject.optString("fridayh").toString();
                 saturday = jsonObject.optString("saturdayh").toString();
                 sunday = jsonObject.optString("sundayh").toString();




            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/

        phoneE.setText(resDet.getTelephone());
        if (resDet.isMonclosed() == false){
            mondays.setText(resDet.getMondayFrom()+"-"+resDet.getMondayTo());
        } else mondays.setText(R.string.closed);

        if (resDet.isTueclosed() == false){
            tue.setText(resDet.getTuesdayFrom()+"-"+resDet.getTuesdayTo());
        } else tue.setText(R.string.closed);

        if (resDet.isWedclosed() == false) {
            wed.setText(resDet.getWednesdayFrom()+"-"+resDet.getWednesdayTo());
        } else wed.setText(R.string.closed);

        if (resDet.isThurclosed() == false) {
            thu.setText(resDet.getThursdayFrom()+"-"+resDet.getThursdayTo());
        } else thu.setText(R.string.closed);

        if (resDet.isFriclosed() == false) {
            fri.setText(resDet.getFridayFrom()+"-"+resDet.getFridayTo());
        } else fri.setText(R.string.closed);

        if (resDet.isSatclosed() == false) {
            sat.setText(resDet.getSaturdayFrom()+"-"+resDet.getSaturdayTo());
        } else sat.setText(R.string.closed);

        if (resDet.isSunclosed() == false){
            sun.setText(resDet.getSundayFrom()+"-"+resDet.getSundayTo());
        }else sun.setText(R.string.closed);

        return myFragment;
    }

        public void onClick(View v){
            if (v == next){
                fliIm.showNext();
            }
            if (v== prev){
                fliIm.showPrevious();
            }
            if (v==editHours){

                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction;

                transaction = manager.beginTransaction();

                EditOpenings editing = new EditOpenings();
                editing.show(transaction,"Dialog");

            }
            if (v==editPhone){
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction;

                transaction = manager.beginTransaction();

                EditPhoneDetails editing = new EditPhoneDetails();
                editing.show(transaction,"Dialog");

            }
        }



/*
    @Override
    public void onClick(View v) {

        EditOpenings fragment2 = new EditOpenings();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction =        fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



    }

*/




    public void onResume(){
        super.onResume();

        loadDataFromJsonFile();
    }


}
