package io.dume.dume.util;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    TimePickerDialog.OnTimeSetListener myListener;

    public void setTimePickerListener(TimePickerDialog.OnTimeSetListener myListener) {
        this.myListener = myListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minite = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), myListener, hour, minite, DateFormat.is24HourFormat(getActivity()));
    }
}
