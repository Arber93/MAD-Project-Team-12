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

public class PendingReservationsFragment extends Fragment {

    public static final String JSON_RESERVATIONS = "JSONReservations";
    private String RESERVATION_ID;
    private String CUSTOMER_NAME;
    private String ORDERED_ITEMS;
    private String TIME_DATE;
    private String ADDITIONAL_NOTES;
    private String jsonPending;
    private String jsonAccepted;
    private String jsonDenied;

    private SharedPreferences sharedPreferences;

    ExpandableListAdapter expAdapter;
    ExpandableListView expandList;
    private ArrayList<ReservationHeader> expListItems;

    public PendingReservationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = this.getActivity().getSharedPreferences(JSON_RESERVATIONS, Context.MODE_PRIVATE);
        jsonPending = sharedPreferences.getString("pending", null);
        jsonAccepted = sharedPreferences.getString("accepted", null);
        jsonDenied = sharedPreferences.getString("denied", null);

        if (jsonPending == null){
            jsonPending = "{\"Reservations\":[{\"reservation_id\":\"1\",\"customer_name\":\"Mario Rossi\", \"customer_phone_number\":\"333 1111111\", \"ordered_items\":\"margherita 2x, coca cola 1x\", \"time_date\":\"1460322117\", \"additional_notes\":\"niente\"},{\"reservation_id\":\"4\",\"customer_name\":\"Mario Neri\", \"customer_phone_number\":\"333 2222222\", \"ordered_items\":\"margherita 2x, coca cola 1x\", \"time_date\":\"1460322117\", \"additional_notes\":\"niente\"},{\"reservation_id\":\"7\",\"customer_name\":\"Mario Bianchi\", \"customer_phone_number\":\"333 3333333\", \"ordered_items\":\"margherita 2x, coca cola 1x\", \"time_date\":\"1460322117\", \"additional_notes\":\"niente\"},{\"reservation_id\":\"10\",\"customer_name\":\"Mario Verdi\", \"customer_phone_number\":\"333 4444444\", \"ordered_items\":\"margherita 2x, coca cola 1x\", \"time_date\":\"1460322117\", \"additional_notes\":\"niente\"}]}";
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("pending", jsonPending);
            editor.commit();
        }
        if (jsonAccepted == null){
            jsonAccepted = "{\"Reservations\":[{\"reservation_id\":\"2\",\"customer_name\":\"Marco Rossi\", \"customer_phone_number\":\"333 1111111\", \"ordered_items\":\"margherita 2x, coca cola 1x\", \"time_date\":\"1460322117\", \"additional_notes\":\"niente\"},{\"reservation_id\":\"5\",\"customer_name\":\"Mario Neri\", \"customer_phone_number\":\"333 2222222\", \"ordered_items\":\"margherita 2x, coca cola 1x\", \"time_date\":\"1460322117\", \"additional_notes\":\"niente\"},{\"reservation_id\":\"8\",\"customer_name\":\"Mario Bianchi\", \"customer_phone_number\":\"333 3333333\", \"ordered_items\":\"margherita 2x, coca cola 1x\", \"time_date\":\"1460322117\", \"additional_notes\":\"niente\"},{\"reservation_id\":\"11\",\"customer_name\":\"Mario Verdi\", \"customer_phone_number\":\"333 4444444\", \"ordered_items\":\"margherita 2x, coca cola 1x\", \"time_date\":\"1460322117\", \"additional_notes\":\"niente\"}]}";
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("accepted", jsonAccepted);
            editor.commit();
        }
        if (jsonDenied == null){
            jsonDenied = "{\"Reservations\":[{\"reservation_id\":\"3\",\"customer_name\":\"Matteo Rossi\", \"customer_phone_number\":\"333 1111111\", \"ordered_items\":\"margherita 2x, coca cola 1x\", \"time_date\":\"1460322117\", \"additional_notes\":\"niente\"},{\"reservation_id\":\"6\",\"customer_name\":\"Mario Neri\", \"customer_phone_number\":\"333 2222222\", \"ordered_items\":\"margherita 2x, coca cola 1x\", \"time_date\":\"1460322117\", \"additional_notes\":\"niente\"},{\"reservation_id\":\"9\",\"customer_name\":\"Mario Bianchi\", \"customer_phone_number\":\"333 3333333\", \"ordered_items\":\"margherita 2x, coca cola 1x\", \"time_date\":\"1460322117\", \"additional_notes\":\"niente\"},{\"reservation_id\":\"12\",\"customer_name\":\"Mario Verdi\", \"customer_phone_number\":\"333 4444444\", \"ordered_items\":\"margherita 2x, coca cola 1x\", \"time_date\":\"1460322117\", \"additional_notes\":\"niente\"}]}";
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("denied", jsonDenied);
            editor.commit();
        }

        RESERVATION_ID = getString(R.string.reservation_id);
        CUSTOMER_NAME = getString(R.string.customer_name);
        ORDERED_ITEMS = getString(R.string.ordered_items);
        TIME_DATE = getString(R.string.time_date_reservation);
        ADDITIONAL_NOTES = getString(R.string.additional_notes);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_reservations, container, false);

        // get the listview
        expandList = (ExpandableListView) view.findViewById(R.id.lvExp);
        expListItems = setListData();
        expAdapter = new ExpandableListAdapter(getActivity(), expListItems, R.layout.fragment_pending_reservations);
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
        jsonPending = sharedPreferences.getString("pending", null);
        Log.d("ADebugTag", "*****************Pending***************: " + jsonPending);
        ArrayList<ReservationHeader> list = new ArrayList<ReservationHeader>();
        ArrayList<ReservationDetails> ch_list;
        JSONArray reservationsArray = null;

        try {
            JSONObject root = new JSONObject(jsonPending); //jsonPending is a string
            reservationsArray = root.getJSONArray("Reservations");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < reservationsArray.length(); i++) {
            String current_id = null;
            String current_name = null;
            String current_items = null;
            String current_td= null;
            String current_notes = null;
            try {
                JSONObject reservation = reservationsArray.getJSONObject(i);
                current_id = RESERVATION_ID + reservation.getString("reservation_id");
                current_name = CUSTOMER_NAME + reservation.getString("customer_name");
                current_td = TIME_DATE + reservation.getString("time_date");
                current_notes = ADDITIONAL_NOTES + reservation.getString("additional_notes");
                current_items = ORDERED_ITEMS + reservation.getString("ordered_items");

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

