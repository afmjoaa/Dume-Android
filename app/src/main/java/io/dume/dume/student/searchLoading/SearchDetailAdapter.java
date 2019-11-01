package io.dume.dume.student.searchLoading;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import carbon.widget.ImageView;
import io.dume.dume.R;

import static io.dume.dume.util.DumeUtils.giveIconOnName;

public class SearchDetailAdapter extends RecyclerView.Adapter<SearchDetailAdapter.MyViewHolder> {

    private static final String TAG = "SearchDetailAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<SearchDetailData> data;
    private int[] navIcons = {
            R.drawable.xxx_level_vector,
            R.drawable.ic_medium,
            R.drawable.ic_class,
            R.drawable.ic_subject,
            R.drawable.xxx_field_vector,
            R.drawable.xxx_division_branch_vector,
            R.drawable.xxx_language_vector,
            R.drawable.xxx_programing_lang_vector,
            R.drawable.xxx_software_vector,
            R.drawable.xxx_music_vector,
            R.drawable.xxx_item_vector,
            R.drawable.xxx_flavour_type_vector,
            R.drawable.xxx_degree_vector,
            R.drawable.ic_cross_check,
            R.drawable.ic_gender_preference,
            R.drawable.ic_payment,
            R.drawable.xxx_item_capacity
    };

    public SearchDetailAdapter(Context context , List<SearchDetailData> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_search_detail_row, parent, false);
        SearchDetailAdapter.MyViewHolder holder = new SearchDetailAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SearchDetailData current = data.get(position);
        if(position == 0){
            holder.searchDetailImage.setCornerRadius((14 * (context.getResources().getDisplayMetrics().density)));
            holder.searchDetailImage.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
            if(current.getItemChange()!= null){
                //not from contact
                Glide.with(context).load(current.getItemChange()).apply(new RequestOptions().override(100, 100).placeholder(R.drawable.alias_profile_icon)).into(holder.searchDetailImage);
            }else {
                if(current.getImageSrc() != null){
                    holder.searchDetailImage.setImageBitmap(current.getImageSrc());
                }else{
                    //set default
                    holder.searchDetailImage.setImageResource(R.drawable.alias_profile_icon);
                }
            }
            holder.searchDetailMain.setText(current.getItemName());
            holder.searchDetailSub.setText(current.getItemInfo());
        }else {
            holder.searchDetailImage.setImageResource(navIcons[giveIconOnName((String)current.getItemInfo())]);
            holder.searchDetailMain.setText(current.getItemName());
            holder.searchDetailSub.setText(current.getItemInfo());
        }
        if(position == (data.size()-1)){
            holder.divider.setVisibility(View.GONE);
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
