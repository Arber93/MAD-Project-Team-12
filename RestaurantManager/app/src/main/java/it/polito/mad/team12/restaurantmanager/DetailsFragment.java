package it.polito.mad.team12.restaurantmanager;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileOutputStream;
import java.lang.reflect.Type;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements View.OnClickListener, GestureDetector.OnGestureListener {
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
    private GestureDetector mGestureDetector;
    private Button next;
    private Button prev;
    private Button editFlipper;
    private String monday;
    private String phone;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;
    private String b64ph1,b64ph2,b64ph3,b64ph4;
    private CheckBox vegan,vegetarian,glutenfree;
    private float initialX;
    private TextView street,resname;
    private Button editHours, editPhone, editLogo;
    private RestaurantDetails resDet= new RestaurantDetails();
    private Firebase mRootRef, restaurant, mPhotosRef, photos;
    ImageView image1,image2,image3,image4;
    ImageView reslogo;
    Firebase geoRef = Utility.getFirebaseGeofireRef();
    private GeoFire geoFire;


    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(String restaurantID) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(Utility.RESTAURANT_ID_KEY, restaurantID);
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getContext());

        String restaurant11= getArguments().getString(Utility.RESTAURANT_ID_KEY);

        mRootRef= Utility.getFirebaseRestaurantsRef();   //ROOT of Firebase Restaurants
        restaurant= mRootRef.child(restaurant11);      //access the specified restaurant

        mPhotosRef = Utility.getFirebasePhotosRef();
        photos = mPhotosRef.child(restaurant11);

        image1= new ImageView(getContext());
        image2= new ImageView(getContext());
        image3= new ImageView(getContext());
        image4= new ImageView(getContext());


        /*
        setHasOptionsMenu(true);
        File jsonFile = new File(getActivity().getFilesDir(), "details.xml");

        if(jsonFile.exists()) {
          loadDataFromJsonFile();
            b64ph1=resDet.getPhoto1();  //the base 64 encodings of the images for the viewflipper
            b64ph2=resDet.getPhoto2();
            b64ph3=resDet.getPhoto3();
            b64ph4=resDet.getPhoto4();



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
        */

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

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
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
        editFlipper=(Button) myFragment.findViewById(R.id.fragment_details_edit_flipper);

        fliIm = (ViewFlipper) myFragment.findViewById(R.id.viewFlip);
        fliIm.setInAnimation(getContext(), android.R.anim.fade_in);
        fliIm.setOutAnimation(getContext(), android.R.anim.fade_out);

        next=(Button) myFragment.findViewById(R.id.nextImage);
        prev=(Button) myFragment.findViewById(R.id.prevImage);

        editLogo=(Button) myFragment.findViewById(R.id.edit_logo);
        editHours=(Button) myFragment.findViewById(R.id.changeOpenings);
        editPhone=(Button) myFragment.findViewById(R.id.changePhone);
        mGestureDetector = new GestureDetector(getContext(),this);
        reslogo=(ImageView) myFragment.findViewById(R.id.acvivity_customer_logores);

        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        editHours.setOnClickListener(this);
        editPhone.setOnClickListener(this);
        editFlipper.setOnClickListener(this);
        editLogo.setOnClickListener(this);
        street=(TextView) myFragment.findViewById(R.id.frag_det_street);
        resname=(TextView) myFragment.findViewById(R.id.frag_det_resname);

        vegan= (CheckBox) myFragment.findViewById(R.id.fragment_details_checkbox_vegan);


        vegetarian =(CheckBox) myFragment.findViewById(R.id.fragment_details_checkbox_vegetarian);


        glutenfree= (CheckBox) myFragment.findViewById(R.id.fragment_details_checkbox_gluten);

        restaurant.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                resDet = dataSnapshot.getValue(RestaurantDetails.class);
                System.out.println("IL RISTORANTEEEEEEEEEEEEEEEEEEEEEEEEEE "+resDet.getRestaurantName());
                phoneE.setText(resDet.getTelephone());
                street.setText(resDet.getRestaurantAddress());
                resname.setText(resDet.getRestaurantName());
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

                if (resDet.isVegan()) vegan.setChecked(true);
                vegan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {  //VEGAN YES
                            resDet.setVegan(true);
                            restaurant.child("vegan").setValue(resDet.isVegan());
                        } else {
                            resDet.setVegan(false);
                            restaurant.child("vegan").setValue(resDet.isVegan());
                        }

                    }
                });
                if (resDet.isVegetarian()) vegetarian.setChecked(true);
                vegetarian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            resDet.setVegetarian(true);
                            restaurant.child("vegetarian").setValue(resDet.isVegetarian());
                        } else {
                            resDet.setVegetarian(false);
                            restaurant.child("vegetarian").setValue(resDet.isVegetarian());
                        }

                    }
                });
                if (resDet.isGlutenFree()) glutenfree.setChecked(true);
                glutenfree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            resDet.setGlutenFree(true);
                            restaurant.child("glutenFree").setValue(resDet.isGlutenFree());

                        } else {
                            resDet.setGlutenFree(false);
                            restaurant.child("glutenFree").setValue(resDet.isGlutenFree());
                        }

                    }
                });

                String logores = resDet.getRestaurantLogo();
                if (logores.equals("null") == false) {
                    Bitmap lr = decodeBase64(logores);

                    reslogo.setImageBitmap(lr);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        photos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> mapP = dataSnapshot.getValue(Map.class);

                b64ph1=(mapP.get("photo1"));   //set them to be what you find in Firebase
                b64ph2=(mapP.get("photo2"));
                b64ph3=(mapP.get("photo3"));
                b64ph4=(mapP.get("photo4"));

                fliIm.removeAllViews();

                if (b64ph1 != null) {
                    if (b64ph1.length() > 0) {
                        if (b64ph1.equals("deleted") || b64ph1.equals("null")) {
                        } else {
                            Bitmap imageForFlipper1 = decodeBase64(b64ph1);
                            image1.setImageBitmap(imageForFlipper1);
                            image1.setScaleType(ImageView.ScaleType.FIT_XY);
                            fliIm.addView(image1);

                        }
                    }
                }

                if (b64ph2 != null){
                    if (b64ph2.length() > 0){
                        if (b64ph2.equals("deleted") || b64ph2.equals("null")){

                        }else{
                            Bitmap imageForFlipper2 = decodeBase64(b64ph2);
                            image2.setImageBitmap(imageForFlipper2);
                            image2.setScaleType(ImageView.ScaleType.FIT_XY);
                            fliIm.addView(image2);
                        }
                    }
                }

                if (b64ph3 != null){
                    if (b64ph3.length() > 0){
                        if (b64ph3.equals("deleted") || b64ph3.equals("null")){

                        }else{
                            Bitmap imageForFlipper3 = decodeBase64(b64ph3);
                            image3.setImageBitmap(imageForFlipper3);
                            image3.setScaleType(ImageView.ScaleType.FIT_XY);
                            fliIm.addView(image3);
                        }
                    }
                }

                if (b64ph4 != null){
                    if (b64ph4.length() > 0){
                        if (b64ph4.equals("deleted") || b64ph4.equals("null")){

                        }else{
                            Bitmap imageForFlipper4 = decodeBase64(b64ph4);
                            image4.setImageBitmap(imageForFlipper4);
                            image4.setScaleType(ImageView.ScaleType.FIT_XY);
                            fliIm.addView(image4);
                        }
                    }
                }

                if (b64ph1 == null && b64ph2==null && b64ph3 == null && b64ph4 == null){
                    ImageView emptyIm = new ImageView(getContext());
                    emptyIm.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_flipper_image));
                    emptyIm.setScaleType(ImageView.ScaleType.CENTER);
                    fliIm.addView(emptyIm);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

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


        return myFragment;
    }





    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        // Swipe left (next)
        if (e1.getX() > e2.getX()) {
            fliIm.showNext();
        }

        // Swipe right (previous)
        if (e1.getX() < e2.getX()) {
            fliIm.showPrevious();
        }
        return true;
    }


    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        return getActivity().onTouchEvent(event);
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
            if (v==editFlipper){
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction;

                transaction = manager.beginTransaction();

                DetailsFlipperImagesEdit editing = new DetailsFlipperImagesEdit();
                editing.show(transaction,"Dialog");
            }
            if (v==editLogo){
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction;

                transaction = manager.beginTransaction();

                DetailsLogoEdit editing = new DetailsLogoEdit();
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

        //loadDataFromJsonFile();
    }
}
