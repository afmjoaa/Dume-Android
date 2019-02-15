package io.dume.dume.student.searchResultTabview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import carbon.widget.ImageView;
import io.dume.dume.R;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.searchResult.SearchResultActivity;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.facebook.FacebookSdk.getApplicationContext;

public abstract class SearchResultTabRecyAda extends RecyclerView.Adapter<SearchResultTabRecyAda.MyViewHolder> {

    private static final String TAG = "SearchResultTabRecyAda";
    private final Activity activity;
    private LayoutInflater inflater;
    private Context context;
    private List<SearchResultTabData> data;
    int[] imageIcons = {
            R.drawable.ic_tic_salary,
            R.drawable.ic_tic_performance,
            R.drawable.ic_tic_expertise,
            R.drawable.ic_tic_accept_ratio
    };
    private String defaultUrl;
    private final float mDensity;

    public SearchResultTabRecyAda(Context context, List<SearchResultTabData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        this.activity = (Activity)context;
        mDensity = context.getResources().getDisplayMetrics().density;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_searchresulttabview_row, parent, false);
        SearchResultTabRecyAda.MyViewHolder holder = new SearchResultTabRecyAda.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SearchResultTabData current = data.get(position);
        if(position % 2 == 0){
            holder.hostRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.tabFirstItem));
        }else {
            holder.hostRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.tabSecondItem));
        }
        switch (current.mentorFilterImage){
            case 1:
                holder.mentorFilterIcon.setImageResource(imageIcons[0]);
                holder.mentorFilterInfo.setText(String.format("... %s", current.salary));
                break;
            case 2:
                holder.mentorFilterIcon.setImageResource(imageIcons[1]);
                holder.mentorFilterInfo.setText(String.format("... %s", current.rating));
                break;
            case 3:
                holder.mentorFilterIcon.setImageResource(imageIcons[2]);
                holder.mentorFilterInfo.setText(String.format("... %s", current.expertise));
                break;
            case 4:
                holder.mentorFilterIcon.setImageResource(imageIcons[3]);
                holder.mentorFilterInfo.setText(String.format("... %s", current.a_ratio));
                break;
        }
        holder.mentorName.setText(current.mentorName);
        if (current.mentorDPUrl != null && !current.mentorDPUrl.equals("")) {
            Glide.with(getApplicationContext()).load(current.mentorDPUrl).apply(new RequestOptions().override((int) (50*mDensity), (int) (50*mDensity)).placeholder(R.drawable.demo_default_avatar_dark)).into(holder.mentorDisplayPic);
        } else {
            if (current.gender.equals("Male") || current.gender.equals("")) {
                defaultUrl = SearchDataStore.DEFAULTMALEAVATER;
            } else {
                defaultUrl =  SearchDataStore.DEFAULTFEMALEAVATER;
            }
            Glide.with(getApplicationContext()).load(defaultUrl).apply(new RequestOptions().override((int) (50*mDensity), (int) (50*mDensity)).placeholder(R.drawable.demo_default_avatar_dark)).into(holder.mentorDisplayPic);
        }


        holder.hostRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                OnItemClicked(view, "select_" +current.identify.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    abstract void OnItemClicked(View v, String identify);

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final MaterialRatingBar mentorRatingBar;
        private final ImageView mentorDisplayPic;
        private final TextView mentorName;
        private final android.widget.ImageView mentorFilterIcon;
        private final TextView mentorFilterInfo;
        private final RelativeLayout hostRelativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            mentorDisplayPic = itemView.findViewById(R.id.mentor_display_pic);
            mentorName = itemView.findViewById(R.id.mentor_name);
            mentorFilterIcon = itemView.findViewById(R.id.mentor_filter_icon);
            mentorFilterInfo = itemView.findViewById(R.id.mentor_filter_info);
            mentorRatingBar = itemView.findViewById(R.id.mentor_rating_bar);
            hostRelativeLayout = itemView.findViewById(R.id.hostRelativeLayout);
        }
    }
}
