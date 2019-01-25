package io.dume.dume.teacher.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.dume.dume.R;
import io.dume.dume.teacher.pojo.Pay;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayVH> {
    ArrayList<Pay> payArrayList;

    public PayAdapter(ArrayList<Pay> payArrayList) {
        this.payArrayList = payArrayList;
    }


    @NonNull
    @Override
    public PayVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new PayVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pay_item, viewGroup, false));


    }

    @Override
    public void onBindViewHolder(@NonNull PayVH viewHolder, int i) {
        if (i == 0) {
            if (payArrayList.get(i).isHaveDiscount()) {
                viewHolder.discountContainer.setVisibility(View.VISIBLE);
                viewHolder.title.setText(payArrayList.get(i).getBuckTitle());
                viewHolder.discount.setText(payArrayList.get(i).getDiscount() + "% off");
                viewHolder.afterDis.setText("$"+(payArrayList.get(i).getBucks() * payArrayList.get(i).getDiscount()) / 100  + "");
                viewHolder.beforeDis.setText("$"+payArrayList.get(i).getBucks() + "");
            }
        }else {
            viewHolder.title.setText(payArrayList.get(i).getBuckTitle());
            viewHolder.afterDis.setText("$"+payArrayList.get(i).getBucks());
       }


    }

    @Override
    public int getItemCount() {
        return payArrayList.size();
    }


    class PayVH extends RecyclerView.ViewHolder {
        private FrameLayout discountContainer;
        private TextView beforeDis, afterDis, title, discount;

        PayVH(@NonNull View itemView) {
            super(itemView);
            discountContainer = itemView.findViewById(R.id.discountContainer);
            title = itemView.findViewById(R.id.reportTitle);
            beforeDis = itemView.findViewById(R.id.beforeDiscount);
            afterDis = itemView.findViewById(R.id.afterDiscount);
            discount = itemView.findViewById(R.id.discountTV);
        }
    }


}
