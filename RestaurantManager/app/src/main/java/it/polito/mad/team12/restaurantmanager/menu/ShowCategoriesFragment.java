package it.polito.mad.team12.restaurantmanager.menu;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import it.polito.mad.team12.restaurantmanager.R;
import it.polito.mad.team12.restaurantmanager.Utility;


public class ShowCategoriesFragment extends Fragment {

    private ListView listView;
    private CategoryAdapter listAdapter;
    private String restaurantName;

    public ShowCategoriesFragment() {
        // Required empty public constructor
    }

    public static ShowCategoriesFragment newInstance(String restaurantName){
        ShowCategoriesFragment fragment = new ShowCategoriesFragment();
        Bundle args = new Bundle();
        args.putString(Utility.RESTAURANT_ID_KEY, restaurantName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.restaurantName = this.getArguments().getString(Utility.RESTAURANT_ID_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_categories, container, false);

        listView = (ListView) view.findViewById(R.id.sc_listview);

        List<CategoryData> categoryData = new LinkedList<>();

        categoryData.add(new CategoryData(ItemData.FIRST_COURSES, getResources().getString(R.string.first_courses)));
        categoryData.add(new CategoryData(ItemData.SECOND_COURSES, getResources().getString(R.string.second_courses)));
        categoryData.add(new CategoryData(ItemData.SIDE_DISHES, getResources().getString(R.string.side_dishes)));
        categoryData.add(new CategoryData(ItemData.DRINKS, getResources().getString(R.string.drinks)));
        categoryData.add(new CategoryData(ItemData.DESSERTS, getResources().getString(R.string.desserts)));
        categoryData.add(new CategoryData(ItemData.OTHERS, getResources().getString(R.string.others)));
        categoryData.add(new CategoryData(OfferData.OFFERS, getResources().getString(R.string.offers)));

        listAdapter = new CategoryAdapter(getActivity(), categoryData, restaurantName);

        for(int i = 0; i < listAdapter.getCount(); i++) {
            if(i != listAdapter.getCount() - 1) {
                View currentView = null;
                listAdapter.getView(i, currentView, container);
            }
        }

        listView.setAdapter(listAdapter);

        return view;
    }

    public class CategoryAdapter extends BaseAdapter {
        private Context context;
        private final List<CategoryData> values;
        private String restaurantName;

        public CategoryAdapter(Context context, List<CategoryData> values, String restaurantName) {
            this.context = context;
            this.values = values;
            this.restaurantName = restaurantName;
        }

        @Override
        public int getCount() {
            return values.size();
        }

        @Override
        public Object getItem(int position) {
            return values.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = li.inflate(R.layout.item_menu_category, null);
            }

            final CategoryData data = values.get(position);

            TextView textView = (TextView) convertView.findViewById(R.id.category_text);
            textView.setText(data.getCategoryName());

            if(position != values.size() - 1) { // for categories different from the offer category
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ShowMenuActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString(Utility.RESTAURANT_ID_KEY, restaurantName);
                        extras.putString(Utility.CATEGORY_ID_KEY, data.getCategoryId());
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                });
            } else {    // for the offer category
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ShowOffersActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString(Utility.RESTAURANT_ID_KEY, restaurantName);
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                });
            }

            return convertView;
        }
    }

}
