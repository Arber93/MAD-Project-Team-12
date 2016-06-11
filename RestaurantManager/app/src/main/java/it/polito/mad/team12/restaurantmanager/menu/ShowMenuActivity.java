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

import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;

import it.polito.mad.team12.restaurantmanager.R;
import it.polito.mad.team12.restaurantmanager.Utility;

public class ShowMenuActivity extends AppCompatActivity {

    private Context context = this;
    private ContextMenuRecyclerView recyclerView;
    private ShowMenuRecyclerAdapter adapter;
    private String restaurantName;
	private String categoryId;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_menu);

        Intent intent = getIntent();

        restaurantName = intent.getExtras().getString(Utility.RESTAURANT_ID_KEY);
        categoryId = intent.getExtras().getString(Utility.CATEGORY_ID_KEY);

        setUpRecyclerView();

        String categoryName;

        switch (categoryId) {
            case ItemData.FIRST_COURSES:
                categoryName = getString(R.string.first_courses);
                break;
            case ItemData.SECOND_COURSES:
                categoryName = getString(R.string.second_courses);
                break;
            case ItemData.SIDE_DISHES:
                categoryName = getString(R.string.side_dishes);
                break;
            case ItemData.DRINKS:
                categoryName = getString(R.string.drinks);
                break;
            case ItemData.DESSERTS:
                categoryName = getString(R.string.desserts);
                break;
            case ItemData.OTHERS:
                categoryName = getString(R.string.others);
                break;
            default:
                categoryName = null;
                break;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.sm_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        recyclerView = (ContextMenuRecyclerView) findViewById(R.id.sm_recycler_view);
        Query query = Utility.getMenuItemsFrom(restaurantName, true).orderByChild("category").equalTo(categoryId);
        adapter = new ShowMenuRecyclerAdapter(ItemData.class, R.layout.item_menu_customer, ShowMenuViewHolder.class, query);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new Utility.SimpleDividerItemDecoration(this, Utility.SimpleDividerItemDecoration.PADDING_LEFT, (int)getResources().getDimension(R.dimen.cardview_image_width)));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
	
	public class ShowMenuRecyclerAdapter extends FirebaseRecyclerAdapter<ItemData, ShowMenuViewHolder> {

        public ShowMenuRecyclerAdapter(Class<ItemData> modelClass, int modelLayout, Class<ShowMenuViewHolder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(ShowMenuViewHolder showMenuViewHolder, ItemData itemData, int position) {
            showMenuViewHolder.passRestaurantId(restaurantName);
            showMenuViewHolder.setData(itemData, position);
            showMenuViewHolder.setListeners();
        }

        @Override
        public ShowMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ShowMenuViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
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
