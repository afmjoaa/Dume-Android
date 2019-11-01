package io.dume.dume.teacher.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import carbon.widget.RelativeLayout;
import io.dume.dume.R;
import io.dume.dume.teacher.pojo.Pay;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayVH> {
    private final Context context;
    private final int itemWidth;
    ArrayList<Pay> payArrayList;
    private Pay pay;

    public PayAdapter(Context context, int itemWidth, ArrayList<Pay> payArrayList) {
        this.payArrayList = payArrayList;
        this.context = context;
        this.itemWidth = itemWidth;
    }

    @NonNull
    @Override
    public PayVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PayVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pay_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PayVH viewHolder, int i) {
        pay = payArrayList.get(i);
        ViewGroup.LayoutParams layoutParams = viewHolder.hostingRelative.getLayoutParams();
        layoutParams.width = (itemWidth);
        viewHolder.hostingRelative.setLayoutParams(layoutParams);
        viewHolder.title.setText(pay.getBuckTitle());
        if (payArrayList.get(i).getBucks() < 0) {
            viewHolder.title.setText("Advance Paid");
            viewHolder.afterDis.setText(Math.abs(pay.getBucks()) + " ৳");
        } else viewHolder.afterDis.setText(payArrayList.get(i).getBucks() + " ৳");



    }

    @Override
    public int getItemCount() {
        return payArrayList.size();
    }

    class PayVH extends RecyclerView.ViewHolder {
        private TextView afterDis;
        private View itemView;
        private TextView title;
        private final RelativeLayout hostingRelative;

        PayVH(@NonNull View itemView) {
            super(itemView);
            hostingRelative = itemView.findViewById(R.id.hosting_relative_layout);
            title = itemView.findViewById(R.id.reportTitle);
            afterDis = itemView.findViewById(R.id.afterDiscount);
            this.itemView = itemView;
        }
    }
}
