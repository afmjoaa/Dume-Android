package io.dume.dume.teacher.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.dume.dume.R;

public class AcademicAdapter extends RecyclerView.Adapter<AcademicAdapter.AcademicVH> {
    @NonNull
    @Override
    public AcademicVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AcademicVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.academic_fragment_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AcademicVH academicVH, int i) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class AcademicVH extends RecyclerView.ViewHolder {

        public AcademicVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
