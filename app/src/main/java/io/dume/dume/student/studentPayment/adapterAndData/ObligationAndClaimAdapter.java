package io.dume.dume.student.studentPayment.adapterAndData;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.dume.dume.R;

public class ObligationAndClaimAdapter extends RecyclerView.Adapter<ObligationAndClaimAdapter.MyViewHolder> {

    private static final String TAG = "ObligationAndClaimAdapt";
    private LayoutInflater inflater;
    private Context context;
    private List<ObligationAndClaimData> data;

    public ObligationAndClaimAdapter(Context context, List<ObligationAndClaimData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_obligation_claim_row, viewGroup, false);
        ObligationAndClaimAdapter.MyViewHolder holder = new ObligationAndClaimAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
