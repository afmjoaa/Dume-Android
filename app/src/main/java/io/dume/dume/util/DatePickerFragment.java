package io.dume.dume.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Objects;

public class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener myListener;
    private int year1 = -1;
    private int month1 = -1;
    private int day1 = -1;

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener myListener) {
        this.myListener = myListener;
    }

    public void setInitialDate(int year, int month, int day) {
        this.year1 = year;
        this.month1 = month;
        this.day1 = day;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(year1 == -1){
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(Objects.requireNonNull(getActivity()), myListener, year, month, day);
        }else {
            return new DatePickerDialog(Objects.requireNonNull(getActivity()), myListener, year1, month1, day1);
        }
    }
}
