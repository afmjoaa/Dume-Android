package io.dume.dume.student.studentPayment.adapterAndData;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import io.dume.dume.R;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {

    private static final String TAG = "PaymentAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<PaymentData> data;
    private RelativeLayout.LayoutParams params;

    public PaymentAdapter(Context context , List<PaymentData> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.custom_payment_method_row, viewGroup, false);
        PaymentAdapter.MyViewHolder holder = new PaymentAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        PaymentData current = data.get(position);
        params = (RelativeLayout.LayoutParams) myViewHolder.textContainer.getLayoutParams();
        myViewHolder.mainText.setText(current.primaryText);
        if(current.secondaryValue== 1){
            myViewHolder.subText.setVisibility(View.VISIBLE);
            params.setMargins(0, (int) (10 * (context.getResources().getDisplayMetrics().density)), 0, (int) (10 * (context.getResources().getDisplayMetrics().density)));
            myViewHolder.textContainer.setLayoutParams(params);
        }else {
            myViewHolder.subText.setVisibility(View.GONE);
            //button margin fix
            params.setMargins(0, (int) (15 * (context.getResources().getDisplayMetrics().density)), 0, (int) (15 * (context.getResources().getDisplayMetrics().density)));
            myViewHolder.textContainer.setLayoutParams(params);
        }
        myViewHolder.mainIcon.setImageResource(current.imageSrc);
       //fixing the padding here
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout hostingLayout;
        private final ImageView mainIcon;
        private final TextView mainText;
        private final TextView subText;
        private final carbon.widget.ImageView moreVertIcon;
        private final View devider;
        private final LinearLayout textContainer;

        public MyViewHolder(View itemView) {
            super(itemView);
            hostingLayout = itemView.findViewById(R.id.recycler_container_layout);
            mainIcon = itemView.findViewById(R.id.auto_image_icon);
            mainText = itemView.findViewById(R.id.text_one);
            subText = itemView.findViewById(R.id.text_two);
            moreVertIcon = itemView.findViewById(R.id.more_vertical_icon);
            devider = itemView.findViewById(R.id.divider2);
            textContainer = itemView.findViewById(R.id.vertical_textview_container);

        }
    }
}
