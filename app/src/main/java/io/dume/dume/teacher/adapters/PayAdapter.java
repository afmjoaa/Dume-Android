package io.dume.dume.teacher.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import carbon.widget.RelativeLayout;
import io.dume.dume.R;
import io.dume.dume.teacher.pojo.Pay;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayVH> {
    private final Context context;
    private final int itemWidth;
    ArrayList<Pay> payArrayList;

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

        ViewGroup.LayoutParams layoutParams = viewHolder.hostingRelative.getLayoutParams();
        layoutParams.width = (itemWidth);
        viewHolder.hostingRelative.setLayoutParams(layoutParams);

        if (i == 0) {
            if (payArrayList.get(i).isHaveDiscount()) {
                viewHolder.title.setText(payArrayList.get(i).getBuckTitle());
                viewHolder.afterDis.setText("$"+(payArrayList.get(i).getBucks() * payArrayList.get(i).getDiscount()) / 100  + "");
                viewHolder.beforeDis.setText("$"+payArrayList.get(i).getBucks() + "");
            }
        }else {
            viewHolder.title.setText(payArrayList.get(i).getBuckTitle());
            viewHolder.afterDis.setText("$"+payArrayList.get(i).getBucks());
            viewHolder.redCut.setVisibility(View.GONE);
            viewHolder.beforeDis.setVisibility(View.GONE);
       }


    }

    @Override
    public int getItemCount() {
        return payArrayList.size();
    }


    class PayVH extends RecyclerView.ViewHolder {
        private TextView beforeDis, afterDis, title, discount;
        private final RelativeLayout hostingRelative;
        private final View redCut;

        PayVH(@NonNull View itemView) {
            super(itemView);
            hostingRelative = itemView.findViewById(R.id.hosting_relative_layout);
            title = itemView.findViewById(R.id.reportTitle);
            beforeDis = itemView.findViewById(R.id.beforeDiscount);
            afterDis = itemView.findViewById(R.id.afterDiscount);
            discount = itemView.findViewById(R.id.discountTV);
            redCut = itemView.findViewById(R.id.red_cut);
        }
    }


}
