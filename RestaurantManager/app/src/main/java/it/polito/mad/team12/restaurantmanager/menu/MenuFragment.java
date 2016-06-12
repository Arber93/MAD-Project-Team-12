package it.polito.mad.team12.restaurantmanager.menu;

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

import it.polito.mad.team12.restaurantmanager.R;
import it.polito.mad.team12.restaurantmanager.Utility;


public class MenuFragment extends Fragment {
    /*
     *   This fragment will allow adding new dishes and will split them into two categories,
     * Available and Unavailable. Dishes in the unavailable slot will automatically disallow
     * reservations containing them to be made from customers.
     */

    private ViewPager viewPager;
    private MenuTabsPagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private String restaurantID;


    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance(String restaurantID) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(Utility.RESTAURANT_ID_KEY, restaurantID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.restaurantID = getArguments().getString(Utility.RESTAURANT_ID_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View temp = inflater.inflate(R.layout.fragment_menu, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
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
                Intent intent = new Intent(getActivity(), AddMenuItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Utility.RESTAURANT_ID_KEY, restaurantID);
                bundle.putBoolean(Utility.EDIT_MODE_KEY, false);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
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
                    result = MenuTabFragment.newInstance(true, restaurantID);
                    break;
                case 1:
                    result = MenuTabFragment.newInstance(false, restaurantID);
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

    // helper method to retrieve the tag fragmentpageradapter gives to its created fragments
    private String getFragmentPagerAdapterTag(int viewPagerId, int fragmentPosition)
    {
        return "android:switcher:" + viewPagerId + ":" + fragmentPosition;
    }
}
