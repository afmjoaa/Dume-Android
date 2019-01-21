package io.dume.dume.teacher.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.teacher.model.KeyValueModel;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportVH> {
    private ArrayList<KeyValueModel> arrayList=null;

    public ReportAdapter(ArrayList<KeyValueModel> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ReportVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main_report_view_item, viewGroup, false);
        return new ReportVH(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportVH reportVH, int i) {
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

        public ReportVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
