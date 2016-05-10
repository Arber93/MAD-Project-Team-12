package it.polito.mad.team12.restaurantmanager;

import android.content.Intent;
import android.content.res.Configuration;
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

import com.firebase.client.Firebase;

import java.io.IOException;

import it.polito.mad.team12.restaurantmanager.review.ReviewUtility;
import it.polito.mad.team12.restaurantmanager.review.ReviewsFragment;
import it.polito.mad.team12.restaurantmanager.review.ReviewsInsertFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    String restaurantID = "Nome Ristorante1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

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

        transaction = manager.beginTransaction();
        transaction.replace(R.id.flContent, new DetailsFragment());
        transaction.commit();

    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
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
                startActivity(new Intent(MainActivity.this, ReservationsActivity.class));
                activity_started = true; // I don't know of the try-catch block is executed anyway
                break;
            //TODO list here all the other cases with fragments
            case R.id.nav_details_fragment:
                transaction = manager.beginTransaction();
                transaction.replace(R.id.flContent, new DetailsFragment());
                transaction.commit();
                break;
            case R.id.nav_menu_fragment:
                transaction = manager.beginTransaction();
                transaction.replace(R.id.flContent, new MenuFragment());
                transaction.commit();
                break;
            case R.id.nav_review_fragment:
                Fragment reviewsFragment = new ReviewsFragment();

                transaction = manager.beginTransaction();
                transaction.replace(R.id.flContent, reviewsFragment);
                transaction.commit();
                break;
            default:
                //fragmentClass = DefaultFragment.class;
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

}
