package io.dume.dume.common.bkash_transection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firestore.v1beta1.DocumentTransform;
import com.google.firestore.v1beta1.FirestoreGrpc;

import java.util.HashMap;
import java.util.Map;

import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;

public class BkashTransectionActivity extends CustomStuAppCompatActivity implements BkashTransContact.View {

    private Button submitBTN;
    private EditText transET;

    private BkashTransContact.Presenter presenter;
    private EditText amountET;
    private LinearLayout transContainer;
    private RelativeLayout onProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkash_transection);
        setActivityContext(this, 254585545);
        findLoadView();
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
            } else {
                transET.setError("Enter Transection Id");
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


    }

    @Override
    public void flush(String msg) {
        submitBTN.setEnabled(true);
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


}
