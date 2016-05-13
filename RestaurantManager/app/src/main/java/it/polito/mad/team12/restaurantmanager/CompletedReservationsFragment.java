package it.polito.mad.team12.restaurantmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CompletedReservationsFragment extends Fragment {

    public static final String JSON_RESERVATIONS = "JSONReservations";
    private String RESERVATION_ID;
    private String CUSTOMER_NAME;
    private String ORDERED_ITEM;
    private String TIME_DATE;
    private String ADDITIONAL_NOTES;
    private String jsonCompleted;

    private SharedPreferences sharedPreferences;

    ExpandableListAdapter expAdapter;
    ExpandableListView expandList;
    private ArrayList<ReservationHeader> expListItems;

    public CompletedReservationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = this.getActivity().getSharedPreferences(JSON_RESERVATIONS, Context.MODE_PRIVATE);

        if (jsonCompleted == null){
            jsonCompleted = "{\"Reservations\":[{\"reservation_id\":\"0\",\"customer_name\":\"Mat Ros\", \"customer_phone_number\":\"333 1111111\", \"ordered_items\":\"margherita 2x, coca cola 1x\", \"time_date\":\"1460322117\", \"additional_notes\":\"niente\"},{\"reservation_id\":\"00\",\"customer_name\":\"Mario Neri\", \"customer_phone_number\":\"333 2222222\", \"ordered_items\":\"margherita 2x, coca cola 1x\", \"time_date\":\"1460322117\", \"additional_notes\":\"niente\"},{\"reservation_id\":\"000\",\"customer_name\":\"Mario Bianchi\", \"customer_phone_number\":\"333 3333333\", \"ordered_items\":\"margherita 2x, coca cola 1x\", \"time_date\":\"1460322117\", \"additional_notes\":\"niente\"},{\"reservation_id\":\"0000\",\"customer_name\":\"Mario Verdi\", \"customer_phone_number\":\"333 4444444\", \"ordered_items\":\"margherita 2x, coca cola 1x\", \"time_date\":\"1460322117\", \"additional_notes\":\"niente\"}]}";
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("completed", jsonCompleted);
            editor.commit();
        }

        RESERVATION_ID = getString(R.string.reservation_id);
        CUSTOMER_NAME = getString(R.string.customer_name);
        ORDERED_ITEM = getString(R.string.ordered_items);
        TIME_DATE = getString(R.string.time_date_reservation);
        ADDITIONAL_NOTES = getString(R.string.additional_notes);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed_reservations, container, false);

        // get the listview
        expandList = (ExpandableListView) view.findViewById(R.id.lvExp);
        expListItems = setListData();
        expAdapter = new ExpandableListAdapter(getActivity(), expListItems, R.layout.fragment_completed_reservations);
        expandList.setAdapter(expAdapter);

        // Listview Group click listener
        expandList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expandList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        // Listview Group collapsed listener
        expandList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        // Listview on child click listener
        expandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });

        return view;
    }

    public ArrayList<ReservationHeader> setListData() {

        // get the updated list
        jsonCompleted = sharedPreferences.getString("completed", null);
        Log.d("ADebugTag", "*****************Completed***************: " + jsonCompleted);

        ArrayList<ReservationHeader> list = new ArrayList<ReservationHeader>();
        ArrayList<ReservationDetails> ch_list;
        JSONArray reservations = null;

        try {
            JSONObject root = new JSONObject(jsonCompleted);
            reservations = root.getJSONArray("Reservations");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < reservations.length(); i++) {
            String current_id = null;
            String current_name = null;
            String current_items = null;
            String current_td= null;
            String current_notes = null;
            try {
                JSONObject reservation = reservations.getJSONObject(i);
                current_id = RESERVATION_ID + reservation.getString("reservation_id");
                current_name = CUSTOMER_NAME + reservation.getString("customer_name");
                current_items = ORDERED_ITEM + reservation.getString("ordered_items");
                current_td = TIME_DATE + reservation.getString("time_date");
                current_notes = ADDITIONAL_NOTES + reservation.getString("additional_notes");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ReservationHeader h = new ReservationHeader();
            h.setHeader(current_id);

            ch_list = new ArrayList<ReservationDetails>();

            ReservationDetails d = new ReservationDetails();
            d.setCustomerName(current_name);
            d.setOrderedItems(current_items);
            d.setTimeDate(current_td);
            d.setNotes(current_notes);

            ch_list.add(d);

            h.setDetails(ch_list);
            list.add(h);

        }

        return list;
    }
}

