package it.polito.mad.team12.restaurantmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by Pal on 08-May-16.
 */
public class ConfirmReservationActivity extends AppCompatActivity implements TimePickerFragment.TimePickerFragmentListener {
    public static final String CART_ITEMS = "CartItems";
    SharedPreferences spItems;
    SharedPreferences spTime;
    int year, month, day, hour, minute;
    Map<String,String> mapItems;
    String restaurantID;
    String date;
    String time;
    ListView listView;
    TextView tvRestaurantId;
    TextView tvDate;
    TextView tvTime;
    String dateText;
    String timeText;
    EditText etNotes;
    Firebase rootRef;
    Firebase pendingRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_reservation);

        // get the date of today
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        // get the time from SP or the default one
        spTime = getSharedPreferences("ReservationTime", Context.MODE_PRIVATE);
        if (spTime.getString("Hour", null) != null){
            hour = Integer.parseInt(spTime.getString("Hour", null));
            minute = Integer.parseInt(spTime.getString("Minute", null));
        } else {
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }

        // double digit minutes fot the TextView
        String minutes = String.valueOf(minute);
        if(minute < 10){
            minutes = "0" + minute;
        }
        date = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
        time = String.valueOf(hour) + ":" + minutes;

        Bundle b = getIntent().getExtras();
        restaurantID = b.getString("restaurantID");

        tvRestaurantId = (TextView) findViewById(R.id.restaurantID);
        listView = (ListView) findViewById(R.id.listView);
        etNotes = (EditText) findViewById(R.id.notes);
        tvDate = (TextView) findViewById(R.id.date);
        tvTime = (TextView) findViewById(R.id.time);

        tvRestaurantId.setText(restaurantID);
        dateText = getString(R.string.date) + date;
        timeText = getString(R.string.time) + time;
        tvDate.setText(dateText);
        tvTime.setText(timeText);
        spItems = getSharedPreferences(CART_ITEMS, Context.MODE_PRIVATE);
        mapItems = (Map<String,String>) spItems.getAll();

        SimpleItemAdapter adapter = new SimpleItemAdapter(mapItems, this);
        listView.setAdapter(adapter);

        rootRef = new Firebase(Utility.FIREBASE_ROOT);
        Firebase reservationsRef = rootRef.child("reservations");
        Firebase restaurantRef = reservationsRef.child(restaurantID);
        pendingRef = restaurantRef.child("pending");
    }

    public void sendReservation(View v){
        // get the updated SP
        spItems = getSharedPreferences(CART_ITEMS, Context.MODE_PRIVATE);
        mapItems = (Map<String,String>) spItems.getAll();

        // at least one item must be selected
        if (!mapItems.isEmpty()){
            String notes = etNotes.getText().toString();
            //TODO senderID, receiverID
            String senderID = "senderID1";
            String receiverID = "receiverID1";

            // get the Reservation's timestamp
            String str_date = year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + "00";
            DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date d = null;
            try {
                d = formatter.parse(str_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long output = d.getTime()/1000L;
            String str = Long.toString(output);
            long longTsReservation = Long.parseLong(str);
            Long longTsCurrent= System.currentTimeMillis()/1000;

            // check if there is time to accept the reservation
            int acceptTime = 60;
            Log.v("Reservation TS", String.valueOf(longTsReservation));
            Log.v("Current TS", String.valueOf(longTsCurrent));

            if (longTsReservation > (longTsCurrent + acceptTime)){
                String stringTsReservation = String.valueOf(longTsReservation);
                //reservationId generation
                Firebase newPendingRef = pendingRef.push();
                String reservationId = newPendingRef.getKey();

                Reservation reservation = new Reservation(reservationId, mapItems, stringTsReservation, notes, senderID, receiverID);
                newPendingRef.setValue(reservation);
                Toast.makeText(this, getResources().getString(R.string.reservation_sent), Toast.LENGTH_SHORT).show();

                //clear the Reservation's data
                SharedPreferences.Editor editorItems = spItems.edit();
                editorItems.clear();
                editorItems.commit();
                SharedPreferences.Editor editorDateTime= spTime.edit();
                editorDateTime.clear();
                editorDateTime.commit();

                //back to the previous activity
                finish();
            } else {
                Toast.makeText(this, getResources().getString(R.string.too_early), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.empty_reservation), Toast.LENGTH_LONG).show();
        }
    }

    public void selectTime(View v){
        TimePickerFragment fragment = TimePickerFragment.newInstance(this);
        fragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(String time) {
        //update Textview
        timeText = getString(R.string.time) + time;
        tvTime.setText(timeText);
        //update SP
        hour = Integer.parseInt(spTime.getString("Hour", null));
        minute = Integer.parseInt(spTime.getString("Minute", null));
    }
}
