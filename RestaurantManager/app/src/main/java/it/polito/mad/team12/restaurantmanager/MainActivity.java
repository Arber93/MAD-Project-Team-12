package it.polito.mad.team12.restaurantmanager;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import it.polito.mad.team12.restaurantmanager.reservation.ReservationsFragment;
import it.polito.mad.team12.restaurantmanager.review.ReviewsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    /* This activity will house the navigation drawer as well as be in charge of fragment switching. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //NAVIGATION DRAWER
        setContentView(R.layout.activity_navigation_drawer);
        //setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //SUPPOSING we want to start the activity from a Reservation View
        ReservationsFragment fragment_container = new ReservationsFragment();


//---   container for the currently displayed fragment, in which you can insert the FRAGMENT!
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new RESERVATION Fragment to be placed in the activity layout
            ReservationsFragment firstFragment = new ReservationsFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
        }


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button,
        int id = item.getItemId();

        Log.i("preeeeeeeeees :(", id + "");
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //FRAGMENTS SWITCHING -----------------------------------------------------------

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft;

        Log.i("preeeeeeeeees", id + "");

        if (id == R.id.drawm_details) {  //USER TAPPED ON DETAILS
            DetailsFragment det = new DetailsFragment();

            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, det);   //switch to the DETAILS fragment
            ft.commit();

        } else if (id == R.id.drawm_reservations) {   //RESERVATIONS
            ReservationsFragment res = new ReservationsFragment();


            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, res);   //switch to the DETAILS fragment
            ft.commit();

        } else if (id == R.id.drawm_offers) {  //OFFERS
            OffersFragment offr = new OffersFragment();


            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, offr);   //switch to the DETAILS fragment
            ft.commit();

        } else if (id == R.id.drawm_menu) {  //MENU
            MenuFragment menf = new MenuFragment();


            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, menf);   //switch to the DETAILS fragment
            ft.commit();

        } else if (id == R.id.drawm_reviews) { //REVIEWS
            ReviewsFragment rev = new ReviewsFragment();
            Log.i("************", "reviews");

            Bundle bundle = new Bundle();
            bundle.putString("restaurantID", "Nome Ristorante1");
            rev.setArguments(bundle);

            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, rev);   //switch to the DETAILS fragment
            ft.commit();

        } else if (id == R.id.drawm_stats) {  //STATISTICS

        } else if (id == R.id.profile_manager) {  //Profile

        } else if (id == R.id.profile_settings) {  //General Settings ?

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
