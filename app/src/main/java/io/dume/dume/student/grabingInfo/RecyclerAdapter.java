package io.dume.dume.student.grabingInfo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Collections;
import java.util.List;

import io.dume.dume.R;

public abstract class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    List<RecycleData> data = Collections.emptyList();
    private Context context;

    public RecyclerAdapter(Context context ,List<RecycleData> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_demo_recycler_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        RecycleData current = data.get(position);
        holder.finalButtons.setText(current.options);
        holder.finalButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "Item clicked at "+ position, Toast.LENGTH_SHORT).show();
                OnButtonClicked(v, position);
            }
        });
    }

    protected abstract void OnButtonClicked(View v ,int position);

    class MyViewHolder extends RecyclerView.ViewHolder {

        Button finalButtons;
        public MyViewHolder(View itemView) {
            super(itemView);
            finalButtons = itemView.findViewById(R.id.finalInfoBtn);
        }

    }
}
