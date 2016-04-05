package it.polito.mad.team12.restaurantmanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class OffersFragment extends Fragment {
    /*
     *    This fragment will allow the publication of offers based on a dish from the menu and
     *  additional information such as discount, validity period and who can use it (e.g. just students).
     */

    public OffersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offers, container, false);
    }

}
