package it.polito.mad.team12.restaurantmanager.menu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;

import it.polito.mad.team12.restaurantmanager.R;
import it.polito.mad.team12.restaurantmanager.Utility;


public class OffersFragment extends Fragment {

    private String restaurantID;
    private ContextMenuRecyclerView recyclerView;
    private static final String RESTAURANT_ID = "American Graffiti Via Lagrange 58";
    private OffersRecyclerAdapter adapter;

    public OffersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View temp = inflater.inflate(R.layout.fragment_offers, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.restaurant_offers_title);

        setUpRecyclerView(temp);

        return temp;
    }

    private void setUpRecyclerView(View container) {
        recyclerView = (ContextMenuRecyclerView) container.findViewById(R.id.offers_recycler_view);
        Query query = Utility.getMenuOffersFrom(RESTAURANT_ID).orderByKey();
        adapter = new OffersRecyclerAdapter(OfferData.class, R.layout.item_menu, OffersViewHolder.class, query);
        recyclerView.setAdapter(adapter);

        registerForContextMenu(recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new Utility.SimpleDividerItemDecoration(getActivity(), Utility.SimpleDividerItemDecoration.PADDING_LEFT, (int) getResources().getDimension(R.dimen.cardview_image_width)));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public class OffersRecyclerAdapter extends FirebaseRecyclerAdapter<OfferData, OffersViewHolder> {

        public OffersRecyclerAdapter(Class<OfferData> modelClass, int modelLayout, Class<OffersViewHolder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(OffersViewHolder offersViewHolder, OfferData offerData, int position) {
            offersViewHolder.setData(offerData, position);
            offersViewHolder.passRestaurantID(RESTAURANT_ID);
            offersViewHolder.passContext(getActivity());
            offersViewHolder.setListeners();
        }

        @Override
        public OffersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            OffersViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
            viewHolder.setRecyclerView(recyclerView);
            return viewHolder;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_restaurant_offers, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.offers_add_new_item:
                Intent intent = new Intent(getActivity(), AddMenuOfferActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Utility.RESTAURANT_ID_KEY, RESTAURANT_ID);
                bundle.putBoolean(Utility.EDIT_MODE_KEY, false);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_context_offers, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ContextMenuRecyclerView.RecyclerViewContextMenuInfo info = (ContextMenuRecyclerView.RecyclerViewContextMenuInfo) item.getMenuInfo();
        OffersViewHolder viewHolder = ((OffersViewHolder)info.getViewHolder());

        switch (item.getItemId()) {
            case R.id.mco_edit:
                viewHolder.requestEdit();
                break;
            case R.id.mco_delete:
                viewHolder.requestRemoval();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }

}
