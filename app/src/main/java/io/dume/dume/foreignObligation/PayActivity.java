package io.dume.dume.foreignObligation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.dume.dume.R;
import io.dume.dume.auth.auth.AuthActivity;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.pojo.BaseAppCompatActivity;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.RadioBtnDialogue;

public class PayActivity extends BaseAppCompatActivity implements PayContact.View {
    private static final String TAG = "PayActivity";
    protected static int fromFlag = 151;
    private PayContact.Presenter mPresenter;
    private TextView opsTextView;
    private Button signOut;
    private Button recheckObligation;
    private Button priviousLogin;
    private Intent fromIntent;
    private String fromIntentAction;
    private DocumentSnapshot documentSnapSHot;
    private String finalOutPut;
    private String[] allNumber;
    private List<String> newAllNumber;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        setActivityContext(this, fromFlag);
        findLoadView();
        mPresenter = new PayPresenter(this, this, new PayModel(this));
        mPresenter.payActivityEnqueue();
        testingCustomDialogue();
    }

    @Override
    public void findView() {
        opsTextView = findViewById(R.id.opps_textview);
        priviousLogin = findViewById(R.id.previous_login);
        recheckObligation = findViewById(R.id.recheck_obligation);
        signOut = findViewById(R.id.sign_out);
        newAllNumber = new ArrayList<>();
    }

    @Override
    public void initPayActivity() {
        DumeUtils.configureAppbar(this, "Foreign Obligation", true);
    }

    @Override
    public void configPayActivity() {

    }

    @Override
    public void signOutAndLoginPrevious() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            Intent gotoAuthActivity = new Intent(this, AuthActivity.class);
            newAllNumber.clear();
            Map<String, Map<String, Object>> obligatedUser = (Map<String, Map<String, Object>>) documentSnapSHot.get("obligated_user");
            if (obligatedUser.size() > 0) {
                for (Map.Entry<String, Map<String, Object>> entry : obligatedUser.entrySet()) {
                    newAllNumber.add( (String) entry.getValue().get("phone_number"));
                }

                if (obligatedUser.size() == 1) {
                    gotoAuthActivity.putExtra("phone_number", newAllNumber.get(0));
                    startActivity(gotoAuthActivity.setAction("previous_login"));
                    finish();
                } else {
                    //selectOne
                    selectOneNum((String[]) newAllNumber.toArray());
                }
            }
        }
    }

    @Override
    public void signOut() {
        onSignOut();
    }

    @Override
    public void flush(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();

    }

    @Override
    public void noObligationFound() {
        flush("Thanks. Your Obligation due amount is cleared...");
        String accountMajor = documentSnapSHot.getString("account_major");
        switch (accountMajor) {
            case DumeUtils.STUDENT:
                startActivity(new Intent(this, HomePageActivity.class));
                finish();
                break;
            case DumeUtils.TEACHER:
                startActivity(new Intent(this, TeacherActivtiy.class));
                finish();
                break;
            case DumeUtils.BOOTCAMP:
                startActivity(new Intent(this, TeacherActivtiy.class));
                finish();
                break;
        }
    }

    @Override
    public void obligationFound() {
        flush("Sorry ! Previous account obligation still not Paid ...");
    }

    @Override
    public void setDocumentSnapshot(DocumentSnapshot documentSnapshot) {
        this.documentSnapSHot = documentSnapshot;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_grabing_info, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
        } else if (id == R.id.action_help) {
            //show the dialogue
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onPayActivityClicked(View view) {
        mPresenter.onPayActivityViewIntracted(view);
    }

    public void selectOneNum(String[] allNumber) {
        finalOutPut = "";
        Bundle pRargs = new Bundle();
        String finalTitle = "Select previous account phone number";
        pRargs.putString("title", finalTitle);
        pRargs.putStringArray("radioOptions", allNumber);
        RadioBtnDialogue selectOneNumDialogue = new RadioBtnDialogue();
        selectOneNumDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finalOutPut = allNumber[i];
            }
        });
        selectOneNumDialogue.setSelectListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent gotoAuthActivity = new Intent(PayActivity.this, AuthActivity.class);
                gotoAuthActivity.putExtra("phone_number", finalOutPut);
                startActivity(gotoAuthActivity.setAction("previous_login"));
                finish();
            }
        });
        selectOneNumDialogue.setCancelOnOutPress(false);
        selectOneNumDialogue.setArguments(pRargs);
        selectOneNumDialogue.show(getSupportFragmentManager(), "selectOneNumDialogue");
        finalOutPut = allNumber[0];
    }

    public void testingCustomDialogue() {
        // custom dialog
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_obligation_dialogue);

        //all find view here
        Button dismissBtn = dialog.findViewById(R.id.dismiss_btn);
        TextView dialogText = dialog.findViewById(R.id.dialog_text);
        dialogText.setGravity(Gravity.CENTER);
        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    //when obligation will be cleared all the obligated_user map will also be cleared
}
