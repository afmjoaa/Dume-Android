package io.dume.dume.student.studentPayment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.student.grabingLocation.MenualRecyclerData;
import io.dume.dume.student.grabingLocation.PlaceMenualRecyAda;
import io.dume.dume.student.homePage.HomePageModel;
import io.dume.dume.student.homePage.adapter.HomePageRatingData;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerData;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.studentHelp.StudentHelpActivity;
import io.dume.dume.student.studentPayment.adapterAndData.ObligationAndClaimAdapter;
import io.dume.dume.student.studentPayment.adapterAndData.ObligationAndClaimData;
import io.dume.dume.student.studentPayment.adapterAndData.PaymentAdapter;
import io.dume.dume.student.studentPayment.adapterAndData.PaymentData;
import io.dume.dume.student.studentPayment.adapterAndData.PromotionAdapter;
import io.dume.dume.student.studentPayment.adapterAndData.PromotionData;
import io.dume.dume.student.studentPayment.adapterAndData.TransactionData;
import io.dume.dume.student.studentPayment.adapterAndData.TransactionHistoryAdapter;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.RadioBtnDialogue;

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
    private RelativeLayout promotionsView;
    private LinearLayout secondaryHideAbleLayout;
    private RelativeLayout idBlock;
    private RelativeLayout refBlock;
    private String[] paymentMethodArr;
    private FrameLayout content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stunav_activity2_student_payment);
        setActivityContext(this, fromFlag);
        findLoadView();
        mPresenter = new StudentPaymentPresenter(this, new StudentPaymentModel());
        mPresenter.studentPaymentEnqueue();
        configureAppbar(this, "Payment");
        //payment method recycler
        paymentAdapter = new PaymentAdapter(this, getFinalData()) {
            @Override
            protected void OnButtonClicked(View v, String methodName) {
                switch (methodName) {
                    case "Cash":
                        onCashClicked();
                        break;
                    case "Bkash":
                        onBkashClicked();
                        break;
                    case "NexusPay/Rocket":
                        onNexusPayClicked();
                        break;
                }
            }
        };
        paymentRecycleView.setAdapter(paymentAdapter);
        paymentRecycleView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void onCashClicked() {
        mainLayoutContent.setVisibility(View.GONE);
        secondaryHideAbleLayout.setVisibility(View.VISIBLE);
        idBlock.setVisibility(View.GONE);
        refBlock.setVisibility(View.GONE);
        configAppbarTittle(StudentPaymentActivity.this, paymentName[0]);
    }

    private void onBkashClicked() {
        mainLayoutContent.setVisibility(View.GONE);
        secondaryHideAbleLayout.setVisibility(View.VISIBLE);
        idBlock.setVisibility(View.VISIBLE);
        refBlock.setVisibility(View.VISIBLE);
        configAppbarTittle(StudentPaymentActivity.this, paymentName[1]);
    }

    private void onNexusPayClicked() {
        mainLayoutContent.setVisibility(View.GONE);
        secondaryHideAbleLayout.setVisibility(View.VISIBLE);
        idBlock.setVisibility(View.VISIBLE);
        refBlock.setVisibility(View.VISIBLE);
        configAppbarTittle(StudentPaymentActivity.this, paymentName[2]);
    }

    @Override
    public void findView() {
        paymentRecycleView = findViewById(R.id.payment_method_recycler);
        paymentName = getResources().getStringArray(R.array.payment_methods);
        addPromoRelativeLayout = findViewById(R.id.add_promotion_layout);
        mainLayoutContent = findViewById(R.id.hide_able_host);
        promotionsView = findViewById(R.id.promotion_relative_layout);
        secondaryHideAbleLayout = findViewById(R.id.secondary_hide_able_layout);
        idBlock = findViewById(R.id.id_block);
        refBlock = findViewById(R.id.ref_block);
        content = findViewById(R.id.content);
        paymentMethodArr = this.getResources().getStringArray(R.array.add_payment_methods);

    }

    @Override
    public void initStudentPayment() {

    }

    @Override
    public void configStudentPayment() {

    }

    @Override
    public void onAddPromoCodeApplied() {
        mainLayoutContent.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        configAppbarTittle(StudentPaymentActivity.this, "Add Promo Code");
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new ApplyPromoCodeFragment()).commit();
    }

    @Override
    public void onViewPromotionsClicked() {
        mainLayoutContent.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        configAppbarTittle(StudentPaymentActivity.this, "Promotions");
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new ViewPromotionsFragment()).commit();
    }

    @Override
    public void onTransactionHistoryClicked() {
        mainLayoutContent.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        configAppbarTittle(StudentPaymentActivity.this, "Your History");
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new ViewTransactionHistoryFragment()).commit();
    }

    @Override
    public void onObligtionClaimClicked() {
        mainLayoutContent.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        configAppbarTittle(StudentPaymentActivity.this, "Obligations");
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new ViewObligationFragment()).commit();
    }

    @Override
    public void onAddPaymentMethod() {
        Bundle pRargs = new Bundle();
        pRargs.putString("title", "Select payment method");
        pRargs.putStringArray("radioOptions", paymentMethodArr);
        RadioBtnDialogue addPaymentDialogue = new RadioBtnDialogue();
        addPaymentDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // selectGenderTextView.setText(genderSelcetionArr[i]);
                Toast.makeText(StudentPaymentActivity.this, "fucked that", Toast.LENGTH_SHORT).show();
            }
        });
        addPaymentDialogue.setArguments(pRargs);
        addPaymentDialogue.show(getSupportFragmentManager(), "payment_method_dialogue");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (mainLayoutContent.getVisibility() == View.VISIBLE) {
                super.onBackPressed();
            } else {
                configAppbarTittle(StudentPaymentActivity.this, "Payment");
                mainLayoutContent.setVisibility(View.VISIBLE);
                secondaryHideAbleLayout.setVisibility(View.GONE);
                content.setVisibility(View.GONE);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public List<PaymentData> getFinalData() {
        List<PaymentData> data = new ArrayList<>();
        int[] imageIcons = {
                R.drawable.ic_cash_pay_icon,
                R.drawable.ic_bkash_icon,
                R.drawable.ic_nexus_pay_icon
        };
        int[] paymentDefaultValue = {1, 0, 0};

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

    //testing the view promo code fragments
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ViewPromotionsFragment extends Fragment {

        private StudentPaymentActivity myMainActivity;
        private RecyclerView pCustomRecyclerView;
        private Context context;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            myMainActivity = (StudentPaymentActivity) getActivity();
            View rootView = inflater.inflate(R.layout.p_custom_recycler_row, container, false);
            pCustomRecyclerView = rootView.findViewById(R.id.p_recycler_view);

            //menual one
            List<HomePageRecyclerData> promotionData = new ArrayList<>();
            PromotionAdapter promotionAdapter = new PromotionAdapter(myMainActivity, promotionData);
            pCustomRecyclerView.setAdapter(promotionAdapter);
            pCustomRecyclerView.setLayoutManager(new LinearLayoutManager(myMainActivity));

            List<String> appliedPromoList;

            if (Google.getInstance().getAccountMajor() == DumeUtils.TEACHER) {
                Map<String, Object> documentSnapshot = TeacherDataStore.getInstance().getDocumentSnapshot();
                appliedPromoList = (ArrayList) documentSnapshot.get("applied_promo");


            } else {
                Map<String, Object> documentSnapshot = SearchDataStore.getInstance().getDocumentSnapshot();
                appliedPromoList = (ArrayList) documentSnapshot.get("applied_promo");

            }
            if (appliedPromoList == null) {
                appliedPromoList = new ArrayList<>();
            }


            for (String promo_code : appliedPromoList) {

                Map<String, Object> promoMap;

                if (Google.getInstance().getAccountMajor() == DumeUtils.TEACHER) {
                    Map<String, Object> documentSnapshot = TeacherDataStore.getInstance().getDocumentSnapshot();
                    promoMap = (Map<String, Object>) documentSnapshot.get(promo_code);


                } else {
                    Map<String, Object> documentSnapshot = SearchDataStore.getInstance().getDocumentSnapshot();
                    promoMap = (Map<String, Object>) documentSnapshot.get(promo_code);

                }
                HomePageRecyclerData homePageRecyclerData = new HomePageRecyclerData();
                homePageRecyclerData.setTitle(promoMap.get("title").toString());
                homePageRecyclerData.setDescription(promoMap.get("description").toString());
                homePageRecyclerData.setStart_date((Date) promoMap.get("start_date"));
                homePageRecyclerData.setProduct(promoMap.get("product").toString());
                Long max_tution_count = (Long) promoMap.get("max_tution_count");
                homePageRecyclerData.setMax_tution_count(max_tution_count.intValue());
                Long max_dicount_percentage = (Long) promoMap.get("max_dicount_percentage");
                homePageRecyclerData.setMax_dicount_percentage((Integer) max_dicount_percentage.intValue());
                Long max_discount_credit = (Long) promoMap.get("max_discount_credit");
                homePageRecyclerData.setMax_discount_credit((Integer) max_discount_credit.intValue());
                homePageRecyclerData.setExpirity((Date) promoMap.get("expirity"));
                homePageRecyclerData.setCriteria((Map<String, Object>) promoMap.get("criteria"));
                homePageRecyclerData.setPromo_for(promoMap.get("promo_for").toString());
                homePageRecyclerData.setPromo_image(promoMap.get("promo_image").toString());
                homePageRecyclerData.setSub_description(promoMap.get("sub_description").toString());
                homePageRecyclerData.setPromo_code(promoMap.get("promo_code").toString());
                homePageRecyclerData.setExpired((Boolean) promoMap.get("expired"));
                promotionAdapter.addPromoToList(homePageRecyclerData);

            }
            //


            return rootView;
        }

        @Override
        public void onAttach(Context context) {
            this.context = context;
            super.onAttach(context);
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


    //testing the view transaction history fragment
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ViewTransactionHistoryFragment extends Fragment {

        private StudentPaymentActivity myMainActivity;
        private RecyclerView pCustomRecyclerView;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            myMainActivity = (StudentPaymentActivity) getActivity();
            View rootView = inflater.inflate(R.layout.p_custom_recycler_row, container, false);
            pCustomRecyclerView = rootView.findViewById(R.id.p_recycler_view);

            //menual one
            List<TransactionData> transactionData = new ArrayList<>();
            TransactionHistoryAdapter transactionHistoryAdapter = new TransactionHistoryAdapter(myMainActivity, transactionData);
            pCustomRecyclerView.setAdapter(transactionHistoryAdapter);
            pCustomRecyclerView.setLayoutManager(new LinearLayoutManager(myMainActivity));
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


    //testing the Obligation fragment
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ViewObligationFragment extends Fragment {

        private StudentPaymentActivity myMainActivity;
        private RecyclerView pCustomRecyclerView;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            myMainActivity = (StudentPaymentActivity) getActivity();
            View rootView = inflater.inflate(R.layout.p_custom_recycler_row, container, false);
            pCustomRecyclerView = rootView.findViewById(R.id.p_recycler_view);

            //menual one
            List<ObligationAndClaimData> transactionData = new ArrayList<>();
            ObligationAndClaimAdapter obligationAndClaimAdapter = new ObligationAndClaimAdapter(myMainActivity, transactionData);
            pCustomRecyclerView.setAdapter(obligationAndClaimAdapter);
            pCustomRecyclerView.setLayoutManager(new LinearLayoutManager(myMainActivity));
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
