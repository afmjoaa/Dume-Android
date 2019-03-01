package io.dume.dume.common.chatActivity;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Calendar;

import io.dume.dume.R;
import io.dume.dume.common.chatActivity.Used_Classes.Message;

class CustomOutcomingMessageViewHolder extends MessagesListAdapter.OutcomingMessageViewHolder<Message>  {
    private static final String TAG = "CustomOutcomingMessageV";
    private final TextView timeTV;

    public CustomOutcomingMessageViewHolder(View itemView) {
        super(itemView);
        timeTV = itemView.findViewById(R.id.messageTime);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
        //time.setText(message.getStatus() + " " + time.getText());
        timeTV.setText(timeTV.getText());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(message.getCreatedAt());
        if (DateFormat.is24HourFormat(itemView.getContext())) {
            timeTV.setText(timeTV.getText());
            //calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        } else {
            final int intHour = calendar.get(Calendar.HOUR);
            final int intMinute = calendar.get(Calendar.MINUTE);
            final int intAMPM = calendar.get(Calendar.AM_PM);
            String AM_PM;
            if (intAMPM == 0) {
                AM_PM = "AM";
            } else {
                AM_PM = "PM";
            }
            timeTV.setText(String.format("%d:%d %s", intHour, intMinute, AM_PM));
        }
    }
}
