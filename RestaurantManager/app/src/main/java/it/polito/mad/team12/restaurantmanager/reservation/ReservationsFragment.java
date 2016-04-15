package it.polito.mad.team12.restaurantmanager.reservation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.polito.mad.team12.restaurantmanager.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReservationsFragment extends Fragment {
    /*
     *   This fragment will show a list of reservations under three different tabs Pending,
     * Accepted and History. It might need other fragments to implement the different tabs.
     */
    public ReservationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservations, container, false);
    }

}
