package it.polito.mad.team12.restaurantmanager.menu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.gson.Gson;

import java.util.HashMap;

import it.polito.mad.team12.restaurantmanager.R;
import it.polito.mad.team12.restaurantmanager.Utility;


public class MenuTabFragment extends Fragment {
    private static final String AVAILABLE_ARG = "AVAILABLE_ARG";

    private boolean available;
    private ContextMenuRecyclerView recyclerView;
    private MenuTabFragment.MenuTabRecyclerAdapter adapter;
    private String restaurantID;


    public MenuTabFragment() {
        // Required empty public constructor
    }

    public static MenuTabFragment newInstance(boolean available, String restaurantID) {
        MenuTabFragment fragment = new MenuTabFragment();
        Bundle args = new Bundle();
        args.putBoolean(AVAILABLE_ARG, available);
        args.putString(Utility.RESTAURANT_ID_KEY, restaurantID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.available = getArguments().getBoolean(AVAILABLE_ARG);
        this.restaurantID = getArguments().getString(Utility.RESTAURANT_ID_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_tab, container, false);
        setUpRecyclerView(view);
        return view;
    }

    private void setUpRecyclerView(View container) {
        recyclerView = (ContextMenuRecyclerView) container.findViewById(R.id.mt_recycler_view);
        Query query = Utility.getMenuItemsFrom(restaurantID, available).orderByKey();
        adapter = new MenuTabRecyclerAdapter(ItemData.class, R.layout.item_menu, MenuTabViewHolder.class, query);
        recyclerView.setAdapter(adapter);

        registerForContextMenu(recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new Utility.SimpleDividerItemDecoration(getActivity(), Utility.SimpleDividerItemDecoration.PADDING_LEFT, (int)getResources().getDimension(R.dimen.cardview_image_width)));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public class MenuTabRecyclerAdapter extends FirebaseRecyclerAdapter<ItemData, MenuTabViewHolder> {

        public MenuTabRecyclerAdapter(Class<ItemData> modelClass, int modelLayout, Class<MenuTabViewHolder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(MenuTabViewHolder menuTabViewHolder, ItemData itemData, int position) {
            menuTabViewHolder.setData(itemData, position);
            menuTabViewHolder.passRestaurantID(restaurantID);
            menuTabViewHolder.passContext(getActivity());
            menuTabViewHolder.setListeners();
        }

        @Override
        public MenuTabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MenuTabViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
            viewHolder.setRecyclerView(recyclerView);
            return viewHolder;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        int id = available ? R.menu.menu_context_available_tab : R.menu.menu_context_unavailable_tab;
        inflater.inflate(id, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ContextMenuRecyclerView.RecyclerViewContextMenuInfo info = (ContextMenuRecyclerView.RecyclerViewContextMenuInfo) item.getMenuInfo();
        MenuTabViewHolder viewHolder = ((MenuTabViewHolder)info.getViewHolder());

        switch (item.getItemId()) {
            case R.id.mct_move:
                viewHolder.requestMove();
                break;
            case R.id.mct_edit:
                viewHolder.requestEdit();
                break;
            case R.id.mct_delete:
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