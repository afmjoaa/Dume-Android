package io.dume.dume.student.searchLoading;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import carbon.widget.ImageView;
import io.dume.dume.R;

public class SearchPackageAdapter extends RecyclerView.Adapter<SearchPackageAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<SearchDetailData> data;
    private int[] navIcons = {
            R.drawable.ic_seven_days,
            R.drawable.ic_preffered_day,
            R.drawable.ic_time
    };
    private int[] packageIcon = {
            R.drawable.dume_gang_image,
            R.drawable.dume_regular_image,
            R.drawable.dume_instant_image
    };

    public SearchPackageAdapter(Context context, List<SearchDetailData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_search_detail_row, viewGroup, false);
        SearchPackageAdapter.MyViewHolder holder = new SearchPackageAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        SearchDetailData current = data.get(position);
        if (position == 0) {
            switch (current.getItemName()) {
                case "Couching Service":
                    myViewHolder.searchDetailImage.setImageResource(packageIcon[0]);
                    break;
                case "Monthly Tutor":
                    myViewHolder.searchDetailImage.setImageResource(packageIcon[1]);
                    break;
                default:
                    myViewHolder.searchDetailImage.setImageResource(packageIcon[2]);
                    break;
            }
        } else if(position == 1){
            myViewHolder.searchDetailImage.setImageResource(navIcons[0]);
        } else  if(position == 2){
            myViewHolder.searchDetailImage.setImageResource(navIcons[1]);
        }else if(position == 3){
            myViewHolder.searchDetailImage.setImageResource(navIcons[2]);
        }
        myViewHolder.searchDetailMain.setText(current.getItemName());
        myViewHolder.searchDetailSub.setText(current.getItemInfo());
        if (position == (data.size() - 1)) {
            myViewHolder.divider.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView searchDetailMain;
        private final TextView searchDetailSub;
        private final Button searchDetailChangeBtn;
        private final View divider;
        private final ImageView searchDetailImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            searchDetailMain = itemView.findViewById(R.id.search_detail_main);
            searchDetailSub = itemView.findViewById(R.id.search_detail_sub);
            searchDetailChangeBtn = itemView.findViewById(R.id.search_interact_info);
            divider = itemView.findViewById(R.id.divider2);
            searchDetailImage = itemView.findViewById(R.id.search_detail_icon);
        }
    }
}
