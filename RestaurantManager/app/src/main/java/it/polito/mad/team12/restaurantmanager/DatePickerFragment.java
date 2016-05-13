package it.polito.mad.team12.restaurantmanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private DatePickerFragmentListener datePickerListener;
    SharedPreferences spDateTime;
    int year;
    int month;
    int day;

    public interface DatePickerFragmentListener {
        void onDateSet(String dateTime);
    }

    public DatePickerFragmentListener getDatePickerListener() {
        return this.datePickerListener;
    }

    public void setDatePickerListener(DatePickerFragmentListener listener) {
        this.datePickerListener = listener;
    }

    protected void notifyDatePickerListener(String dateTime) {
        if(this.datePickerListener != null) {
            this.datePickerListener.onDateSet(dateTime);
        }
    }

    public static DatePickerFragment newInstance(DatePickerFragmentListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setDatePickerListener(listener);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        spDateTime = this.getActivity().getSharedPreferences("ReservationDateTime", Context.MODE_PRIVATE);

        // get the date from SP or the default one
        if (spDateTime.getString("Year", null) != null){
            year = Integer.parseInt(spDateTime.getString("Year", null));
            month = Integer.parseInt(spDateTime.getString("Month", null));
            day = Integer.parseInt(spDateTime.getString("Day", null));
        } else {
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH) + 1; // +1 otherwise it starts from zero
            day = c.get(Calendar.DAY_OF_MONTH);
        }

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        // Save the selected date
        SharedPreferences.Editor editor = spDateTime.edit();
        editor.putString("Year", String.valueOf(year));
        editor.putString("Month", String.valueOf(month));
        editor.putString("Day", String.valueOf(day));
        editor.commit();

/*
        // get the time from SP or the default one
        if (spDateTime.getString("Hour", null) != null){
            hour = Integer.parseInt(spDateTime.getString("Hour", null));
            minute = Integer.parseInt(spDateTime.getString("Minute", null));
        } else {
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }
*/

        String date = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
/*
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute);
        Date date = c.getTime();
*/

        // Here we call the listener and pass the date back to it.
        notifyDatePickerListener(date);
    }
}