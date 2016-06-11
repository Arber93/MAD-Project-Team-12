package it.polito.mad.team12.restaurantmanager.menu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.Calendar;

import it.polito.mad.team12.restaurantmanager.R;
import it.polito.mad.team12.restaurantmanager.Utility;

public class ShowOffersActivity extends AppCompatActivity {

    private Context context = this;
    private ContextMenuRecyclerView recyclerView;
    private ShowOffersRecyclerAdapter adapter;
    private String restaurantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offers);

        Toolbar toolbar = (Toolbar) findViewById(R.id.so_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.show_offers_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        restaurantName = intent.getExtras().getString(Utility.RESTAURANT_ID_KEY);

        setUpRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpRecyclerView() {
        recyclerView = (ContextMenuRecyclerView) findViewById(R.id.so_recycler_view);
        Query query = chooseQuery();
        adapter = new ShowOffersRecyclerAdapter(OfferData.class, R.layout.item_menu_customer, ShowOffersViewHolder.class, query);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new Utility.SimpleDividerItemDecoration(this, Utility.SimpleDividerItemDecoration.PADDING_LEFT, (int)getResources().getDimension(R.dimen.cardview_image_width)));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private Query chooseQuery() {
        Firebase firebase = Utility.getMenuOffersFrom(restaurantName);
        Query query = null;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                query = firebase.orderByChild("offeredMonday").equalTo(true);
                break;
            case Calendar.TUESDAY:
                query = firebase.orderByChild("offeredTuesday").equalTo(true);
                break;
            case Calendar.WEDNESDAY:
                query = firebase.orderByChild("offeredWednesday").equalTo(true);
                break;
            case Calendar.THURSDAY:
                query = firebase.orderByChild("offeredThursday").equalTo(true);
                break;
            case Calendar.FRIDAY:
                query = firebase.orderByChild("offeredFriday").equalTo(true);
                break;
            case Calendar.SATURDAY:
                query = firebase.orderByChild("offeredSaturday").equalTo(true);
                break;
            case Calendar.SUNDAY:
                query = firebase.orderByChild("offeredSunday").equalTo(true);
                break;
        }

        return query;
    }

    public class ShowOffersRecyclerAdapter extends FirebaseRecyclerAdapter<OfferData, ShowOffersViewHolder> {

        public ShowOffersRecyclerAdapter(Class<OfferData> modelClass, int modelLayout, Class<ShowOffersViewHolder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(ShowOffersViewHolder showOffersViewHolder, OfferData offerData, int position) {
            showOffersViewHolder.setData(offerData, position);
            showOffersViewHolder.setListeners();
        }

        @Override
        public ShowOffersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ShowOffersViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
            viewHolder.passContext(context);
            return viewHolder;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }
}
