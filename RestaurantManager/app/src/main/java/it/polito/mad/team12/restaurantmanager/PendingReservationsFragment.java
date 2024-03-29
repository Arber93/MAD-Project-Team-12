package it.polito.mad.team12.restaurantmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class PendingReservationsFragment extends Fragment {

    Firebase rootRef;
    Firebase pendingRef;
    RecyclerView recyclerView;
    private String restaurantID;
    private String RESERVATION_ID;
    private String SENDER_ID;
    private String ORDERED_ITEMS;
    private String TIME_DATE;
    private String ADDITIONAL_NOTES;
    private String current_sender_id;
    private StringBuffer current_ordered_items;
    private StringBuffer timestamp_long;
    private String current_time_date;
    private String current_notes;


    public PendingReservationsFragment() {
        // Required empty public constructor
    }

    public static PendingReservationsFragment newInstance(String restaurantID) {
        PendingReservationsFragment fragment = new PendingReservationsFragment();
        Bundle args = new Bundle();
        args.putString(Utility.RESTAURANT_ID_KEY, restaurantID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RESERVATION_ID = getString(R.string.reservation_id);
        SENDER_ID = getString(R.string.sender_id);
        ORDERED_ITEMS = getString(R.string.ordered_items);
        TIME_DATE = getString(R.string.time_date_reservation);
        ADDITIONAL_NOTES = getString(R.string.additional_notes);
        //TODO get the restaurantID and use it for the Firebase query
        //restaurantID = getArguments().getString(Utility.RESTAURANT_ID_KEY);
        restaurantID = ((ReservationsActivity)getActivity()).getRestaurantID();
        if(restaurantID == null && savedInstanceState != null) {
            restaurantID = savedInstanceState.getString(Utility.RESTAURANT_ID_KEY, null);
        }
        current_ordered_items = new StringBuffer("");
        timestamp_long = new StringBuffer("");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_reservations, container, false);

        rootRef = new Firebase(Utility.FIREBASE_ROOT);
        Firebase reservationsRef = rootRef.child("reservations");
        Firebase restaurantRef = reservationsRef.child(restaurantID);
        pendingRef = restaurantRef.child("pending");

        // recyclerView setup
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseRecyclerAdapter<Reservation, MyViewHolder> adapter = new
                FirebaseRecyclerAdapter<Reservation, MyViewHolder>(Reservation.class,R.layout.item_reservation, MyViewHolder.class, pendingRef) {
                    @Override
                    protected void populateViewHolder(MyViewHolder myViewHolder, Reservation reservation, int i) {

                        //pass the details to the viewHolder for later use
                        myViewHolder.res = reservation;

                        //timestamp conversion
                        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        timestamp_long.setLength(0);
                        timestamp_long.append(reservation.getDateTime());
                        timestamp_long.append("000");
                        Date date = new Date(Long.parseLong(timestamp_long.toString()));
                        //list of items
                        current_ordered_items.setLength(0);
                        current_ordered_items.append(ORDERED_ITEMS);
                        current_ordered_items.append(" \n");
                        for (Map.Entry entry : reservation.getItems().entrySet()){
                            current_ordered_items.append("- ");
                            current_ordered_items.append(entry.getKey());
                            current_ordered_items.append(" x");
                            current_ordered_items.append(entry.getValue());
                            current_ordered_items.append(" \n");
                        }

                        //explanation + data
                        current_sender_id = SENDER_ID + reservation.getSenderID() + "\n";
                        current_time_date= TIME_DATE + sf.format(date) + "\n";
                        current_notes = ADDITIONAL_NOTES + reservation.getNotes() + "\n";
//                        Log.v("current notes", current_notes);
//                        Log.v("current items", current_ordered_items.toString());
//                        Log.v("current time", current_time_date);
//                        Log.v("current senderID", current_sender_id);
//                        Log.v("current reservationID", current_reservation_id);
                        myViewHolder.tvSenderId.setText(current_sender_id);
                        myViewHolder.tvItems.setText(current_ordered_items);
                        myViewHolder.tvTimeDate.setText(current_time_date);
                        myViewHolder.tvNotes.setText(current_notes);
                    }
                };
        recyclerView.setAdapter(adapter);

        return view;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        AlertDialog.Builder builder;

        TextView tvSenderId;
        TextView tvItems;
        TextView tvTimeDate;
        TextView tvNotes;
        Button b_accept;
        Button b_deny;
        Button b_complete;
        Firebase rootRef = new Firebase(Utility.FIREBASE_ROOT);
        Firebase reservationsRef = rootRef.child("reservations");
        Firebase pendingRef = reservationsRef.child("pending");
        Firebase acceptedRef = reservationsRef.child("accepted");
        Firebase deniedRef = reservationsRef.child("denied");
        Reservation res;

        public MyViewHolder(View v){
            super(v);
            tvSenderId = (TextView)v.findViewById(R.id.senderId);
            tvItems = (TextView)v.findViewById(R.id.items);
            tvItems = (TextView)v.findViewById(R.id.items);
            tvTimeDate = (TextView)v.findViewById(R.id.timeDate);
            tvNotes = (TextView)v.findViewById(R.id.notes);
            b_accept = (Button)v.findViewById(R.id.b_accept);
            b_deny = (Button)v.findViewById(R.id.b_deny);
            b_complete= (Button)v.findViewById(R.id.b_complete);
            b_complete.setVisibility(View.GONE);
            b_accept.setOnClickListener(this);
            b_deny.setOnClickListener(this);
            builder = new AlertDialog.Builder(tvItems.getContext());
        }


        @Override
        public void onClick(View v) {
//            Log.v("reservation---->", res.getItems().toString());

            if (v.getId() == b_accept.getId()){
                builder.setMessage(R.string.dialog_message_accept).setTitle(R.string.dialog_title_accept);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button

                        //remove the reservation from pending
                        pendingRef.child(res.getReservationID()).setValue(null);

                        //move this pending reservation to accepted
                        Firebase newRef = acceptedRef.push();
                        String reservationId = newRef.getKey();
                        res.setReservationID(reservationId);
                        newRef.setValue(res);

                    }
                });
                builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                // b_denied has been clicked
                builder.setMessage(R.string.dialog_message_deny).setTitle(R.string.dialog_title_deny);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button

                        //remove the reservation from pending
                        pendingRef.child(res.getReservationID()).setValue(null);

                        //move this pending reservation to denied
                        Firebase newRef = deniedRef.push();
                        String reservationId = newRef.getKey();
                        res.setReservationID(reservationId);
                        newRef.setValue(res);
                    }
                });
                builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Utility.RESTAURANT_ID_KEY, restaurantID);
    }


}

