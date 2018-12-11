package io.dume.dume.student.studentPayment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import io.dume.dume.R;
import io.dume.dume.student.grabingLocation.MenualRecyclerData;
import io.dume.dume.student.grabingLocation.PlaceMenualRecyAda;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.studentHelp.StudentHelpActivity;
import io.dume.dume.student.studentPayment.adapterAndData.PaymentAdapter;
import io.dume.dume.student.studentPayment.adapterAndData.PaymentData;

import static io.dume.dume.util.DumeUtils.configAppbarTittle;
import static io.dume.dume.util.DumeUtils.configureAppbar;

public class StudentPaymentActivity extends CustomStuAppCompatActivity implements StudentPaymentContract.View {

    private StudentPaymentContract.Presenter mPresenter;
    private static final int fromFlag = 17;
    private static final String TAG = "StudentPaymentActivity";
    private RecyclerView paymentRecycleView;
    private PaymentAdapter paymentAdapter;
    private String[] paymentName;
    private RelativeLayout addPromoRelativeLayout;
    private LinearLayout mainLayoutContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stunav_activity2_student_payment);
        setActivityContext(this, fromFlag);
        mPresenter = new StudentPaymentPresenter(this, new StudentPaymentModel());
        mPresenter.studentPaymentEnqueue();
        configureAppbar(this, "Payment");
        //payment method recycler
        paymentAdapter = new PaymentAdapter(this, getFinalData());
        paymentRecycleView.setAdapter(paymentAdapter);
        paymentRecycleView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void findView() {
        paymentRecycleView = findViewById(R.id.payment_method_recycler);
        paymentName = getResources().getStringArray(R.array.payment_methods);
        addPromoRelativeLayout = findViewById(R.id.add_promotion_layout);
        mainLayoutContent = findViewById(R.id.hide_able_host);

    }

    @Override
    public void onAddPromoCodeApplied() {
        mainLayoutContent.setVisibility(View.GONE);
        configAppbarTittle(StudentPaymentActivity.this, "Add Promo Code");
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new ApplyPromoCodeFragment()).commit();
    }

    @Override
    public void initStudentPayment() {

    }

    @Override
    public void configStudentPayment() {

    }

    public List<PaymentData> getFinalData() {
        List<PaymentData> data = new ArrayList<>();
        int[] imageIcons = {
                R.drawable.ic_cash_pay_icon,
                R.drawable.ic_bkash_icon,
                R.drawable.ic_nexus_pay_icon
        };
        int[] paymentDefaultValue = {1,0,0};

        for (int i = 0; i < paymentName.length; i++) {
            PaymentData current = new PaymentData();
            current.primaryText = paymentName[i];
            current.secondaryValue = paymentDefaultValue[i];
            current.imageSrc = imageIcons[i];
            data.add(current);
        }
        return data;
    }

    public void onPaymentViewClicked(View view) {
        mPresenter.onStudentPaymentIntracted(view);
    }

    //testing the apply promo code fragment up
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ApplyPromoCodeFragment extends Fragment {

        private StudentPaymentActivity myMainActivity;
        private AutoCompleteTextView queryTextView;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            myMainActivity = (StudentPaymentActivity) getActivity();
            View rootView = inflater.inflate(R.layout.custom_contact_up_fragment, container, false);
            queryTextView = rootView.findViewById(R.id.feedback_textview);

            queryTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        queryTextView.setHint("Please describe your problem");
                    } else {
                        queryTextView.setHint("Please describe your problem");
                    }
                }
            });
            return rootView;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), StudentPaymentActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    //testing the apply promo code fragment up
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ViewPromotionsFragment extends Fragment {

        private StudentPaymentActivity myMainActivity;
        private AutoCompleteTextView queryTextView;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            myMainActivity = (StudentPaymentActivity) getActivity();
            View rootView = inflater.inflate(R.layout.custom_contact_up_fragment, container, false);
            return rootView;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), StudentPaymentActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

}
