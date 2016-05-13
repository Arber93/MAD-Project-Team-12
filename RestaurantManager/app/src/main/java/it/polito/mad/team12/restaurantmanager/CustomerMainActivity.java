package it.polito.mad.team12.restaurantmanager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;


import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;


public class CustomerMainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Spinner spinnerCategories, spinnerSortFilter;
    ArrayAdapter<CharSequence> catAd, sortAd;
    private Double latitude, longitude;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private Toolbar toolbar;
    private RecyclerView recyclerRestaur;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Firebase mRef = new Firebase("https://popping-inferno-6667.firebaseio.com/restaurants");
    Firebase geoRef = new Firebase("https://popping-inferno-6667.firebaseio.com/geofire");
    private RestaurantDetails resDet;
    private LinkedHashMap<String,RestaurantDetails> Geodets = new LinkedHashMap<String,RestaurantDetails>();
    private LinkedList<RestaurantDetails> linkedGeo = new LinkedList<RestaurantDetails>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        setContentView(R.layout.activity_customer_main);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinnerCategories = (Spinner) findViewById(R.id.customer_spinner_foodcategory);
        spinnerSortFilter = (Spinner) findViewById(R.id.customer_spinner_sortingfilters);

        catAd = ArrayAdapter.createFromResource(this, R.array.foodcategories, android.R.layout.simple_spinner_item);
        catAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   //built-in layout resources

        sortAd = ArrayAdapter.createFromResource(this, R.array.sortingfilters, android.R.layout.simple_spinner_item);
        sortAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategories.setAdapter(catAd);
        spinnerSortFilter.setAdapter(sortAd);


        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (id == 0) {   //ALL THE CATEGORIES

                    final Query queryRef = mRef.orderByChild("categoryFood");

                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                            myFunctionStartActivity(mRef);


                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(mRef);


                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            myFunctionStartActivity(mRef);


                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(mRef);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });


                }
                if (id == 1) {   //AMERICAN
                    final Query queryRef = mRef.orderByChild("categoryFood").startAt("American").endAt("American");

                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                }
                if (id == 2) {    //ASIAN
                    final Query queryRef = mRef.orderByChild("categoryFood").startAt("Asian").endAt("Asian");

                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            myFunctionStartActivity(queryRef);

                        }
                    });

                }
                if (id == 3) {   //BEVERAGES
                    final Query queryRef = mRef.orderByChild("categoryFood").startAt("Beverages").endAt("Beverages");

                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            myFunctionStartActivity(queryRef);

                        }
                    });
                }
                if (id == 4) {   //HAMBURGERS
                    final Query queryRef = mRef.orderByChild("categoryFood").startAt("Hamburgers").endAt("Hamburgers");

                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            myFunctionStartActivity(queryRef);

                        }
                    });
                }
                if (id == 5) {   //INDIAN
                    final Query queryRef = mRef.orderByChild("categoryFood").startAt("Indian").endAt("Indian");

                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                            myFunctionStartActivity(queryRef);

                        }
                    });
                }
                if (id == 6) {   //ITALIAN
                    final Query queryRef = mRef.orderByChild("categoryFood").startAt("Italian").endAt("Italian");

                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            myFunctionStartActivity(queryRef);

                        }
                    });
                }
                if (id == 7) {   //GREEK
                    final Query queryRef = mRef.orderByChild("categoryFood").startAt("Greek").endAt("Greek");

                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                            myFunctionStartActivity(queryRef);

                        }
                    });
                }
                if (id == 8) {   //JAPANESE
                    final Query queryRef = mRef.orderByChild("categoryFood").startAt("Japanese").endAt("Japanese");

                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                            myFunctionStartActivity(queryRef);

                        }
                    });
                }
                if (id == 9) {   //MEXICAN
                    final Query queryRef = mRef.orderByChild("categoryFood").startAt("Mexican").endAt("Mexican");

                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            myFunctionStartActivity(queryRef);

                        }
                    });
                }
                if (id == 10) {  //PIZZA
                    final Query queryRef = mRef.orderByChild("categoryFood").startAt("Pizzas").endAt("Pizzas");

                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                            myFunctionStartActivity(queryRef);


                        }
                    });
                }
                if (id == 11) {  //THAI
                    final Query queryRef = mRef.orderByChild("categoryFood").startAt("Thai").endAt("Thai");

                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                            myFunctionStartActivity(queryRef);

                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSortFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (id == 0) {  // NO FILTERS
                    final Query queryRef = mRef.orderByChild("categoryFood");

                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            myFunctionStartActivity(queryRef);


                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });


                }
                if (id == 1) { // A-Z

                    final Query queryRef = mRef.orderByChild("restaurantName");
                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            myFunctionStartActivity(queryRef);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            myFunctionStartActivity(queryRef);

                        }
                    });

                }
                if (id == 2) { //CLOSEST FIRST

                    geoFire = new GeoFire(geoRef);

                    System.out.println("Cerco le coordinate!" + latitude);
                    geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), 1);
                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {  //you will iterate here for every single KEY found in your radius
                        @Override
                        public void onKeyEntered(String key, GeoLocation location) {

                            System.out.println("LA CHIAVEEEEE "+key);
                            Firebase tempRef = mRef.child(key);  //ONE single restaurant matching the key


                            tempRef.addValueEventListener(new ValueEventListener() {
                                @Override                                                   //accessing info of ONE restaurant
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    resDet = dataSnapshot.getValue(RestaurantDetails.class);
                                    System.out.println("IL RISTORANTEEE "+resDet.getRestaurantName());
                                    if (Geodets.containsKey(resDet.getRestaurantName()) == false) {
                                        Geodets.put(resDet.getRestaurantName(), resDet);  //adding all info we need
                                        System.out.println("AGGIUNTOIL TISTORANTE! ");

                                    }
                                    onGeoQueryReady();
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });

                        }

                        @Override
                        public void onKeyExited(String key) {
                            Firebase tempRef = mRef.child(key);  //ONE single restaurant matching the key


                            tempRef.addValueEventListener(new ValueEventListener() {
                                @Override                                                   //accessing info of ONE restaurant
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    resDet = dataSnapshot.getValue(RestaurantDetails.class);

                                    Geodets.remove(resDet.getRestaurantName());  //removing the key not in our radius anymore
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                        }

                        @Override
                        public void onKeyMoved(String key, GeoLocation location) {

                        }

                        @Override
                        public void onGeoQueryReady() {
                                instanciateAdapter(Geodets);
                        }

                        @Override
                        public void onGeoQueryError(FirebaseError error) {

                        }




                    });


                }
                if (id == 3) {   //BEST REVIEWS

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        recyclerRestaur = (RecyclerView) findViewById(R.id.customer_main_recycler);
        recyclerRestaur.setHasFixedSize(true);
        recyclerRestaur.setLayoutManager(new LinearLayoutManager(this));


        myFunctionStartActivity(mRef);


    }

    void instanciateAdapter(LinkedHashMap<String, RestaurantDetails> call){
        myGeoFirebaseAdapter adapterG = new myGeoFirebaseAdapter(getApplicationContext(),linkedGeo);


        adapterG.SetOnItemClickListener(new myGeoFirebaseAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(View v, String name) {        //Open a new Activity showing the restaurant chosen!
                // the restaurant is opened accessing the name

                //PASS THE NAME = ID of restaurant in Firebase to the Intent creating the activity showing THE chosen restaurant
                startnewActivity(name);
            }


        });


        linkedGeo.clear();
        for (RestaurantDetails r : call.values()){   //passing from linkedhashmap TO linkedlist
            linkedGeo.add(r);
            System.out.println("QUANDO????");

        }

        adapterG.notifyDataSetChanged();
        recyclerRestaur.setAdapter(adapterG);
        adapterG.notifyDataSetChanged();
    }




    private void myFunctionStartActivity(Query ref) {

        myFirebaseRecyAdapter madR = new myFirebaseRecyAdapter(RestaurantDetails.class,
                R.layout.customer_recycler_item,
                myFirebaseRecyAdapter.DetailsHolder.class,
                ref);


        madR.notifyDataSetChanged();
        recyclerRestaur.setAdapter(madR);
        madR.notifyDataSetChanged();

        madR.SetOnItemClickListener(new myFirebaseRecyAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, String name) {        //Open a new Activity showing the restaurant chosen!
                // the restaurant is opened accessing the name

                //PASS THE NAME = ID of restaurant in Firebase to the Intent creating the activity showing THE chosen restaurant
                startnewActivity(name);
            }
        });
    }

    public void startnewActivity(String name) {
        System.out.println("POSITION: " + name);
        Intent i = new Intent(this, CustomerRestaurantActivityMain.class);
        i.putExtra("restName", name);
        startActivity(i);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {

        if(latitude != null) {
            savedInstanceState.putDouble("lat", latitude);
        }
        if(longitude != null) {
            savedInstanceState.putDouble("lon", longitude);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        longitude = savedInstanceState.getDouble("lon");
        latitude = savedInstanceState.getDouble("lat");
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            this.latitude=mLastLocation.getLatitude();
            this.longitude=mLastLocation.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}