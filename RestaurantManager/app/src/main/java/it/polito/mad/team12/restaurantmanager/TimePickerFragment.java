package it.polito.mad.team12.restaurantmanager;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;


public  class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private TimePickerFragmentListener timePickerListener;
    SharedPreferences spDateTime;
    int year;
    int month;
    int day;
    int hour;
    int minute;

    public interface TimePickerFragmentListener {
        void onTimeSet(String dateTime);
    }

    public TimePickerFragmentListener getTimePickerListener() {
        return this.timePickerListener;
    }

    public void setTimePickerListener(TimePickerFragmentListener listener) {
        this.timePickerListener = listener;
    }

    protected void notifyTimePickerListener(String dateTime) {
        if(this.timePickerListener != null) {
            this.timePickerListener.onTimeSet(dateTime);
        }
    }

    public static TimePickerFragment newInstance(TimePickerFragmentListener listener) {
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setTimePickerListener(listener);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        spDateTime = this.getActivity().getSharedPreferences("ReservationTime", Context.MODE_PRIVATE);

        if (spDateTime.getString("Hour", null) != null){
            hour = Integer.parseInt(spDateTime.getString("Hour", null));
            minute = Integer.parseInt(spDateTime.getString("Minute", null));
        } else {
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hour, int minute) {

        // Save the selected time
        SharedPreferences.Editor editor = spDateTime.edit();
        editor.putString("Hour", String.valueOf(hour));
        editor.putString("Minute", String.valueOf(minute));
        editor.commit();
        // double digit minutes
        String minutes = String.valueOf(minute);
        if(minute < 10){
            minutes = "0" + minute;
        }

        String time = String.valueOf(hour) + ":" + minutes;
/*
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute);
        Date date = c.getTime();
*/

        // Here we call the listener and pass the date back to it.
        notifyTimePickerListener(time);

    }
}