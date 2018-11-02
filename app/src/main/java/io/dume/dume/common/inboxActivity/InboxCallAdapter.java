package io.dume.dume.common.inboxActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jackandphantom.circularprogressbar.CircleProgressbar;

import java.util.List;

import carbon.widget.ImageView;
import io.dume.dume.R;

public class InboxCallAdapter extends RecyclerView.Adapter<InboxCallAdapter.MyViewHolder>{

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
        switch (position){
            case 1:
                //testing offline
                holder.onlineIndicator.setVisibility(View.GONE);
                holder.offlineIndicator.setVisibility(View.VISIBLE);
                break;
            case 2:
                //testing selected item
                holder.chatUserDP.setHeight((int) (44*context.getResources().getDisplayMetrics().density));
                holder.chatUserDP.setWidth((int) (44*context.getResources().getDisplayMetrics().density));
                break;
            case 4:
                //testing call icon
                holder.callSendOrReceived.setImageResource(R.drawable.ic_call_made_black_24dp);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 10;
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
