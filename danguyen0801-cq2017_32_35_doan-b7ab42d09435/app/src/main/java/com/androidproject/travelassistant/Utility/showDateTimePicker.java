package com.androidproject.travelassistant.Utility;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.androidproject.travelassistant.R;

import java.util.Calendar;

public class showDateTimePicker {
    private Context context;
    private final TaskListener taskListener;

    public interface TaskListener {
        public void onFinished(Calendar calendar);
    }

    public showDateTimePicker(Context context, TaskListener listener){
        super();
        this.context = context;
        this.taskListener = listener;
    }

    public Calendar run() {
        final Calendar date;
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(context, R.style.PickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);

                new TimePickerDialog(context, R.style.PickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        if(taskListener != null) {
                            // And if it is we call the callback function on it.
                            taskListener.onFinished(date);
                        }
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();

        return date;
    }
}
