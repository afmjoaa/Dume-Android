package io.dume.dume.student.studentPayment.adapterAndData;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.dume.dume.R;
import io.dume.dume.util.DumeUtils;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.MyViewHolder> {

    private static final String TAG = "TransactionHistoryAdapt";
    private LayoutInflater inflater;
    private Context context;
    private List<PaymentHistory> data;

    public PaymentHistoryAdapter(Context context, List<PaymentHistory> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_transaction_history_row, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        PaymentHistory paymentHistory = data.get(i);

        holder.date.setText(DumeUtils.getFormattedDate(paymentHistory.getTransection_date()));
        holder.status.setText(paymentHistory.getStatus());
        switch (paymentHistory.getStatus()) {
            case "Accepted":
                holder.status.setBackgroundColor(Color.GREEN);
                holder.status.setTextColor(Color.WHITE);
                break;
            case "Rejected":
                holder.status.setBackgroundColor(Color.RED);
                holder.status.setTextColor(Color.WHITE);

                break;
            case "Pending":
                holder.status.setBackgroundColor(Color.GRAY);
                holder.status.setTextColor(Color.WHITE);

                break;
        }
        switch (paymentHistory.getPayment_method()) {
            case "bkash_transection":
                holder.method.setText("Bkash Trans Id");
                break;
            case "bkash_otp":
                holder.method.setText("Bkash OTP");
                break;
            case "roket":
                holder.method.setText("Rocket");
                break;
        }
        holder.amount.setText(paymentHistory.getAmount() + " ৳");

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date, status, method, amount;

        public MyViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateTV);
            amount = itemView.findViewById(R.id.amountTV);
            status = itemView.findViewById(R.id.statusTV);
            method = itemView.findViewById(R.id.methodTV);
        }
    }
}


