package io.dume.dume.common.inboxActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.List;

import carbon.widget.ImageView;
import io.dume.dume.R;
import io.dume.dume.util.VisibleToggleClickListener;

public class InboxNotiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "InboxNotiAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<InboxNotiData> data;
    int[] imageIcons = {
            R.drawable.ic_dume_admin, //default
            R.drawable.record_icon,  // for  record notifications
            R.drawable.ic_promo_icon, //for promotions
            R.drawable.ic_cancel_icon //for cancel
    };

    public InboxNotiAdapter(Context context, List<InboxNotiData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.inbox_notification_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        InboxNotiData item = data.get(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;


        if (item.isSeen()) {
            myViewHolder.onlineOrOffline.setForegroundProgressColor(R.color.status_viewed);
            //myViewHolder.host.setBackgroundColor(context.getResources().getColor(R.color.colorNarvik));
        }else {
            myViewHolder.onlineOrOffline.setForegroundProgressColor(R.color.inbox_active_color);
        }
        myViewHolder.notiUserName.setText(item.getTitle());
        myViewHolder.description.setText(item.getBody());

        myViewHolder.host.setOnClickListener(new VisibleToggleClickListener() {

            @SuppressLint("CheckResult")
            @Override
            protected void changeVisibility(boolean visible) {
                TransitionSet set = new TransitionSet()
                        .addTransition(new Fade())
                        .addTransition(new Slide(Gravity.TOP))
                        .setInterpolator(visible ? new LinearOutSlowInInterpolator() : new FastOutLinearInInterpolator())
                        .addListener(new Transition.TransitionListener() {
                            @Override
                            public void onTransitionStart(@NonNull Transition transition) {

                            }

                            @Override
                            public void onTransitionEnd(@NonNull Transition transition) {
                                if (!visible) {
                                    myViewHolder.recyclerLinearLayout.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onTransitionCancel(@NonNull Transition transition) {

                            }

                            @Override
                            public void onTransitionPause(@NonNull Transition transition) {

                            }

                            @Override
                            public void onTransitionResume(@NonNull Transition transition) {

                            }
                        });
                TransitionManager.beginDelayedTransition(myViewHolder.recyclerLinearLayout, set);
                if (visible) {
                    myViewHolder.recyclerLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    myViewHolder.recyclerLinearLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView notiUserDP;
        private final TextView notiUserName;
        private final TextView freqAndTime;
        private final CircleProgressbar onlineOrOffline;
        private final RelativeLayout host;
        private final LinearLayout recyclerLinearLayout;
        private final TextView description;
        private final TextView data;

        public MyViewHolder(View itemView) {
            super(itemView);
            notiUserDP = itemView.findViewById(R.id.noti_user_dp);
            notiUserName = itemView.findViewById(R.id.noti_user_name);
            freqAndTime = itemView.findViewById(R.id.frequency_and_time);
            onlineOrOffline = itemView.findViewById(R.id.selected_indicator);
            host = itemView.findViewById(R.id.hosting_relative_layout);
            recyclerLinearLayout = itemView.findViewById(R.id.recycle_hosting_linear);
            description = itemView.findViewById(R.id.description);
            data = itemView.findViewById(R.id.dateTV);
        }
    }

    class HeaderVH extends RecyclerView.ViewHolder {

        private final TextView headerText;

        public HeaderVH(View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.recent_updates);
        }
    }
}
