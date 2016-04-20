package it.polito.mad.team12.restaurantmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    public static final int BTN_ACCEPT_REQUEST = 111;
    public static final int BTN_DENY_REQUEST = 000;
    private Context context;
    int currentFragmentInt;
    int pendingFragmentInt = R.layout.fragment_pending_reservations;
    int acceptedFragmentInt = R.layout.fragment_accepted_reservations;
    int deniedFragmentInt = R.layout.fragment_denied_reservations;
    SharedPreferences sharedPreferences;
    String jsonPending;
    String jsonAccepted;
    String jsonDenied;
    String current_id;
    String current_name;
    String current_number;
    String current_items;
    String current_td;
    String current_notes;


    private ArrayList<ReservationHeader> reservationHeaders;

    public ExpandableListAdapter(Context context, ArrayList<ReservationHeader> reservationHeaders, int currentFragmentInt) {
        this.context = context;
        this.currentFragmentInt = currentFragmentInt;
        this.reservationHeaders = reservationHeaders;
        this.sharedPreferences = context.getSharedPreferences("JSONReservations", context.MODE_PRIVATE);
        this.jsonPending = sharedPreferences.getString("pending", null);
        this.jsonAccepted = sharedPreferences.getString("accepted", null);
        this.jsonDenied= sharedPreferences.getString("denied", null);

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ReservationDetails> chList = reservationHeaders.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ReservationDetails child = (ReservationDetails) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.list_reservation_details, null);
        }

        TextView tv1 = (TextView) convertView.findViewById(R.id.customer_name);
        TextView tv2 = (TextView) convertView.findViewById(R.id.customer_phone_number);
        TextView tv3 = (TextView) convertView.findViewById(R.id.ordered_items);
        TextView tv4 = (TextView) convertView.findViewById(R.id.time_date);
        TextView tv5 = (TextView) convertView.findViewById(R.id.notes);
        tv1.setText(child.getCustomerName());
        tv2.setText(child.getCustomerPhoneNumber());
        tv3.setText(child.getOrderedItems());
        tv4.setText(child.getTimeDate());
        tv5.setText(child.getNotes());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ReservationDetails> chList = reservationHeaders.get(groupPosition).getItems();
        return chList.size();
    }

    @Override
    public ReservationHeader getGroup(int groupPosition) {
        return reservationHeaders.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return reservationHeaders.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ReservationHeader header = (ReservationHeader) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.list_reservation_header, null);
        }

        TextView tvHeader = (TextView) convertView.findViewById(R.id.header);
        Button btn_deny = (Button) convertView.findViewById(R.id.btn_deny);
        Button btn_accept = (Button) convertView.findViewById(R.id.btn_accept);
        if (currentFragmentInt == acceptedFragmentInt){
            btn_accept.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)btn_deny.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            btn_deny.setLayoutParams(params);
        }
        if (currentFragmentInt == deniedFragmentInt){
            btn_deny.setVisibility(View.GONE);
        }

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFragmentInt == pendingFragmentInt){
                    getAndRemove(groupPosition, currentFragmentInt, jsonPending); // it also updates sharedPreferences
                    putReservation(BTN_ACCEPT_REQUEST); // it also updates sharedPreferences
                }
                if (currentFragmentInt == deniedFragmentInt){
                    getAndRemove(groupPosition, currentFragmentInt, jsonDenied); // it also updates sharedPreferences
                    putReservation(BTN_ACCEPT_REQUEST); // it also updates sharedPreferences
                }

                //remove header from list and add it to the accepted headers
                reservationHeaders.remove(getGroup(groupPosition));
                notifyDataSetChanged();
            }
        });


        btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFragmentInt == pendingFragmentInt){
                    getAndRemove(groupPosition, currentFragmentInt, jsonPending); // it also updates sharedPreferences
                    putReservation(BTN_DENY_REQUEST); // it also updates sharedPreferences
                }
                if (currentFragmentInt == acceptedFragmentInt){
                    getAndRemove(groupPosition, currentFragmentInt, jsonAccepted); // it also updates sharedPreferences
                    putReservation(BTN_DENY_REQUEST); // it also updates sharedPreferences
                }

                //remove header from list and add it to the accepted headers
                reservationHeaders.remove(getGroup(groupPosition));
                notifyDataSetChanged();
            }
        });

        btn_deny.setFocusable(false);
        btn_accept.setFocusable(false);
        tvHeader.setText(header.getHeader());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void getAndRemove(int i, int fragmentInt, String jsonString){
        // jsonString has all the reservations and their details, the i-th of them must be removed

        JSONArray oldArray;
        JSONObject oldRoot;

        // get and save the details of the reservation to move for later use
        try {
            oldRoot = new JSONObject(jsonString); // from String to JSONObject
            oldArray= oldRoot.getJSONArray("Reservations"); //from JSONObject to JSONArray
            JSONObject reservation = oldArray.getJSONObject(i); //get the single reservation
            current_id = reservation.getString("reservation_id");
            current_name = reservation.getString("customer_name");
            current_number = reservation.getString("customer_phone_number");
            current_td = reservation.getString("time_date");
            current_notes = reservation.getString("additional_notes");
            current_items = reservation.getString("ordered_items");

        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // remove the i-th reservation
        JSONArray newArray = new JSONArray();
        JSONObject newRoot = new JSONObject();

        if (oldArray!= null) {    // why is it always true?
            int len = oldArray.length();
            for (int j=0; j<len; j++){
                //Excluding the item at position i
                if (j != i)
                {
                    try {
                        newArray.put(oldArray.get(j));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        // put the new list of reservations in a new JSONObject
        try {
            newRoot.put("Reservations", newArray);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // update sharedPreferences
        // convert this new JSONObject in a string and save it in the sharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (fragmentInt == pendingFragmentInt){
            jsonPending = newRoot.toString();
            editor.putString("pending", jsonPending);
        }
        if (fragmentInt == acceptedFragmentInt){
            jsonAccepted = newRoot.toString();
            editor.putString("accepted", jsonAccepted);
        }
        if (fragmentInt == deniedFragmentInt){
            jsonDenied = newRoot.toString();
            editor.putString("denied", jsonDenied);
        }
        editor.commit();
    }

    // call this method only after getAndRemove() in order to have updated values
    private void putReservation(int btn_request) {
        // create a new JSONObject for the reservation that has to be moved
        JSONObject newReservation = new JSONObject();
        try {
            newReservation.put("reservation_id", current_id); // current_something gets the value in getAndRemove
            newReservation.put("customer_name", current_name);
            newReservation.put("customer_phone_number", current_number);
            newReservation.put("time_date", current_td);
            newReservation.put("additional_notes", current_notes);
            newReservation.put("ordered_items", current_items);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // put this new JSONObject in a new JSONArray of reservations
        JSONArray newArray= new JSONArray();
        newArray.put(newReservation);

        // now, add all the other reservations (accepted or denied), so that the new reservation is
        // at the top of the list
        JSONObject oldRoot;
        JSONArray oldArray;
        try {

            if(btn_request == BTN_ACCEPT_REQUEST){
                // add all the accepted reservations
                oldRoot = new JSONObject(jsonAccepted); // from String to JSONObject
                oldArray = oldRoot.getJSONArray("Reservations"); //from JSONObject to JSONArray
            } else {
                // add all the denied reservations
                oldRoot = new JSONObject(jsonDenied); // from String to JSONObject
                oldArray = oldRoot.getJSONArray("Reservations"); //from JSONObject to JSONArray
            }

            if (oldArray != null) {
                int len = oldArray.length();
                for (int j=0; j<len; j++) {
                    JSONObject oldReservation = oldArray.getJSONObject(j); //get the single reservation
                    newArray.put(oldReservation);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // put the new list of accepted or denied reservations in a new JSONObject
        JSONObject newRoot = new JSONObject();
        try {
            newRoot.put("Reservations", newArray);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // convert this new JSONObject in a string and save it in the sharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (btn_request == BTN_DENY_REQUEST){
            jsonDenied = newRoot.toString();
            editor.putString("denied", jsonDenied);
        } else {
            jsonAccepted = newRoot.toString();
            editor.putString("accepted", jsonAccepted);
        }
        editor.commit();

    }


}


