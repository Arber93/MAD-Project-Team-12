package it.polito.mad.team12.restaurantmanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


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





    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        myFragment= inflater.inflate(R.layout.fragment_details, container, false);
        phoneE=(TextView) myFragment.findViewById(R.id.phoneNumber);
        mondays=(TextView) myFragment.findViewById(R.id.textView10);
        tue=(TextView) myFragment.findViewById(R.id.textView11);
        wed=(TextView) myFragment.findViewById(R.id.textView12);
        thu=(TextView) myFragment.findViewById(R.id.textView13);
        fri=(TextView) myFragment.findViewById(R.id.textView14);
        sat=(TextView) myFragment.findViewById(R.id.textView15);
        sun=(TextView) myFragment.findViewById(R.id.textView16);
        fliIm=(ViewFlipper) myFragment.findViewById(R.id.viewFlip);
        next=(Button) myFragment.findViewById(R.id.nextImage);
        prev=(Button) myFragment.findViewById(R.id.prevImage);

        next.setOnClickListener(this);
        prev.setOnClickListener(this);

       // Button editH = (Button) myFragment.findViewById(R.id.editHoursbutton);
       // editH.setOnClickListener(this);

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

        phoneE.setText(phone);
        mondays.setText(monday);
        tue.setText(tuesday);
        wed.setText(wednesday);
        thu.setText(thursday);
        fri.setText(friday);
        sat.setText(saturday);
        sun.setText(sunday);

        return myFragment;
    }

        public void onClick(View v){
            if (v == next){
                fliIm.showNext();
            }
            if (v== prev){
                fliIm.showPrevious();
            }
        }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("details.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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






}
