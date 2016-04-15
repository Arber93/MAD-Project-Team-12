package it.polito.mad.team12.restaurantmanager;


import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class MenuTabFragment extends Fragment {
    private final static String AVAILABLE_ARG = "AVAILABLE_ARG";
    private final static int EDIT_MENU_ITEM = 1;

    private boolean available;
    private List<RestaurantMenuItem> list;
    private RecyclerView recyclerView;
    private MenuTabRecyclerAdapter adapter;

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

        if(this.available) {
            list = ((MenuFragment)getParentFragment()).getAvailableList();
        } else {
            list = ((MenuFragment)getParentFragment()).getUnavailableList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_tab, container, false);

        //TODO Handle the menu

        setUpRecyclerView(view);

        return view;
    }

    private void setUpRecyclerView(View container) {
        recyclerView = (RecyclerView) container.findViewById(R.id.mt_recycler_view);
        adapter = new MenuTabRecyclerAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    public class MenuTabRecyclerAdapter extends RecyclerView.Adapter<MenuTabRecyclerAdapter.MenuTabViewHolder> {
        private List<RestaurantMenuItem> items;
        private LayoutInflater inflater;

        public MenuTabRecyclerAdapter(Context context, List<RestaurantMenuItem> items){
            this.items = items;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public MenuTabRecyclerAdapter.MenuTabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_menu, parent, false);
            MenuTabViewHolder holder = new MenuTabViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MenuTabRecyclerAdapter.MenuTabViewHolder holder, int position) {
            holder.setListeners();

            RestaurantMenuItem item = items.get(position);
            holder.setData(item, position);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class MenuTabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
            private TextView itemName;
            private TextView itemDescription;
            private TextView itemPrice;
            private ImageView imgItem;
            private ImageView imgMore;
            private int position;
            // The system for currency handling might be made more sophisticated if necessary
            private Currency italianCurrency = Currency.getInstance(Locale.ITALY);

            public MenuTabViewHolder(View itemView) {
                super(itemView);
                itemName = (TextView) itemView.findViewById(R.id.mt_item_name);
                itemDescription = (TextView) itemView.findViewById(R.id.mt_item_description);
                itemPrice = (TextView) itemView.findViewById(R.id.mt_item_price);
                imgItem = (ImageView) itemView.findViewById(R.id.mt_item_image);
                imgMore = (ImageView) itemView.findViewById(R.id.mt_overflow_menu);
            }

            public void setData(RestaurantMenuItem item, int position) {
                itemName.setText(item.getName());
                itemDescription.setText(item.getDescription());
                itemPrice.setText(item.getPrice().toString() + " " + italianCurrency.getSymbol());
                if(item.getImageUri() == null) {
                    // TODO handle insertion of default image
                } else {
                    // TODO load the provided image
                }
                this.position = position;
            }

            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.mt_overflow_menu:
                        PopupMenu popup = new PopupMenu(((AppCompatActivity)getActivity()).getSupportActionBar().getThemedContext(), v);
                        if (available) {
                            popup.inflate(R.menu.menu_popup_available_tab);
                        } else {
                            popup.inflate(R.menu.menu_popup_unavailable_tab);
                        }

                        popup.setOnMenuItemClickListener(this);
                        popup.show();

                        break;
                }
            }

            public void setListeners() {
                imgMore.setOnClickListener(MenuTabViewHolder.this);
            }

            public void removeItem(int position) {
                ((MenuFragment)getParentFragment()).removeItem(available, position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, items.size());
            }

            public void moveItem(int position) {
                ((MenuFragment)getParentFragment()).moveItemFrom(available, position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, items.size());
            }

            public void editItem(int position, RestaurantMenuItem menuItem) {
                ((MenuFragment)getParentFragment()).editItem(available, position, menuItem);
                notifyDataSetChanged();
            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                boolean result = true;

                switch (item.getItemId()) {
                    case R.id.mct_move:
                        moveItem(position);
                        break;
                    case R.id.mct_edit:
                        Gson gson = new Gson();
                        String data = gson.toJson(items.get(position));
                        Bundle bundle = new Bundle();
                        Intent intent = new Intent(getActivity(), AddMenuItemActivity.class);

                        bundle.putString(AddMenuItemActivity.MENU_ITEM_DATA, data);
                        bundle.putInt(AddMenuItemActivity.MENU_ITEM_POSITION, position);

                        intent.putExtras(bundle);
                        startActivityForResult(intent, EDIT_MENU_ITEM);
                        break;
                    case R.id.mct_delete:
                        removeItem(position);
                        break;
                    default:
                        result = false;
                        break;
                }

                return result;
            }
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
                    MenuTabRecyclerAdapter.MenuTabViewHolder viewHolder = (MenuTabRecyclerAdapter.MenuTabViewHolder)recyclerView.getChildViewHolder(recyclerView.getChildAt(position));
                    viewHolder.editItem(position, menuItem);

                }
            }
        }
    }

    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                mDivider = context.getDrawable(R.drawable.horizontal_divider);
            } else {
                mDivider = context.getResources().getDrawable(R.drawable.horizontal_divider);
            }
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
