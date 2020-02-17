package io.dume.dume.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;


public class SMSAutoVerfication extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
     /*   String action = intent.getAction();
        Log.i(TAG, "Intent recieved: " + action);
        if (PhoneVerificationActivity.isAlive) {
            if (action != null) {
                if (action.equals(SMS_RECEIVED)) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        final SmsMessage[] messages = new SmsMessage[pdus.length];
                        for (int i = 0; i < pdus.length; i++) {
                            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        }
                        if (messages.length > -1) {

                            Log.i(TAG, "Message recieved: " + messages[0]);
                            Intent i = new Intent("got_sms");
                            i.putExtra("sms_body", messages[0].getMessageBody());
                            context.sendBroadcast(i);
                        }
                    }
                }
            }
        }*/
    }
}
