package io.dume.dume.common.contactActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.util.DumeUtils;

public class ContactActivity extends CustomStuAppCompatActivity implements ContactActivityContact.View {

    private ContactActivityContact.Presenter mPresenter;
    private static final int fromFlag = 31;
    private RecyclerView contactRecyclerView;
    private HorizontalLoadView loadView;
    private String retriveAction;
    private Boolean isFromMsg;
    private LinearLayout noDataBlock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common5_activity_contact);
        setActivityContext(this, fromFlag);
        if (getIntent().getAction() != null) {
            retriveAction = getIntent().getAction();
        }
        isFromMsg = retriveAction.equals("from_message");
        mPresenter = new ContactActivityPresenter(this, new ContactActivityModel(this));
        mPresenter.contactActivityEnqueue();
        DumeUtils.configureAppbar(this, "Select contact", true);
        findLoadView();
        //testing the adapter here
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void findView() {
        contactRecyclerView = findViewById(R.id.contact_recycler_view);
        loadView = findViewById(R.id.loadView);
        noDataBlock = findViewById(R.id.no_data_block);
    }

    @Override
    public void initContactActivity() {

    }

    @Override
    public void configContactActivity() {

    }


    @Override
    public void flush(String toFlush) {
        Toast toast = Toast.makeText(this, toFlush, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    @Override
    public void loadRV(List<ContactData> dataList) {
        ContactDataAdapter contactRecyAda = new ContactDataAdapter(this, dataList, isFromMsg);
        contactRecyclerView.setAdapter(contactRecyAda);
        if (dataList.size() <= 0){
            noDataBlock.setVisibility(View.VISIBLE);
        }else {
            noDataBlock.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_help:
                //Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
