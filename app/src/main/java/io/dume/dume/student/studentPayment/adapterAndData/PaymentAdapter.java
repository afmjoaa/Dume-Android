package io.dume.dume.student.studentPayment.adapterAndData;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import io.dume.dume.R;

public abstract class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {

    private static final String TAG = "PaymentAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<PaymentData> data;
    private RelativeLayout.LayoutParams params;

    public PaymentAdapter(Context context, List<PaymentData> data) {
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
        myViewHolder.mainIcon.setImageResource(current.imageSrc);

        if((data.size()-1)== position){
            myViewHolder.devider.setVisibility(View.GONE);
        }

        if (current.secondaryValue == 0) {
            myViewHolder.subText.setVisibility(View.VISIBLE);
            myViewHolder.subText.setText("Coming soon...");
            params.setMargins(0, (int) (10 * (context.getResources().getDisplayMetrics().density)), 0, (int) (10 * (context.getResources().getDisplayMetrics().density)));
            myViewHolder.textContainer.setLayoutParams(params);
            myViewHolder.moreVertIcon.setVisibility(View.GONE);
            //myViewHolder.hostingLayout.setBackgroundColor(context.getResources().getColor(R.color.disable_color));
        }else{
            myViewHolder.subText.setVisibility(View.GONE);
            params.setMargins(0, (int) (15 * (context.getResources().getDisplayMetrics().density)), 0, (int) (15 * (context.getResources().getDisplayMetrics().density)));
            myViewHolder.textContainer.setLayoutParams(params);
        }
        //fixing the padding here
        myViewHolder.hostingLayout.setOnClickListener(view -> OnButtonClicked(view, current.primaryText));
        myViewHolder.moreVertIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, view);
                popup.inflate(R.menu.menu_payment_method_cash_only);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if(itemId == R.id.how){
                            if(current.primaryText.equals("Cash Payment")){
                                testingCustomDialogue(R.string.cash_info, current.primaryText);
                            }else {
                                testingCustomDialogue(R.string.payment_info, current.primaryText);
                            }
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    public void testingCustomDialogue(int infoStringId , String primaryText) {
        // custom dialog
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_obligation_dialogue);

        //Toast.makeText(context, primaryText, Toast.LENGTH_SHORT).show();
        //all find view here
        Button dismissBtn = dialog.findViewById(R.id.dismiss_btn);
        TextView dialogText = dialog.findViewById(R.id.dialog_text);
        carbon.widget.ImageView dialogImage = dialog.findViewById(R.id.dialog_image);
        dialogText.setGravity(Gravity.START);

        dialogImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_info_icon_green));
        SpannableString text = new SpannableString(context.getResources().getString(infoStringId));
        text.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorLink)), text.length()-9, (text.length()), 0);
        text.setSpan(new URLSpan("https://dume-2d063.firebaseapp.com/home"), text.length()-9, (text.length()), 0);
        dialogText.setMovementMethod(LinkMovementMethod.getInstance());
        dialogText.setText(text, TextView.BufferType.SPANNABLE);
        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    protected abstract void OnButtonClicked(View v, String methodName);

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
