package it.polito.mad.team12.restaurantmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ReservationsActivity extends AppCompatActivity {

    private String PENDING;
    private String ACCEPTED;
    private String DENIED;
    private String COMPLETED;

    Toolbar toolbar;
    TabLayout tabLayout;
    private ViewPager viewPager;
    private String restaurantID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        Intent intent = getIntent();

        if(intent.getExtras().containsKey(Utility.RESTAURANT_ID_KEY)) {
            restaurantID = intent.getExtras().getString(Utility.RESTAURANT_ID_KEY);
        } else if (savedInstanceState != null && savedInstanceState.containsKey(Utility.RESTAURANT_ID_KEY)) {
            restaurantID = savedInstanceState.getString(Utility.RESTAURANT_ID_KEY);
        }

        PENDING = getResources().getString(R.string.pending);
        ACCEPTED = getResources().getString(R.string.accepted);
        DENIED = getResources().getString(R.string.denied);
        COMPLETED = getResources().getString(R.string.completed_e);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        OnPageChangeListener myOnPageChangeListener =
                new OnPageChangeListener(){

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

    }

    public String getRestaurantID() {
        return restaurantID;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(PendingReservationsFragment.class, PENDING);
        adapter.addFragment(AcceptedReservationsFragment.class, ACCEPTED);
        adapter.addFragment(DeniedReservationsFragment.class, DENIED);
        adapter.addFragment(CompletedReservationsFragment.class, COMPLETED);
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private List<Class<? extends Fragment>> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            try {
                Fragment instance = mFragmentList.get(position).newInstance();
                return instance;
            } catch (InstantiationException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public int getItemPosition(Object object){
            return POSITION_NONE;
        }

        public void addFragment(Class<? extends Fragment> fragmentClass, String title) {
            mFragmentList.add(fragmentClass);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Utility.RESTAURANT_ID_KEY, restaurantID);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restaurantID = savedInstanceState.getString(Utility.RESTAURANT_ID_KEY);
    }
}
