package it.polito.mad.team12.restaurantmanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;


public class ShowOffersFragment extends Fragment {
    private final static String RESTAURANT_NAME="restaurant name";

    private ContextMenuRecyclerView recyclerView;
    private ShowOffersRecyclerAdapter adapter;
    private String restaurantName;

    public ShowOffersFragment() {
        // Required empty public constructor
    }

    public static ShowOffersFragment newInstance(String restaurantName){
        ShowOffersFragment fragment = new ShowOffersFragment();
        Bundle args = new Bundle();
        args.putString(RESTAURANT_NAME, restaurantName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getActivity().getApplicationContext());
        this.restaurantName = this.getArguments().getString(RESTAURANT_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_offers, container, false);

        setUpRecyclerView(view);
        return view;
    }

    private void setUpRecyclerView(View container) {
        recyclerView = (ContextMenuRecyclerView) container.findViewById(R.id.so_recycler_view);
        Query query = Utility.getMenuOffersFrom(this.restaurantName).orderByChild("available").equalTo(true);
        adapter = new ShowOffersRecyclerAdapter(OfferData.class, R.layout.item_menu, ShowOffersViewHolder.class, query);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new Utility.SimpleDividerItemDecoration(getActivity()));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
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
            viewHolder.passContext(getActivity());
            return viewHolder;
        }
    }

}
