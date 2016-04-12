package it.polito.mad.team12.restaurantmanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    /*
     *    This fragment will contain information on the restaurant (such as location, timetables,
     *  photos, etc.). The manager must be able to edit this data through the use of dialog boxes.
     */

    public DetailsFragment() {
        // Required empty public constructor
    }


    //INFLATE fragment_details layout!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }


}
