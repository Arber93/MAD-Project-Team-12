package it.polito.mad.team12.restaurantmanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {
    /*
     *   This fragment will allow adding new dishes and will split them into two categories,
     * Available and Unavailable. Dishes in the unavailable slot will automatically disallow
     * reservations containing them to be made from customers.
     */

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

}
