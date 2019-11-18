package io.dume.dume.student.grabingInfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import carbon.widget.LinearLayout;
import io.dume.dume.R;

public class SalaryAdapter extends RecyclerView.Adapter<SalaryAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<SalaryData> data;
    private Context context;

    public SalaryAdapter(Context context , List<SalaryData> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.salary_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout salaryItemHolder;
        public MyViewHolder(View itemView) {
            super(itemView);
            salaryItemHolder = itemView.findViewById(R.id.salaryItemHolder);
        }

    }
}
