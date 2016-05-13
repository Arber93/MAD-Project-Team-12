package it.polito.mad.team12.restaurantmanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;


public class ShowMenuFragment extends Fragment {
    private final static String RESTAURANT_NAME="restaurant name";

    private ContextMenuRecyclerView recyclerView;
    private ShowMenuRecyclerAdapter adapter;
    private String restaurantName;

    public ShowMenuFragment() {
        // Required empty public constructor
    }

    public static ShowMenuFragment newInstance(String restaurantName){
        ShowMenuFragment fragment = new ShowMenuFragment();
        Bundle args = new Bundle();
        args.putString(RESTAURANT_NAME, restaurantName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.restaurantName = this.getArguments().getString(RESTAURANT_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_menu, container, false);

        setUpRecyclerView(view);
        return view;
    }

    private void setUpRecyclerView(View container) {
        recyclerView = (ContextMenuRecyclerView) container.findViewById(R.id.sm_recycler_view);
        Query query = Utility.getMenuItemsFrom(this.restaurantName).orderByChild("available").equalTo(true);
        adapter = new ShowMenuRecyclerAdapter(ItemData.class, R.layout.item_menu, ShowMenuViewHolder.class, query);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new Utility.SimpleDividerItemDecoration(getActivity()));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public class ShowMenuRecyclerAdapter extends FirebaseRecyclerAdapter<ItemData, ShowMenuViewHolder> {

        public ShowMenuRecyclerAdapter(Class<ItemData> modelClass, int modelLayout, Class<ShowMenuViewHolder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(ShowMenuViewHolder showMenuViewHolder, ItemData itemData, int position) {
            showMenuViewHolder.setData(new RestaurantMenuItem(itemData), position);
            showMenuViewHolder.setListeners();
        }

        @Override
        public ShowMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ShowMenuViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
            viewHolder.passContext(getActivity());
            return viewHolder;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //adapter.cleanup();
    }
}
