package it.polito.mad.team12.restaurantmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.polito.mad.team12.restaurantmanager.review.*;

/**
 * Created by Andrea on 08/05/2016.
 */
public class CustomerRestaurantActivityMain extends AppCompatActivity {

    private Toolbar toolbar;
    private String name;
    private String Details,Menu,Reviews;
    private ViewPager viewPager;
    TabLayout tabLayout;
    private Firebase mLocRef, restLoc;
    private Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_restaurant_main);

        Details=getResources().getString(R.string.details);
        Menu=getResources().getString(R.string.menu);
        Reviews=getResources().getString(R.string.reviews);

        Intent intent= getIntent();
        this.name= intent.getExtras().getString("restName");
        ReviewUtility.loadReviews(this.name);
        ReviewUtility.loadRestaurant(this.name);


        mLocRef = new Firebase("https://popping-inferno-6667.firebaseio.com/geofire");   //ROOT of Firebase Restaurants
        restLoc = mLocRef.child(name).child("l");      //access the specified restaurant


        restLoc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("VEDIAMO UN POCHINOOOO "+dataSnapshot.child("0").getValue().toString());

                latitude = Double.parseDouble(dataSnapshot.child("0").getValue().toString());
                longitude = Double.parseDouble(dataSnapshot.child("1").getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        viewPager = (ViewPager) findViewById(R.id.customer_viewpager);
        setupViewPager(viewPager);


        ViewPager.OnPageChangeListener myOnPageChangeListener =
                new ViewPager.OnPageChangeListener(){

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        //Called when the scroll state changes.
                    }

                    @Override
                    public void onPageScrolled(int position,
                                               float positionOffset, int positionOffsetPixels) {
                        //This method will be invoked when the current page is scrolled,
                        //either as part of a programmatically initiated smooth scroll
                        //or a user initiated touch scroll.
                    }

                    @Override
                    public void onPageSelected(int position) {
                        //This method will be invoked when a new page becomes selected.
                        //this should refresh the list of reservations
                        viewPager.getAdapter().notifyDataSetChanged();
                        Log.d("ADebugTag", "***************notifyDataSetChanged*************** ");

                    }};
        viewPager.addOnPageChangeListener(myOnPageChangeListener);

        tabLayout = (TabLayout) findViewById(R.id.customer_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_restaurant_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.rd_make_reservation:
                Intent intent = new Intent(this, ReservationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("restaurantID", name);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CustomerRestaurantDetailsFragment(), Details);
        adapter.addFragment(ShowMenuFragment.newInstance(name), Menu);
        adapter.addFragment(new ReviewsInsertFragment(), Reviews);
        viewPager.setAdapter(adapter);
    }

    public String retrieveRestInfo(){
        return name;
    }
    public Double retrieveLat(){return latitude; }
    public Double retrieveLon(){return longitude; }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment instance;
            instance = mFragmentList.get(position);
            return instance;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public int getItemPosition(Object object){
            return POSITION_NONE;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReviewUtility.clear();
    }
}
