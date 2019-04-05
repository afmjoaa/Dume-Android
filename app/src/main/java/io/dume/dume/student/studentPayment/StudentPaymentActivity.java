package io.dume.dume.student.studentPayment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.common.bkash_transection.BkashTransectionActivity;
import io.dume.dume.model.DumeModel;
import io.dume.dume.student.homePage.HomePageModel;
import io.dume.dume.student.homePage.HomePagePresenter;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerData;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.studentHelp.StudentHelpActivity;
import io.dume.dume.student.studentPayment.adapterAndData.ObligationAndClaimAdapter;
import io.dume.dume.student.studentPayment.adapterAndData.ObligationAndClaimData;
import io.dume.dume.student.studentPayment.adapterAndData.PaymentAdapter;
import io.dume.dume.student.studentPayment.adapterAndData.PaymentData;
import io.dume.dume.student.studentPayment.adapterAndData.PromotionAdapter;
import io.dume.dume.student.studentPayment.adapterAndData.PaymentHistory;
import io.dume.dume.student.studentPayment.adapterAndData.PaymentHistoryAdapter;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.util.DumeUtils.configAppbarTittle;
import static io.dume.dume.util.DumeUtils.configureAppbar;
import static io.dume.dume.util.DumeUtils.showKeyboard;

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
    private FrameLayout content;
    private TextView dueAmount;
    private TextView dueAmountTile;
    private TextView discountTitle;
    private TextView discountAmount;
    private TextView promotionNumberTV;
    private String retriveAction;

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
                    case "Cash Payment":
                        onCashClicked();
                        break;
                    case "Bkash Transaction ID":
                        startActivity(new Intent(getApplicationContext(), BkashTransectionActivity.class));
                        break;
                    case "Bkash OTP":
                        flush("Payment method is under development...");
                        break;
                    case "NexusPay/Rocket":
                        flush("Payment method is under development...");
                        break;
                    case "Card Transfer":
                        flush("Payment method is under development...");
                        break;
                }
            }
        };
        paymentRecycleView.setAdapter(paymentAdapter);
        paymentRecycleView.setLayoutManager(new LinearLayoutManager(this));
        retriveAction = getIntent().getAction();
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

    public void flush(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null) {
            v.setGravity(Gravity.CENTER);
        }
        toast.show();

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
        dueAmountTile = findViewById(R.id.reportTitle);
        dueAmount = findViewById(R.id.afterDiscount);
        discountTitle = findViewById(R.id.reportTitle_one);
        discountAmount = findViewById(R.id.afterDiscount_one);
        promotionNumberTV = findViewById(R.id.promotion_value_text);

    }

    @Override
    public void initStudentPayment() {
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
        promotionNumberTV.setText("" + appliedPromoList.size());
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
    public void configurePaymentInformation() {


        if (TeacherDataStore.getInstance().getDocumentSnapshot()==null) {
            return;
        }
        Map<String, Object> payments = (Map<String, Object>) TeacherDataStore.getInstance().getDocumentSnapshot().get("payments");
        int discount = 0;
        if (payments != null) {
            Boolean hd = (Boolean) payments.get("have_discount");
            String dc = (String) payments.get("discount");
            if (hd != null) {

            }
            if (dc != null) {
                discount = Integer.parseInt(dc);
            }
            dueAmount.setText(Integer.parseInt(payments.get("obligation_amount").toString()) < 0 ? Math.abs(Integer.parseInt(payments.get("obligation_amount").toString())) + " ৳" : payments.get("obligation_amount").toString() + " ৳");
            dueAmountTile.setText(Integer.parseInt(payments.get("obligation_amount").toString()) < 0 ? "Advance Paid" : "Due Amount");
            discountAmount.setText(discount + " ৳");
            discountTitle.setText("Discount on Pay");


        }
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
        if (mainLayoutContent.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        } else {
            configAppbarTittle(StudentPaymentActivity.this, "Payment");
            mainLayoutContent.setVisibility(View.VISIBLE);
            secondaryHideAbleLayout.setVisibility(View.GONE);
            content.setVisibility(View.GONE);
        }
        //super.onBackPressed();
    }

    public List<PaymentData> getFinalData() {
        List<PaymentData> data = new ArrayList<>();
        int[] imageIcons = {
                R.drawable.ic_bkash_icon,
                R.drawable.ic_bkash_icon,
                R.drawable.ic_nexus_pay_icon,
                R.drawable.ic_pay_credit_bank
        };
        int[] paymentDefaultValue = {1, 0, 0, 0};

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
        private Button skipBtn;
        private Button submitBtn;
        private TextView limit;
        private TextInputLayout textInputLayout;
        private Map<String, Object> documentSnapshot;
        private Context context;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            this.context = context;
        }

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
            skipBtn = rootView.findViewById(R.id.skip_btn);
            submitBtn = rootView.findViewById(R.id.submit_btn);
            limit = rootView.findViewById(R.id.limitTV);
            skipBtn.setText("How to use...");
            textInputLayout = (TextInputLayout) rootView.findViewById(R.id.input_layout_firstname);
            textInputLayout.setHint("Promo code");

            skipBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StudentHelpActivity.class);
                    intent.setAction("how_to_use");
                    startActivity(intent);
                }
            });
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setEnabled(false);
                    myMainActivity.showProgress();
                    switch (myMainActivity.retriveAction) {
                        case DumeUtils.TEACHER:
                            documentSnapshot = TeacherDataStore.getInstance().getDocumentSnapshot();
                            break;
                        case DumeUtils.STUDENT:
                            documentSnapshot = SearchDataStore.getInstance().getDocumentSnapshot();
                            break;
                        case DumeUtils.BOOTCAMP:
                            documentSnapshot = TeacherDataStore.getInstance().getDocumentSnapshot();
                            break;
                    }
                    ArrayList<String> available_promo = (ArrayList<String>) documentSnapshot.get("available_promo");
                    ArrayList<String> applied_promo = (ArrayList<String>) documentSnapshot.get("applied_promo");
                    final String writtenCode = queryTextView.getText().toString();

                    if (writtenCode.length() <= 0) {
                        queryTextView.setError("Please enter a promo code...");
                        view.setEnabled(true);
                        myMainActivity.hideProgress();
                    } else if (applied_promo != null && applied_promo.contains(writtenCode)) {
                        queryTextView.setText("");
                        Toast.makeText(myMainActivity, "Promo already applied...", Toast.LENGTH_SHORT).show();
                        view.setEnabled(true);
                        myMainActivity.hideProgress();
                    } else if (available_promo != null && available_promo.contains(writtenCode)) {
                        HomePageModel homePageModel = new HomePageModel((Activity) context, context);

                        homePageModel.getPromo(writtenCode, new TeacherContract.Model.Listener<HomePageRecyclerData>() {
                            @Override
                            public void onSuccess(HomePageRecyclerData list) {
                                homePageModel.applyPromo(list, writtenCode, Google.getInstance().getAccountMajor(), new TeacherContract.Model.Listener<String>() {
                                    @Override
                                    public void onSuccess(String list) {
                                        Toast.makeText(context, list, Toast.LENGTH_SHORT).show();
                                        view.setEnabled(true);
                                        myMainActivity.hideProgress();
                                    }

                                    @Override
                                    public void onError(String msg) {
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                        view.setEnabled(true);
                                        myMainActivity.hideProgress();
                                    }
                                });
                            }
                            @Override
                            public void onError(String msg) {
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                view.setEnabled(true);
                                myMainActivity.hideProgress();
                            }
                        });
                    } else {
                        queryTextView.setError("Promo not valid for you...");
                        Toast.makeText(myMainActivity, "Promo not valid for you...", Toast.LENGTH_SHORT).show();
                        view.setEnabled(true);
                        myMainActivity.hideProgress();
                    }
                }
            });

            queryTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        queryTextView.setHint("Enter your promo code");
                        limit.setTextColor(getResources().getColor(R.color.loader_color_one));
                        showKeyboard(myMainActivity);
                    } else {
                        queryTextView.setHint("");
                    }
                }
            });
            queryTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.equals("")) {
                        submitBtn.setEnabled(true);
                    } else {
                        submitBtn.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().equals("")) {
                        submitBtn.setEnabled(false);
                    }
                    if (s.toString().length() >= 120) {
                        limit.setText(s.toString().length() + "/120");
                        limit.setTextColor(getResources().getColor(R.color.light_red));
                    } else if (s.toString().length() >= 1) {
                        limit.setText(s.toString().length() + "/120");
                        limit.setTextColor(getResources().getColor(R.color.loader_color_one));
                    } else {
                        limit.setText(s.toString().length() + "/120");
                        limit.setTextColor(Color.BLACK);
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
                homePageRecyclerData.setPackageName(promoMap.get("packageName").toString());
                Long max_tution_count = (Long) promoMap.get("max_tution_count");
                homePageRecyclerData.setMax_tution_count(max_tution_count.intValue());
                Long max_dicount_percentage = (Long) promoMap.get("max_dicount_percentage");
                homePageRecyclerData.setMax_dicount_percentage((Integer) max_dicount_percentage.intValue());
                Long max_discount_credit = (Long) promoMap.get("max_discount_credit");
                homePageRecyclerData.setMax_discount_credit((Integer) max_discount_credit.intValue());
                homePageRecyclerData.setExpirity((Date) promoMap.get("expirity"));
                homePageRecyclerData.setCriteria((Map<String, Object>) promoMap.get("criteria"));
                homePageRecyclerData.setPromo_for(promoMap.get("promo_for").toString());
                Object promo_image = promoMap.get("promo_image");
                if (promo_image == null) {
                    homePageRecyclerData.setPromo_image(null);
                } else {
                    homePageRecyclerData.setPromo_image(promo_image.toString());
                }
                homePageRecyclerData.setSub_description(promoMap.get("sub_description").toString());
                homePageRecyclerData.setPromo_code(promoMap.get("promo_code").toString());
                homePageRecyclerData.setExpired((Boolean) promoMap.get("expired"));
                promotionAdapter.addPromoToList(homePageRecyclerData);
            }
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
        private Context context;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public void onAttach(Context context) {
            this.context = context;
            super.onAttach(context);

        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            myMainActivity = (StudentPaymentActivity) getActivity();
            View rootView = inflater.inflate(R.layout.p_custom_recycler_row, container, false);
            pCustomRecyclerView = rootView.findViewById(R.id.p_recycler_view);

            //menual one
            StudentPaymentActivity activity = (StudentPaymentActivity) context;
            activity.showProgress();

            new DumeModel(context).getPaymentHistory(FirebaseAuth.getInstance().getUid(), new TeacherContract.Model.Listener<List<PaymentHistory>>() {
                @Override
                public void onSuccess(List<PaymentHistory> histories) {
                    activity.hideProgress();
                    if (histories.size() > 0) {
                        rootView.findViewById(R.id.hostRelativeLayout).setVisibility(View.VISIBLE);
                        PaymentHistoryAdapter historyAdapter = new PaymentHistoryAdapter(myMainActivity, histories);
                        pCustomRecyclerView.setAdapter(historyAdapter);
                    } else {
                        rootView.findViewById(R.id.notFoundTV).setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onError(String msg) {
                    activity.hideProgress();
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                }
            });


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
        private Context context;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            this.context = context;
        }

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
