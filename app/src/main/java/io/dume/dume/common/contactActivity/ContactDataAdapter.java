package io.dume.dume.common.contactActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import carbon.widget.ImageView;
import io.dume.dume.R;

public class ContactDataAdapter extends RecyclerView.Adapter<ContactDataAdapter.MyViewHolder> {

    private static final String TAG = "ContactDataAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<ContactData> data;
    private int[] statusIcon = {
            R.drawable.ic_task_pending,
            R.drawable.ic_task_accepted,
            R.drawable.ic_task_current
    };
    private String[] statusText;

    public ContactDataAdapter(Context context, List<ContactData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        statusText = context.getResources().getStringArray(R.array.statusText);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_contact_row, parent, false);
        ContactDataAdapter.MyViewHolder holder = new ContactDataAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        switch (position){
            case 1:
                //pending test
                holder.statusIndicatorImage.setImageResource(statusIcon[0]);
                holder.statusText.setText(statusText[0]);
                holder.hostRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.status_viewed));
                holder.hostRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Request not accepted yet", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 2:
                //accepted test
                holder.statusIndicatorImage.setImageResource(statusIcon[1]);
                holder.statusText.setText(statusText[1]);
                break;
            case 5:
                //current test
                holder.statusIndicatorImage.setImageResource(statusIcon[2]);
                holder.statusText.setText(statusText[2]);
                break;
            case 9:
                //testing selected item
                holder.contactUserDP.setHeight((int) (44*context.getResources().getDisplayMetrics().density));
                holder.contactUserDP.setWidth((int) (44*context.getResources().getDisplayMetrics().density));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView contactUserDP;
        private final ImageView statusIndicatorImage;
        private final TextView contactUserName;
        private final TextView statusText;
        private final RelativeLayout hostRelativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            hostRelativeLayout = itemView.findViewById(R.id.hostRelativeLayout);
            contactUserDP = itemView.findViewById(R.id.chat_user_display_pic);
            statusIndicatorImage = itemView.findViewById(R.id.status_indicator_image);
            contactUserName = itemView.findViewById(R.id.chat_user_name);
            statusText = itemView.findViewById(R.id.status_text);

        }
    }
}
