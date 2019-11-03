package io.dume.dume.teacher.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.teacher.model.KeyValueModel;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportVH> {
    private final Context context;
    private final int itemWidth;
    private ArrayList<KeyValueModel> arrayList = null;

    public ReportAdapter(Context context, int itemWidth, ArrayList<KeyValueModel> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        this.itemWidth = itemWidth;
    }

    @NonNull
    @Override
    public ReportVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main_report_view_item, viewGroup, false);
        return new ReportVH(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportVH reportVH, int i) {
        ViewGroup.LayoutParams layoutParams = reportVH.hostingRelativeLayout.getLayoutParams();
        layoutParams.width = (itemWidth);
        reportVH.hostingRelativeLayout.setLayoutParams(layoutParams);


        reportVH.reportTitle.setText(arrayList.get(i).getTitle());
        reportVH.reportValue.setText(arrayList.get(i).getValue());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ReportVH extends RecyclerView.ViewHolder {
        @BindView(R.id.reportTitle)
        TextView reportTitle;
        @BindView(R.id.reportValue)
        TextView reportValue;
        @BindView(R.id.hosting_relative_layout)
        RelativeLayout hostingRelativeLayout;

        public ReportVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
