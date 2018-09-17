package io.dume.dume.student.searchResult;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.dume.dume.R;

public class QualificationAdapter extends RecyclerView.Adapter<QualificationAdapter.MyViewHolder> {

    private static final String TAG = "QualificationAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<QualificationData> data;


    public QualificationAdapter(Context context , List<QualificationData> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_qualification_row, parent, false);
        QualificationAdapter.MyViewHolder holder = new QualificationAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        QualificationData current = data.get(position);
        holder.institutionText.setText(current.institution);
        holder.examText.setText(current.exam);
        holder.sessionText.setText(current.session);
        holder.resultText.setText(current.result);
        if(position == 0){
            holder.institutionText.setBackgroundResource(R.drawable.bg_rating_tittle);
            holder.examText.setBackgroundResource(R.drawable.bg_rating_tittle);
            holder.sessionText.setBackgroundResource(R.drawable.bg_rating_tittle);
            holder.resultText.setBackgroundResource(R.drawable.bg_rating_tittle);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView institutionText;
        private final TextView examText;
        private final TextView sessionText;
        private final TextView resultText;

        public MyViewHolder(View itemView) {
            super(itemView);
            institutionText = itemView.findViewById(R.id.textview_institution);
            examText = itemView.findViewById(R.id.textview_Exam);
            sessionText = itemView.findViewById(R.id.textview_Session);
            resultText = itemView.findViewById(R.id.textview_Result);

        }
    }
}
