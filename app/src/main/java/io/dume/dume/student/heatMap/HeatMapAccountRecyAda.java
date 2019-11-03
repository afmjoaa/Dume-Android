package io.dume.dume.student.heatMap;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import io.dume.dume.R;

public abstract class HeatMapAccountRecyAda extends RecyclerView.Adapter<HeatMapAccountRecyAda.MyViewHolder> {

    private LayoutInflater inflater;
    List<AccountRecyData> data = Collections.emptyList();
    private Context context;


    public HeatMapAccountRecyAda(Context context, List<AccountRecyData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_account_recycler_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AccountRecyData current = data.get(position);
        holder.accouTypeText.setText(current.accouName);
        holder.accouIcon.setImageResource(current.iconId);
        if (position == (data.size() - 1)) {
            holder.divider.setVisibility(View.INVISIBLE);
        }

        if (current.selectedOne == position) {
            holder.doneIcon.setVisibility(View.VISIBLE);
        } else {
            holder.doneIcon.setVisibility(View.INVISIBLE);
        }

        if (position == 2) {
            holder.accouTypeText.setTextColor(Color.GRAY);
        }
        holder.accouContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Item clicked at " + position, Toast.LENGTH_SHORT).show();
                /*for (int i = 0; i < data.size(); i++) {
                    data.get(i).selectedOne = position;
                }
                notifyDataSetChanged();*/
                OnAccouItemClicked(v, position);
            }
        });
    }

    protected abstract void OnAccouItemClicked(View v, int position);

    public void update(List<AccountRecyData> newData) {
        data.clear();
        data.addAll(newData);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout accouContainer;
        ImageView accouIcon;
        TextView accouTypeText;
        ImageView doneIcon;
        View divider;

        public MyViewHolder(View itemView) {
            super(itemView);
            accouContainer = itemView.findViewById(R.id.linear_accou_container);
            accouIcon = itemView.findViewById(R.id.account_icon);
            accouTypeText = itemView.findViewById(R.id.account_type_textview);
            doneIcon = itemView.findViewById(R.id.account_selected_icon_container);
            divider = itemView.findViewById(R.id.divider2);
        }

    }
}
