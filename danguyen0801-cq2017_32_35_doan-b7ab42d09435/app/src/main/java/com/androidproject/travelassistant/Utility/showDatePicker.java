package com.androidproject.travelassistant.Utility;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.androidproject.travelassistant.R;

import java.util.Calendar;

public class showDatePicker {
    private Context context;
    private final com.androidproject.travelassistant.Utility.showDateTimePicker.TaskListener taskListener;

    public interface TaskListener {
        public void onFinished(Calendar calendar);
    }

    public showDatePicker(Context context, com.androidproject.travelassistant.Utility.showDateTimePicker.TaskListener listener){
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
                if(taskListener != null) {
                    // And if it is we call the callback function on it.
                    taskListener.onFinished(date);
                }
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();

        return date;
    }
}
