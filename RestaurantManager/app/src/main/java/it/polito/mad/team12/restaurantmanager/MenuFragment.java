package it.polito.mad.team12.restaurantmanager;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {
    /*
     *   This fragment will allow adding new dishes and will split them into two categories,
     * Available and Unavailable. Dishes in the unavailable slot will automatically disallow
     * reservations containing them to be made from customers.
     */

    private static final int AVAILABLE_TAB_POSITION = 0;
    private static final int UNAVAILABLE_TAB_POSITION = 1;

    private static final int ADD_NEW_ITEM = 1;

    private ViewPager viewPager;
    private MenuTabsPagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    private List<RestaurantMenuItem> availableList = new ArrayList<>();
    private List<RestaurantMenuItem> unavailableList = new ArrayList<>();
    private SortedSet<RestaurantMenuItem> menuItems = new TreeSet<>();


    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        File jsonFile = new File(getActivity().getFilesDir(), "menu.xml");

        if(jsonFile.exists()) {
            menuItems = loadDataFromJsonFile();
        } else {
            // static initialization and first time storing to file
            RestaurantMenuItem item1 = new RestaurantMenuItem();
            RestaurantMenuItem item2 = new RestaurantMenuItem();
            RestaurantMenuItem item3 = new RestaurantMenuItem();
            RestaurantMenuItem item4 = new RestaurantMenuItem();

            item1.setName("Item 1");
            item1.setDescription("Description of the first item.");
            item1.setPrice(new BigDecimal(5.25));

            item2.setName("Item 2");
            item2.setDescription("Description of the second item.");
            item2.setPrice(new BigDecimal(3.2));

            item3.setName("Item 3");
            item3.setDescription("Description of the third item.");
            item3.setPrice(new BigDecimal(4.7));
            item3.setAvailable(false);

            item4.setName("Item 4");
            item4.setDescription("Description of the fourth item");
            item4.setPrice(new BigDecimal(1.2));
            item4.setAvailable(false);

            menuItems.add(item1);
            menuItems.add(item2);
            menuItems.add(item3);
            menuItems.add(item4);

            saveDataToJsonFile(menuItems);
        }

        for(RestaurantMenuItem menuItem: menuItems) {
            if(menuItem.isAvailable()) {
                availableList.add(menuItem);
            } else {
                unavailableList.add(menuItem);
            }
        }

    }

    // TODO try to adapt this method for the Utility class.
    private SortedSet<RestaurantMenuItem> loadDataFromJsonFile() {
        String filename = "menu.xml";
        FileInputStream fis;
        InputStreamReader isr;
        BufferedReader bufferedReader;
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            fis = getActivity().openFileInput(filename);
            isr = new InputStreamReader(fis);
            bufferedReader = new BufferedReader(isr);

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            fis.close();
        }catch (IOException e) {
            // TODO handle exception
        }

        String data = sb.toString();

        Gson gson = new Gson();
        Type menuType = new TypeToken<SortedSet<RestaurantMenuItem>>(){}.getType();
        TreeSet<RestaurantMenuItem> menuItems = gson.fromJson(data, menuType);

        return menuItems;
    }

    // TODO try to adapt this method for the Utility class.
    private void saveDataToJsonFile(SortedSet<RestaurantMenuItem> menuItems) {
        Gson gson = new Gson();
        Type menuType = new TypeToken<SortedSet<RestaurantMenuItem>>(){}.getType();
        String data = gson.toJson(menuItems, menuType);
        String filename = "menu.xml";

        FileOutputStream outputStream;

        try {
            outputStream = getActivity().getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View temp = inflater.inflate(R.layout.fragment_menu, container, false);

        Toolbar toolbar = (Toolbar) temp.findViewById(R.id.m_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.restaurant_menu_title);

        pagerAdapter = new MenuTabsPagerAdapter(getChildFragmentManager());

        viewPager = (ViewPager) temp.findViewById(R.id.m_view_pager);
        viewPager.setAdapter(pagerAdapter);

        tabLayout = (TabLayout) temp.findViewById(R.id.m_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

      return temp;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_restaurant_items, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.mt_add_new_item:
            // TODO start the activity that adds an item for result
                Intent intent = new Intent(getActivity(), AddMenuItemActivity.class);
                startActivityForResult(intent, ADD_NEW_ITEM);
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == AppCompatActivity.RESULT_OK) {
            if(requestCode == ADD_NEW_ITEM) {
                String receivedData;

                if((receivedData = data.getExtras().getString(AddMenuItemActivity.MENU_ITEM_DATA)) != null) {
                    Gson gson = new Gson();
                    RestaurantMenuItem menuItem = gson.fromJson(receivedData, RestaurantMenuItem.class);
                    if(menuItems.add(menuItem)) {   // if the item has no naming conflict with other items
                        availableList.add(menuItem);
                        Collections.sort(availableList);
                        ((MenuTabFragment) getChildFragmentManager().findFragmentByTag(getFragmentPagerAdapterTag(viewPager.getId(), AVAILABLE_TAB_POSITION))).notifyDataSetChanged();
                    }
                }
            }
        }
    }

    /**
     *  @param available A boolean indicating where the request is coming from the
     *                  AVAILABLE tab (true) or UNAVAILABLE tab (false)
     * @param position  The position where the item to be removed can be found in
     *                  the appropriate list
     */
    public void removeItem(boolean available, int position) {
        RestaurantMenuItem toBeRemoved;

        if(available) {
            toBeRemoved = availableList.remove(position);
            menuItems.remove(toBeRemoved);
        } else {
            toBeRemoved = unavailableList.remove(position);
            menuItems.remove(toBeRemoved);
        }
    }

    /**
     * @param available A boolean indicating where the request is coming from the
     *                  AVAILABLE tab (true) or UNAVAILABLE tab (false)
     * @param position  The position where the item to be moved can be found in
     *                  the appropriate list
     */
    public void moveItemFrom(boolean available, int position) {
        RestaurantMenuItem toBeMoved;

        if(available) { // moving from AVAILABLE tab to UNAVAILABLE
            toBeMoved = availableList.remove(position);
            toBeMoved.setAvailable(false);
            unavailableList.add(toBeMoved);
            Collections.sort(unavailableList);
            ((MenuTabFragment)getChildFragmentManager().findFragmentByTag(getFragmentPagerAdapterTag(viewPager.getId(), UNAVAILABLE_TAB_POSITION))).notifyDataSetChanged();
        } else {
            toBeMoved = unavailableList.remove(position);
            toBeMoved.setAvailable(true);
            availableList.add(toBeMoved);
            Collections.sort(availableList);
            ((MenuTabFragment)getChildFragmentManager().findFragmentByTag(getFragmentPagerAdapterTag(viewPager.getId(), AVAILABLE_TAB_POSITION))).notifyDataSetChanged();
        }
    }

    public void editItem(boolean available, int position, RestaurantMenuItem menuItem) {
        RestaurantMenuItem toBeReplaced;

        if(available) {
            toBeReplaced = availableList.remove(position);
            menuItems.remove(toBeReplaced);
            if(!menuItems.add(menuItem)) {  // if you can't add this item, rollback the change
                menuItems.add(toBeReplaced);
                availableList.add(toBeReplaced);
            }
            Collections.sort(availableList);
            saveDataToJsonFile(menuItems);
        } else {
            toBeReplaced = unavailableList.remove(position);
            menuItems.remove(toBeReplaced);
            if(!menuItems.add(menuItem)) {  // if you can't add this item, rollback the change
                menuItems.add(toBeReplaced);
                unavailableList.add(toBeReplaced);
            }
            Collections.sort(unavailableList);
            saveDataToJsonFile(menuItems);
        }
    }

    class MenuTabsPagerAdapter extends FragmentPagerAdapter {
        private static final int PAGE_COUNT = 2;
        private String tabTitles[];

        public MenuTabsPagerAdapter(FragmentManager fm) {
            super(fm);
            tabTitles = new String[PAGE_COUNT];
            tabTitles[0] = getResources().getString(R.string.menu_available_tab_title).toUpperCase();
            tabTitles[1] = getResources().getString(R.string.menu_unavailable_tab_title).toUpperCase();
        }

        @Override
        public Fragment getItem(int position) {
            MenuTabFragment result;

            switch(position){
                case 0:
                    result = MenuTabFragment.newInstance(true);
                    break;
                case 1:
                    result = MenuTabFragment.newInstance(false);
                    break;
                default:
                    result = null;
                    break;
            }

            return result;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

    public List<RestaurantMenuItem> getAvailableList() {
        return availableList;
    }

    public List<RestaurantMenuItem> getUnavailableList() {
        return unavailableList;
    }

    // helper method to retrieve the tag fragmentpageradapter gives to its created fragments
    private String getFragmentPagerAdapterTag(int viewPagerId, int fragmentPosition)
    {
        return "android:switcher:" + viewPagerId + ":" + fragmentPosition;
    }

    @Override
    public void onPause() {
        super.onPause();

        saveDataToJsonFile(menuItems);
    }
}