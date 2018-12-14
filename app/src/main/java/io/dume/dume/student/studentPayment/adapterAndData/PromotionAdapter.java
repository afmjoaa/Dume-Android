package io.dume.dume.student.studentPayment.adapterAndData;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.dume.dume.R;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.MyViewHolder> {

    private static final String TAG = "PromotionAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<PromotionData> data;

    public PromotionAdapter(Context context, List<PromotionData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_promotion_row, viewGroup, false);
        PromotionAdapter.MyViewHolder holder = new PromotionAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

}
