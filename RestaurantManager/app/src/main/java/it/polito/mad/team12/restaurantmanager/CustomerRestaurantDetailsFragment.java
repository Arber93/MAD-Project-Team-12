package it.polito.mad.team12.restaurantmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

public class CustomerRestaurantDetailsFragment extends Fragment {
    private View myFragment;

    private String name;
    private TextView phoneE;
    private TextView mondays;
    private TextView tue;
    private TextView wed;
    private TextView thu;
    private TextView fri;
    private TextView sat;
    private TextView sun;
    private TextView streetname;
    private TextView resname;
    private CheckBox vegan,vegetarian,glutenfree;
    private RestaurantDetails resDet= new RestaurantDetails();
    private String b64ph1,b64ph2,b64ph3,b64ph4;
    ImageView image1,image2,image3,image4, logores;
    Firebase mRootRef,mPhotosRef, restaurant, photos;
    private Toolbar toolbar;
    private ViewPager viewPager;
    PagerAdapter adapter;
    private String[] images={"0","0","0","0"};
    private Button takemethere;
    CustomerRestaurantShowMap showing = new CustomerRestaurantShowMap();
    private boolean firstVisit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.activity_customer_restaurant, container, false);

        ;
        /*
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        */
        firstVisit=true;
        phoneE = (TextView) myFragment.findViewById(R.id.activity_customer_resphone);
        mondays = (TextView) myFragment.findViewById(R.id.activity_monday);
        tue = (TextView) myFragment.findViewById(R.id.activity_tuesday);
        wed = (TextView) myFragment.findViewById(R.id.activity_wednesday);
        thu = (TextView) myFragment.findViewById(R.id.activity_thursday);
        fri = (TextView) myFragment.findViewById(R.id.activity_friday);
        sat = (TextView) myFragment.findViewById(R.id.activity_saturday);
        sun = (TextView) myFragment.findViewById(R.id.activity_sunday);
        viewPager = (ViewPager) myFragment.findViewById(R.id.customer_pager_photos_shower);
        streetname = (TextView) myFragment.findViewById(R.id.activity_customer_popupmap);
        takemethere = (Button) myFragment.findViewById(R.id.activity_customer_takemethere);

        takemethere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction;

                transaction = manager.beginTransaction();

                showing.show(transaction, "Dialog");

            }
        });
        resname = (TextView) myFragment.findViewById(R.id.activity_customer_resname);
        logores = (ImageView) myFragment.findViewById(R.id.acvivity_customer_logores);

        vegan = (CheckBox) myFragment.findViewById(R.id.activity_customer_checkboxvegan);
        vegetarian = (CheckBox) myFragment.findViewById(R.id.activity_customer_checkboxvegetarian);
        glutenfree = (CheckBox) myFragment.findViewById(R.id.activity_customer_checkboxgluten);

        CustomerRestaurantActivityMain act = (CustomerRestaurantActivityMain) getActivity();
        name = act.retrieveRestInfo(); // I RETRIEVE THE INFO OF THE RESTAURANT FROM HERE

        mRootRef = new Firebase("https://popping-inferno-6667.firebaseio.com/restaurants");   //ROOT of Firebase Restaurants
        restaurant = mRootRef.child(name);      //access the specified restaurant

        mPhotosRef = new Firebase("https://popping-inferno-6667.firebaseio.com/photos");
        photos = mPhotosRef.child(name);



        image1 = new ImageView(getContext());
        image2 = new ImageView(getContext());
        image3 = new ImageView(getContext());
        image4 = new ImageView(getContext());


        restaurant.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                resDet = dataSnapshot.getValue(RestaurantDetails.class);
                phoneE.setText(resDet.getTelephone());
                streetname.setText(resDet.getRestaurantAddress());
                resname.setText(resDet.getRestaurantName());

                if (resDet.isMonclosed() == false) {
                    mondays.setText(resDet.getMondayFrom() + "-" + resDet.getMondayTo());
                } else mondays.setText(R.string.closed);

                if (resDet.isTueclosed() == false) {
                    tue.setText(resDet.getTuesdayFrom() + "-" + resDet.getTuesdayTo());
                } else tue.setText(R.string.closed);

                if (resDet.isWedclosed() == false) {
                    wed.setText(resDet.getWednesdayFrom() + "-" + resDet.getWednesdayTo());
                } else wed.setText(R.string.closed);

                if (resDet.isThurclosed() == false) {
                    thu.setText(resDet.getThursdayFrom() + "-" + resDet.getThursdayTo());
                } else thu.setText(R.string.closed);

                if (resDet.isFriclosed() == false) {
                    fri.setText(resDet.getFridayFrom() + "-" + resDet.getFridayTo());
                } else fri.setText(R.string.closed);

                if (resDet.isSatclosed() == false) {
                    sat.setText(resDet.getSaturdayFrom() + "-" + resDet.getSaturdayTo());
                } else sat.setText(R.string.closed);

                if (resDet.isSunclosed() == false) {
                    sun.setText(resDet.getSundayFrom() + "-" + resDet.getSundayTo());
                } else sun.setText(R.string.closed);

                if (resDet.isVegan()) vegan.setChecked(true);

                if (resDet.isVegetarian()) vegetarian.setChecked(true);

                if (resDet.isGlutenFree()) glutenfree.setChecked(true);

                String lr = resDet.getRestaurantLogo();
                if (lr.equals("null") == false) {
                    Bitmap photorestaurant = decodeBase64(lr);
                    logores.setImageBitmap(photorestaurant);
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

                int i =0;

                b64ph1=(mapP.get("photo1"));   //set them to be what you find in Firebase
                b64ph2=(mapP.get("photo2"));
                b64ph3=(mapP.get("photo3"));
                b64ph4=(mapP.get("photo4"));


                if (b64ph1 != null) {
                    if (b64ph1.length() > 0) {
                        if (b64ph1.equals("deleted") || b64ph1.equals("null")) {
                        } else {

                            images[i]=b64ph1;
                            i++;
                        }
                    }
                }

                if (b64ph2 != null){
                    if (b64ph2.length() > 0){
                        if (b64ph2.equals("deleted") || b64ph2.equals("null")){

                        }else{

                            images[i]=b64ph2;
                            i++;
                        }
                    }
                }

                if (b64ph3 != null){
                    if (b64ph3.length() > 0){
                        if (b64ph3.equals("deleted") || b64ph3.equals("null")){

                        }else{

                            images[i]=b64ph3;
                            i++;
                        }
                    }
                }

                if (b64ph4 != null){
                    if (b64ph4.length() > 0){
                        if (b64ph4.equals("deleted") || b64ph4.equals("null")){

                        }else{

                            images[i]=b64ph4;
                        }
                    }
                }


                adapter = new ViewPagerAdapter(getContext(), images);
                // Binds the Adapter to the ViewPager
                viewPager.setAdapter(adapter);



            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });





        return myFragment;
    }



    public class ViewPagerAdapter extends PagerAdapter {
        // Declare Variables
        Context context;
        String[] allp;

        public ViewPagerAdapter(Context c, String[] p){
            this.context=c;
            this.allp=p;
        }

        @Override
        public int getCount() {
            return allp.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // Declare Variables
            ImageView image;
            LayoutInflater inflater;

            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.sliding_image_layout, container,
                    false);

            String imag= allp[position];
            if (imag.equals("0") ==false){
            Bitmap logop=decodeBase64(imag);
            // Locate the ImageView in viewpager_item.xml
            image = (ImageView) itemView.findViewById(R.id.image);
            // Capture position and set to the ImageView
            image.setImageBitmap(logop);
            }
            // Add viewpager_item.xml to ViewPager
            ((ViewPager) container).addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Remove viewpager_item.xml from ViewPager
            ((ViewPager) container).removeView((View) object);

        }
    }


    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }



}
