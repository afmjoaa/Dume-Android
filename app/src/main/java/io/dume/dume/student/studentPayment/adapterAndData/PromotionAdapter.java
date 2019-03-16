package io.dume.dume.student.studentPayment.adapterAndData;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerData;
import io.dume.dume.util.DumeUtils;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.MyViewHolder> {

    private static final String TAG = "PromotionAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<HomePageRecyclerData> data;

    public PromotionAdapter(Context context, List<HomePageRecyclerData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    public void addPromoToList(HomePageRecyclerData promoData) {
        data.add(promoData);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_promotion_row, viewGroup, false);
        PromotionAdapter.MyViewHolder holder = new PromotionAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.title.setText(data.get(i).getTitle());
         HomePageRecyclerData homePageRecyclerData = data.get(i);
        holder.description.setText(homePageRecyclerData.getDescription());
        Date start_date = homePageRecyclerData.getStart_date();
        if(start_date!= null){
            holder.date.setText(DumeUtils.getFormattedDate(start_date));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.percent_off_btn)
        TextView title;
        @BindView(R.id.info_text)
        TextView description;
        @BindView(R.id.valid_time_text)
        TextView date;
        @BindView(R.id.valid_count_text)
        TextView tutionCount;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
