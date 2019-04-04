package io.dume.dume.common.bkash_transection;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

import io.dume.dume.R;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.studentPayment.StudentPaymentActivity;

import static io.dume.dume.util.DumeUtils.configAppbarTittle;
import static io.dume.dume.util.DumeUtils.configureAppbar;
import static io.dume.dume.util.DumeUtils.showKeyboard;

public class BkashTransectionActivity extends CustomStuAppCompatActivity implements BkashTransContact.View {

    private Button submitBTN;
    private EditText transET;
    private BkashTransContact.Presenter presenter;
    private EditText amountET;
    private RelativeLayout transContainer;
    private RelativeLayout onProgress;
    private static final String TAG = "BkashTransectionActivit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkash_transection);
        setActivityContext(this, 254585545);
        findLoadView();
        configureAppbar(this, "Bkash Transaction With ID");
        presenter = new BkashTransectionPresenter(this, new BkashTransModel(this));
        presenter.enqueue();
        submitBTN.setOnClickListener(v -> {
            submitBTN.setEnabled(false);
            if (!transET.getText().toString().equals("") && !amountET.getText().toString().equals("")) {
                Map<String, Object> data = new HashMap<>();
                data.put("transection_id", transET.getText().toString());
                data.put("transection_date", FieldValue.serverTimestamp());
                data.put("status", "Pending");
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Toast.makeText(this, "Session Expired, Please Login.", Toast.LENGTH_SHORT).show();
                    return;
                }
                data.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                data.put("payment_method", "bkash_transection");
                data.put("amount", amountET.getText().toString());
                presenter.handleTransection(data);
            } else if(transET.getText().toString().equals("")) {
                transET.setError("Enter Transection Id");
            }else{
                amountET.setError("Enter Bkashed Amount");
            }
        });
    }

    @Override
    public void init() {
        submitBTN = findViewById(R.id.submitTransID);
        transET = findViewById(R.id.trans_id);
        amountET = findViewById(R.id.amount);
        transContainer = findViewById(R.id.transContainer);
        onProgress = findViewById(R.id.inprogress);
        transET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    transET.setHint("Enter Bksah Transaction Id");
                    showKeyboard(BkashTransectionActivity.this);
                }else{
                    transET.setHint("");
                }
            }
        });
        amountET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    amountET.setHint("Enter Amount in BDT");
                    showKeyboard(BkashTransectionActivity.this);
                }else{
                    amountET.setHint("");

                }
            }
        });
    }

    @Override
    public void flush(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVerificaitonProgress(String msg) {
        transContainer.setVisibility(View.GONE);
        onProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void load() {
        showProgress();
    }

    @Override
    public void stopLoad() {
        hideProgress();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void setEnable(){
        submitBTN.setEnabled(true);
    }

}
