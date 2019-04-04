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
        private TextView afterDis, title;
        private final RelativeLayout hostingRelative;

        PayVH(@NonNull View itemView) {
            super(itemView);
            hostingRelative = itemView.findViewById(R.id.hosting_relative_layout);
            title = itemView.findViewById(R.id.reportTitle);
            afterDis = itemView.findViewById(R.id.afterDiscount);
        }
    }
}
