package io.dume.dume.teacher.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.dume.dume.R;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportVH> {


    @NonNull
    @Override
    public ReportVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main_report_view_item, viewGroup, false);

        return new ReportVH(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportVH reportVH, int i) {

    }

    @Override
    public int getItemCount() {
        return 6 ;
    }

    class ReportVH extends RecyclerView.ViewHolder {

        public ReportVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
