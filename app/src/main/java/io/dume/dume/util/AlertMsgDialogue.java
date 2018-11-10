package io.dume.dume.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import java.util.Objects;
import io.dume.dume.R;

public class AlertMsgDialogue extends DialogFragment {
    String msg;

    DialogInterface.OnClickListener myListener;

    public void setItemChoiceListener(DialogInterface.OnClickListener myListener) {
        this.myListener = myListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msg = Objects.requireNonNull(getArguments()).getString("msg");

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.RadioDialogTheme);
        builder.setMessage(msg)
                .setPositiveButton("Ok", myListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Typeface font = Typeface.createFromAsset(Objects.requireNonNull(getContext()).getAssets(), "fonts/Cairo_Regular.ttf");
        TextView messageView = alertDialog.findViewById(android.R.id.message);
        if (messageView != null) messageView.setTypeface(font);
        return alertDialog;
    }
}
