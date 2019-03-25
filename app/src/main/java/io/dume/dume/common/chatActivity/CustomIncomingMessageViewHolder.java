package io.dume.dume.common.chatActivity;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Calendar;

import carbon.widget.ImageView;
import io.dume.dume.R;
import io.dume.dume.common.chatActivity.Used_Classes.Message;

class CustomIncomingMessageViewHolder extends MessagesListAdapter.IncomingMessageViewHolder <Message> {

    private static final String TAG = "CustomIncomingMessageVi";
    private final TextView timeTV;
    private final TextView messageText;
    private final ImageView messengerDp;

    public CustomIncomingMessageViewHolder(View itemView) {
        super(itemView);
        timeTV = itemView.findViewById(R.id.messageTime);
        messageText = itemView.findViewById(R.id.messageText);
        messengerDp = itemView.findViewById(R.id.messageUserAvatar);

    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
        //Log.e(TAG, "onBind: " + message.getStatus()  + message.getText() + message.getImageUrl());

        Glide.with(itemView.getContext()).load(message.getUser().avatar).apply(new RequestOptions().override(100, 100).placeholder(R.drawable.avatar_female)).into(messengerDp);

        messageText.setText(message.getText());
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
