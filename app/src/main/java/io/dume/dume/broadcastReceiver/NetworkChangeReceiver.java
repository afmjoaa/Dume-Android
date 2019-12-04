package io.dume.dume.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.MyApplication;
import io.dume.dume.util.NetworkUtil;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkChangeReceiver";
    MyConnectivityHandler myConnectivityHandler;

    public void setNetworkListener(MyConnectivityHandler myConnectivityHandler) {
        this.myConnectivityHandler = myConnectivityHandler;
    }


    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.e(TAG, "onReceive: " + "Yap Broadcasat Called");
        try {
            boolean isVisible = MyApplication.isActivityVisible();// Check if
            Log.i("Activity is Visible ", "Is activity visible : " + isVisible);

            // If it is visible then trigger the task else do nothing
            if (isVisible) {

                //Handling the network connectivity
                if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                    int status = NetworkUtil.getConnectivityStatusString(context);
//                    Log.e(TAG, "Sulod sa network reciever");

                    if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                        myConnectivityHandler.pause();
                        //Toast.makeText(context, "Pause", Toast.LENGTH_SHORT).show();
                    } else {
                        myConnectivityHandler.resume();

                        new Thread(() -> {
                            try {
                                Socket socket = new Socket();
                                socket.connect(new InetSocketAddress("8.8.8.8", 53), 2500);

                                // socket.connect(new InetSocketAddress("114.114.114.114", 53), 3000);
                                myConnectivityHandler.resume();

                            } catch (IOException e) {
                                e.printStackTrace();
                                myConnectivityHandler.pause();

                            }
                        }).start();


                        //Toast.makeText(context, "Resume", Toast.LENGTH_SHORT).show();
                    }
                }

               /* //Handling the Gps connectivity
                if ("android.location.PROVIDERS_CHANGED".equals(intent.getAction())) {
                    Log.e(TAG, "Sulod sa GPS reciever");
                    boolean gpsEnabled = DumeUtils.isGpsEnabled(context);

                    if (gpsEnabled) {
//                        myGpsHandlerOne.onGpsResume();
                        Toast.makeText(context, "Gps on", Toast.LENGTH_SHORT).show();
                    } else {
//                        myGpsHandlerOne.onGpsPause();
                        Toast.makeText(context, "Gps off", Toast.LENGTH_SHORT).show();
                    }

                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            /*if ("android.location.PROVIDERS_CHANGED".equals(intent.getAction())) {
                myGpsHandlerOne.onGpsError(e);
            }*/
            if (myConnectivityHandler != null) {
                myConnectivityHandler.onError(e);
            }


        }

    }
}