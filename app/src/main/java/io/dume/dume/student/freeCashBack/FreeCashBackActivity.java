package io.dume.dume.student.freeCashBack;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;

import static io.dume.dume.util.DumeUtils.configureAppbar;

public class FreeCashBackActivity extends CustomStuAppCompatActivity implements FreeCashBackContact.View {

    private FreeCashBackContact.Presenter mPresenter;
    private static final int fromFlag = 15;
    private static final String TAG = "FreeCashBackActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu14_activity_free_cash_back);
        setActivityContext(this, fromFlag);
        mPresenter = new FreeCashBackPresenter(this, new FreeCashBackModel());
        mPresenter.freeCashBackEnqueue();
        configureAppbar(this, "Free cash-back");


    }

    @Override
    public void findView() {

    }

    @Override
    public void initFreeCashBack() {

    }

    @Override
    public void configFreeCashBack() {

    }
}
