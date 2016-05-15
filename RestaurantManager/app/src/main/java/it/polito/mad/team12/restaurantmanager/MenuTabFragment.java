package it.polito.mad.team12.restaurantmanager;


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


public class MenuTabFragment extends Fragment {
    private final static String AVAILABLE_ARG = "AVAILABLE_ARG";
    private final static int EDIT_MENU_ITEM = 1;

    private boolean available;
    private ContextMenuRecyclerView recyclerView;
    private MenuTabFragment.MenuTabRecyclerAdapter adapter;


    public MenuTabFragment() {
        // Required empty public constructor
    }

    public static MenuTabFragment newInstance(boolean available) {
        MenuTabFragment fragment = new MenuTabFragment();
        Bundle args = new Bundle();
        args.putBoolean(AVAILABLE_ARG, available);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.available = getArguments().getBoolean(AVAILABLE_ARG);
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
        Query query = Utility.getMenuItemsFrom("restID").orderByChild("available").equalTo(available);
        adapter = new MenuTabRecyclerAdapter(ItemData.class, R.layout.item_menu, MenuTabViewHolder.class, query);
        recyclerView.setAdapter(adapter);

        registerForContextMenu(recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new Utility.SimpleDividerItemDecoration(getActivity()));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public class MenuTabRecyclerAdapter extends FirebaseRecyclerAdapter<ItemData, MenuTabViewHolder> {

        public MenuTabRecyclerAdapter(Class<ItemData> modelClass, int modelLayout, Class<MenuTabViewHolder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(MenuTabViewHolder menuTabViewHolder, ItemData itemData, int position) {
            menuTabViewHolder.setData(new RestaurantMenuItem(itemData), position);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == AppCompatActivity.RESULT_OK) {
            if(requestCode == EDIT_MENU_ITEM) {
                String receivedData;

                if((receivedData = data.getExtras().getString(AddMenuItemActivity.MENU_ITEM_DATA)) != null) {
                    Gson gson = new Gson();
                    RestaurantMenuItem menuItem = gson.fromJson(receivedData, RestaurantMenuItem.class);
                    int position = data.getExtras().getInt(AddMenuItemActivity.MENU_ITEM_POSITION);
                    MenuTabViewHolder viewHolder = (MenuTabViewHolder)recyclerView.getChildViewHolder(recyclerView.getChildAt(position));
                    //viewHolder.editItem(position, menuItem);
                }
            }
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
        int pos = info.getPosition();
        Firebase fb;

        switch (item.getItemId()) {
            case R.id.mct_move:
                fb = Utility.getMenuItemsFrom("restID").child(viewHolder.itemName.getText().toString());
                HashMap<String, Object> map = new HashMap<>();
                if(item.getTitle().toString().equals(getResources().getString(R.string.mcat_move_title))){
                    map.put("available", Boolean.valueOf(false));
                } else {
                    map.put("available", Boolean.valueOf(true));
                }
                fb.updateChildren(map);
                break;
            case R.id.mct_edit:
                //viewHolder.requestEdit(pos);
                break;
            case R.id.mct_delete:
                fb = Utility.getMenuItemsFrom("restID").child(viewHolder.itemName.getText().toString());
                fb.setValue(null);
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