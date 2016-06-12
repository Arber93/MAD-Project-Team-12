package it.polito.mad.team12.restaurantmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import it.polito.mad.team12.restaurantmanager.menu.MenuFragment;
import it.polito.mad.team12.restaurantmanager.menu.OffersFragment;
import it.polito.mad.team12.restaurantmanager.review.ReviewUtility;
import it.polito.mad.team12.restaurantmanager.review.ReviewsFragment;
import it.polito.mad.team12.restaurantmanager.review.ReviewsInsertFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;

    private static final String LAST_FRAGMENT = "last fragment";

    private static final String DETAILS = "details";
    private static final String MENU = "menu";
    private static final String OFFERS = "offers";
    private static final String REVIEWS = "reviews";

    private String restaurantID;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private String nameOfUser;
    private String currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        pref = getSharedPreferences("testapp", MODE_PRIVATE);
        String restname = pref.getString("restID", null);

        this.restaurantID= restname;

        setContentView(R.layout.activity_main);

        //setup Firebase
        Firebase.setAndroidContext(this);
        ReviewUtility.loadReviews(restaurantID);
        ReviewUtility.loadRestaurant(restaurantID);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction;

        if(savedInstanceState == null) {
            transaction = manager.beginTransaction();
            transaction.replace(R.id.flContent, DetailsFragment.newInstance(restaurantID));
            transaction.commit();
        } else {
            currentFragment = savedInstanceState.getString(LAST_FRAGMENT);
            restaurantID = savedInstanceState.getString(Utility.RESTAURANT_ID_KEY);
            returnToCurrentFragment();
        }

    }

    private void returnToCurrentFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction;

        if(currentFragment == null) {
            transaction = manager.beginTransaction();
            currentFragment = DETAILS;
            transaction.replace(R.id.flContent, DetailsFragment.newInstance(restaurantID));
            transaction.commit();
        } else {
            switch (currentFragment) {
                case DETAILS:
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.flContent, DetailsFragment.newInstance(restaurantID));
                    transaction.commit();
                    break;
                case MENU:
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.flContent, MenuFragment.newInstance(restaurantID));
                    transaction.commit();
                    break;
                case OFFERS:
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.flContent, OffersFragment.newInstance(restaurantID));
                    transaction.commit();
                    break;
                case REVIEWS:
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.flContent, new ReviewsFragment());
                    transaction.commit();
                    break;
            }
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        pref = getSharedPreferences("testapp", MODE_PRIVATE);
        nameOfUser = pref.getString("nameU",null);


        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header);
        TextView profileLogout;
        TextView managerName;
        profileLogout = (TextView) headerLayout.findViewById(R.id.ic_drawer_logout);
        managerName = (TextView) headerLayout.findViewById(R.id.ic_drawer_name);

        managerName.setText(nameOfUser);

        profileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction;
        boolean activity_started = false;
        switch(menuItem.getItemId()) {
            case R.id.nav_reservations_activity:
                Intent intent = new Intent(this, ReservationsActivity.class);
                Bundle extras = new Bundle();
                extras.putString(Utility.RESTAURANT_ID_KEY, restaurantID);
                intent.putExtras(extras);
                startActivity(intent);
                activity_started = true; // I don't know of the try-catch block is executed anyway
                break;
            case R.id.nav_details_fragment:
                transaction = manager.beginTransaction();
                currentFragment = DETAILS;
                transaction.replace(R.id.flContent, DetailsFragment.newInstance(restaurantID));
                transaction.commit();
                break;
            case R.id.nav_menu_fragment:
                transaction = manager.beginTransaction();
                currentFragment = MENU;
                transaction.replace(R.id.flContent, MenuFragment.newInstance(restaurantID));
                transaction.commit();
                break;
            case R.id.nav_offers_fragment:
                transaction = manager.beginTransaction();
                currentFragment = OFFERS;
                transaction.replace(R.id.flContent, OffersFragment.newInstance(restaurantID));
                transaction.commit();
                break;
            case R.id.nav_review_fragment:
                transaction = manager.beginTransaction();
                currentFragment = REVIEWS;
                transaction.replace(R.id.flContent, new ReviewsFragment());
                transaction.commit();
                break;
        }

        if (!activity_started) {
//            try {
//                fragment = (Fragment) fragmentClass.newInstance();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            // Insert the fragment by replacing any existing fragment
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (drawerToggle.onOptionsItemSelected(item)) {

            switch (item.getItemId()) {
                case android.R.id.home:
                    mDrawer.openDrawer(GravityCompat.START);
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public String retrieveRestID(){
        String restID=restaurantID;
        return restID;
    }

    @Override
    public void onBackPressed(){

    }

    public void logout(){
        System.out.println("I HAVE LOGGED OUT!! ");
        editor = pref.edit();
        editor.putString("loggedin","false");
        editor.commit();
        Intent i = new Intent(this, MainLoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LAST_FRAGMENT, currentFragment);
        outState.putString(Utility.RESTAURANT_ID_KEY, restaurantID);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentFragment = savedInstanceState.getString(LAST_FRAGMENT);
        restaurantID = savedInstanceState.getString(Utility.RESTAURANT_ID_KEY);
    }
}
