package io.dume.dume.teacher.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.dume.dume.R;
import io.dume.dume.teacher.pojo.Academic;

public class AcademicAdapter extends RecyclerView.Adapter<AcademicAdapter.AcademicVH> {

    private static final String TAG = "AcademicAdapter";
    private final Activity activity;
    private final List<Academic> data;
    private Context context;

    public AcademicAdapter(Context context,  List<Academic> data) {
        this.context = context;
        this.activity = (Activity) context;
        this.data = data;
    }


    @NonNull
    @Override
    public AcademicVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new AcademicVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.academic_fragment_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AcademicVH academicVH, int position) {

    }

    public void update(List<Academic> newData){
        data.clear();
        data.addAll(newData);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class AcademicVH extends RecyclerView.ViewHolder {

        public AcademicVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
