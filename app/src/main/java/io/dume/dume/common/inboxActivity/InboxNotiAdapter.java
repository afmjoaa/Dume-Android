package io.dume.dume.common.inboxActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.List;

import carbon.widget.FrameLayout;
import carbon.widget.ImageView;
import io.dume.dume.R;
import io.dume.dume.model.DumeModel;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.VisibleToggleClickListener;

public class InboxNotiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "InboxNotiAdapter";
    private LayoutInflater inflater;
    private Context context;
    private BottomSheetDialog mBackBSD;
    private View backsheetRootView;
    private TextView backMainText;
    private TextView backSubText;
    private Button backYesBtn;
    private Button backNoBtn;
    private List<InboxNotiData> data = new ArrayList<>();
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
        mBackBSD = new BottomSheetDialog(context);
        backsheetRootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_bottom_sheet_dialogue_cancel, null);
        mBackBSD.setContentView(backsheetRootView);
        backMainText = mBackBSD.findViewById(R.id.main_text);
        backSubText = mBackBSD.findViewById(R.id.sub_text);
        backYesBtn = mBackBSD.findViewById(R.id.cancel_yes_btn);
        backNoBtn = mBackBSD.findViewById(R.id.cancel_no_btn);
        View view = inflater.inflate(R.layout.inbox_notification_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        InboxNotiData item = data.get(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        switch (item.getType()) {
            case "record":
                myViewHolder.titleImage.setImageResource(imageIcons[1]);
                break;
            case "cancel":
                myViewHolder.titleImage.setImageResource(imageIcons[3]);
                break;
            case "promo":
                myViewHolder.titleImage.setImageResource(imageIcons[2]);
                break;
            case "global":
                myViewHolder.titleImage.setImageResource(imageIcons[0]);
                break;
            default:
                myViewHolder.titleImage.setImageResource(imageIcons[0]);
                break;
        }

        if (item.isSeen()) {
            myViewHolder.onlineOrOffline.setForegroundProgressColor(R.color.status_viewed);
            myViewHolder.wrapper.setBackgroundColor(Color.WHITE);

        } else {
            myViewHolder.onlineOrOffline.setForegroundProgressColor(R.color.inbox_active_color);
            myViewHolder.wrapper.setBackgroundColor(context.getResources().getColor(R.color.notificationItemColor));

        }
        myViewHolder.title.setText(item.getTitle());
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


                if (!item.isSeen()) {
                    new DumeModel(context).modifySeenStatusNotification(item.getDoc_id(), new TeacherContract.Model.Listener<Boolean>() {
                        @Override
                        public void onSuccess(Boolean list) {
                            item.setSeen(true);
                            myViewHolder.onlineOrOffline.setForegroundProgressColor(R.color.status_viewed);
                            myViewHolder.wrapper.setBackgroundColor(Color.WHITE);
                            //notifyItemChanged(position);
                        }

                        @Override
                        public void onError(String msg) {
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        myViewHolder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (backMainText != null && backSubText != null && backYesBtn != null && backNoBtn != null) {
                    backMainText.setText("Remove notification ?");
                    backSubText.setText("Confirming will remove the notification...");
                    backYesBtn.setText("Yes, Remove");
                    backNoBtn.setText("No");

                    backYesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mBackBSD.dismiss();
                            new DumeModel(context).deleteNotification(item.getDoc_id(), new TeacherContract.Model.Listener<Boolean>() {
                                @Override
                                public void onSuccess(Boolean list) {
                                    Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(String msg) {
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                    backNoBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mBackBSD.dismiss();
                        }
                    });
                }
                mBackBSD.show();
            }
        });

        String avatar = item.getAvatar();
        if (avatar != null) {
            Glide.with(context).load(avatar).apply(new RequestOptions().override(40, 40).placeholder(R.drawable.record_icon)).into(myViewHolder.titleImage);
        }
        //myViewHolder.subTitle.setText(DumeUtils.getFormattedDate(item.getTimestapm()));
        myViewHolder.subTitle.setText(java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(item.getTimestapm().getTime()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView titleImage;
        private final TextView title;
        private final TextView subTitle;
        private final CircleProgressbar onlineOrOffline;
        private final RelativeLayout host;
        private final carbon.widget.LinearLayout recyclerLinearLayout;
        private final TextView description;
        private final TextView date;
        private final FrameLayout wrapper;
        private final ImageView deleteIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleImage = itemView.findViewById(R.id.categoryAvaterIV);
            title = itemView.findViewById(R.id.noti_user_name);
            subTitle = itemView.findViewById(R.id.frequency_and_time);
            onlineOrOffline = itemView.findViewById(R.id.selected_indicator);
            host = itemView.findViewById(R.id.hosting_relative_layout);
            recyclerLinearLayout = itemView.findViewById(R.id.recycle_hosting_linear);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.dateTV);
            wrapper = itemView.findViewById(R.id.primary_framelayout);
            deleteIcon = itemView.findViewById(R.id.skillDots);
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
