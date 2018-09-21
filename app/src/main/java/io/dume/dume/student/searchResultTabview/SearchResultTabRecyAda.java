package io.dume.dume.student.searchResultTabview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import carbon.widget.ImageView;
import io.dume.dume.R;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class SearchResultTabRecyAda extends RecyclerView.Adapter<SearchResultTabRecyAda.MyViewHolder> {

    private static final String TAG = "SearchResultTabRecyAda";
    private LayoutInflater inflater;
    private Context context;
    private List<SearchResultTabData> data;

    public SearchResultTabRecyAda(Context context, List<SearchResultTabData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
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
        if(position % 2 == 0){
            holder.hostRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.tabFirstItem));
        }else {
            holder.hostRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.tabSecondItem));

        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

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
