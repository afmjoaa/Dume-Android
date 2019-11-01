package io.dume.dume.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.widget.TextView;

import java.util.Objects;
import io.dume.dume.R;

public class AlertMsgDialogue extends DialogFragment {
    String msg;

    DialogInterface.OnClickListener myListener;
    private String positiveText = "Ok";

    public void setItemChoiceListener(DialogInterface.OnClickListener myListener, String positiveText) {
        this.myListener = myListener;
        this.positiveText = positiveText;
    }

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
                .setPositiveButton(positiveText, myListener)
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
