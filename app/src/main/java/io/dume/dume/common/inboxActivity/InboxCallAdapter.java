package io.dume.dume.common.inboxActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jackandphantom.circularprogressbar.CircleProgressbar;

import java.util.List;

import carbon.widget.ImageView;
import io.dume.dume.R;

public class InboxCallAdapter extends RecyclerView.Adapter<InboxCallAdapter.MyViewHolder> {

    private static final String TAG = "InboxCallAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<InboxCallData> data;

    public InboxCallAdapter(Context context, List<InboxCallData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_call_row, parent, false);
        InboxCallAdapter.MyViewHolder holder = new InboxCallAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        InboxCallData inboxCallData = data.get(position);
        holder.callUserName.setText(inboxCallData.userName);
        holder.frequencyAndTime.setText("+88" + inboxCallData.phoneNumber);
        Glide.with(context).load(inboxCallData.avatar).into(holder.chatUserDP);
        holder.hostRelativeLayout.setOnClickListener(view -> {
            Uri u = Uri.parse("tel:" + inboxCallData.phoneNumber);
            Intent i = new Intent(Intent.ACTION_DIAL, u);
            context.startActivity(i);
        });
        holder.callBtnImage.setOnClickListener(view -> holder.hostRelativeLayout.performClick());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout hostRelativeLayout;
        private final ImageView chatUserDP;
        private final CircleProgressbar onlineIndicator;
        private final CircleProgressbar offlineIndicator;
        private final ImageView callBtnImage;
        private final TextView callUserName;
        private final ImageView callSendOrReceived;
        private final TextView frequencyAndTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            hostRelativeLayout = itemView.findViewById(R.id.hostRelativeLayout);
            chatUserDP = itemView.findViewById(R.id.chat_user_display_pic);
            onlineIndicator = itemView.findViewById(R.id.online_Indicator);
            offlineIndicator = itemView.findViewById(R.id.offline_Indicator);
            callBtnImage = itemView.findViewById(R.id.call_btn_image);
            callUserName = itemView.findViewById(R.id.call_user_name);
            callSendOrReceived = itemView.findViewById(R.id.send_or_received);
            frequencyAndTime = itemView.findViewById(R.id.frequency_and_time);
        }
    }
}
