package com.example.volunteerapp.Activity.Project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

public class DateTimeHelper {

    public interface DateTimeListener {
        void onDateTimeSet(Calendar selectedDateTime);
    }

    public static void showDateTimePicker(Context context, final Calendar calendar, final DateTimeListener listener) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                showTimePicker(context, calendar, listener);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private static void showTimePicker(Context context, final Calendar calendar, final DateTimeListener listener) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                if (listener != null) {
                    listener.onDateTimeSet(calendar);
                }
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }
}
