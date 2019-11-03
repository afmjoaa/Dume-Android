package io.dume.dume.student.studentHelp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;

import java.util.ArrayList;
import java.util.List;

import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.common.appInfoActivity.AppInfoActivity;
import io.dume.dume.common.privacyPolicy.PrivacyPolicyActivity;
import io.dume.dume.model.DumeModel;
import io.dume.dume.student.common.SettingData;
import io.dume.dume.student.common.SettingsAdapter;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.util.AlertMsgDialogue;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.util.DumeUtils.configAppbarTittle;
import static io.dume.dume.util.DumeUtils.configureAppbar;
import static io.dume.dume.util.DumeUtils.showKeyboard;

public class StudentHelpActivity extends CustomStuAppCompatActivity implements StudentHelpContract.View {

    private StudentHelpContract.Presenter mPresenter;
    private static final int fromFlag = 12;
    private String[] helpNameArr;
    private RecyclerView helpRecyclerView;
    private AppBarLayout appBarLayout;
    private View helpContent;
    private static String loadingURL;
    private RelativeLayout loadingRl;
    private ProgressBar progressBar;
    private Sprite doubleBounce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stunav_activity3_student_help);
        setActivityContext(this, fromFlag);
        findLoadView();
        mPresenter = new StudentHelpPresenter(this, new StudentHelpModel());
        mPresenter.studentHelpEnqueue();
        configureAppbar(this, "Help");

        loadingURL = "https://xume.xyz/hows";
        //setting the recycler view
        SettingsAdapter helpAdapter = new SettingsAdapter(this, getFinalData()) {
            @Override
            protected void OnButtonClicked(View v, int position) {
                switch (position) {
                    case 0:
                        generalFrag(position, "https://xume.xyz/whatsnew");
                        break;
                    case 1:
                        String url = "https://xume.xyz/home";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;
                    case 2:
                        generalFrag(position, "https://xume.xyz/hows");
                        break;
                    case 3:
                        faqFrag(position);
                        break;
                    case 4:
                        helpContent.setVisibility(View.GONE);
                        configAppbarTittle(StudentHelpActivity.this, helpNameArr[position]);
                        appBarLayout.setExpanded(false);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, new ContactUsFragment()).commit();
                        break;
                    case 5:
                        updateAppCalled();
                        break;
                    case 6:
                        startActivity(new Intent(StudentHelpActivity.this, PrivacyPolicyActivity.class).setAction("fromHelp"));
                        break;
                    case 7:
                        startActivity(new Intent(StudentHelpActivity.this, AppInfoActivity.class).setAction("fromHelp"));
                        break;
                }

            }
        };
        helpRecyclerView.setAdapter(helpAdapter);
        helpRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String action = getIntent().getAction();
        if(action!=null){
            if(action.equals("how_to_use")){
                generalFrag(2, "https://xume.xyz/hows");
            } else if (action.equals("whats_new")) {
                generalFrag(0, "https://xume.xyz/whatsnew");
            } else if (action.equals("faq")) {
                faqFrag(3);
            }
        }

    }

    private void faqFrag(int position) {
        helpContent.setVisibility(View.GONE);
        configAppbarTittle(StudentHelpActivity.this, helpNameArr[position]);
        appBarLayout.setExpanded(false);
        showProgress();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new FAQFragment()).commit();
    }

    private void generalFrag(int title, String s) {
        helpContent.setVisibility(View.GONE);
        configAppbarTittle(StudentHelpActivity.this, helpNameArr[title]);
        appBarLayout.setExpanded(false);
        showProgress();
        loadingURL = s;
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new GeneralUrlLoaderFrag()).commit();
    }

    private void updateAppCalled() {

        Bundle Uargs = new Bundle();
        Uargs.putString("msg", "Sorry! No update available.");
        AlertMsgDialogue updateAlertDialogue = new AlertMsgDialogue();
        updateAlertDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(StudentHelpActivity.this, "Ok", Toast.LENGTH_SHORT).show();
            }
        }, "Ok");
        updateAlertDialogue.setArguments(Uargs);
        updateAlertDialogue.show(getSupportFragmentManager(), "updateAlertDialogue");
    }

    @Override
    public void findView() {
        helpNameArr = getResources().getStringArray(R.array.helpHeader);
        helpRecyclerView = findViewById(R.id.help_recycler);
        appBarLayout = findViewById(R.id.app_bar);
        helpContent = findViewById(R.id.help_content);
        loadingRl = findViewById(R.id.loadingPanel);
        progressBar = findViewById(R.id.progress_bar);
        doubleBounce = new ChasingDots();
        doubleBounce.setColor(getResources().getColor(R.color.inbox_active_color));
        progressBar.setIndeterminateDrawable(doubleBounce);
    }

    @Override
    public void initStudentHelp() {

    }

    @Override
    public void configStudentHelp() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public List<SettingData> getFinalData() {
        List<SettingData> data = new ArrayList<>();
        int[] imageIcons = {
                R.drawable.ic_help_whats_new,
                R.drawable.ic_dume_web,
                R.drawable.ic_help_feature,
                R.drawable.ic_help_faq,
                R.drawable.ic_help_contact_us,
                R.drawable.ic_sync,
                R.drawable.ic_help_privacy_policy,
                R.drawable.ic_help_app_info
        };

        for (int i = 0; i < helpNameArr.length && i < imageIcons.length; i++) {
            SettingData current = new SettingData();
            current.settingName = helpNameArr[i];
            current.settingIcon = imageIcons[i];
            data.add(current);
        }
        return data;
    }


    //testing the contact up
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ContactUsFragment extends Fragment {

        private StudentHelpActivity myMainActivity;
        private AutoCompleteTextView queryTextView;
        private Context context;
        private DumeModel dumeModel;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
            dumeModel = new DumeModel(context);
        }

        @Override
        public void onAttach(Context context) {
            this.context = context;
            super.onAttach(context);

        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            myMainActivity = (StudentHelpActivity) getActivity();
            View rootView = inflater.inflate(R.layout.custom_contact_up_fragment, container, false);
            queryTextView = rootView.findViewById(R.id.feedback_textview);
            Button submitBTN = rootView.findViewById(R.id.submit_btn);
            Button readFaqBTN = rootView.findViewById(R.id.skip_btn);
            TextView limit = rootView.findViewById(R.id.limitTV);
            queryTextView.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    queryTextView.setHint("Please describe your problem");
                    limit.setTextColor(context.getResources().getColor(R.color.loader_color_one));
                    showKeyboard((Activity) context);
                }
            });

            queryTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString().length() >= 200) {
                        limit.setText(editable.toString().length() + "/200");
                        //limit.setTextColor(Color.RED);
                        limit.setTextColor(context.getResources().getColor(R.color.light_red));
                    } else if(editable.toString().length() >= 1){
                        limit.setText(editable.toString().length() + "/200");
                        limit.setTextColor(context.getResources().getColor(R.color.loader_color_one));
                    }else {
                        limit.setText(editable.toString().length() + "/200");
                        limit.setTextColor(Color.BLACK);
                    }
                }
            });
            submitBTN.setOnClickListener(view -> {
                submitBTN.setEnabled(false);
                myMainActivity.showProgress();
                if (queryTextView.getText() != null && !queryTextView.getText().toString().equals("")) {
                    dumeModel.reportIssue(Google.getInstance().getAccountMajor().equals(DumeUtils.STUDENT) ? SearchDataStore.getInstance().getUserMail() : TeacherDataStore.getInstance().gettUserMail(), queryTextView.getText().toString(), new TeacherContract.Model.Listener<Void>() {
                        @Override
                        public void onSuccess(Void list) {
                            submitBTN.setEnabled(true);
                            String email = Google.getInstance().getAccountMajor().equals(DumeUtils.STUDENT) ? SearchDataStore.getInstance().getUserMail() : TeacherDataStore.getInstance().gettUserMail();
                            Toast.makeText(myMainActivity, "Your message is sent to Dume authority. You will be notified by your email : " + email, Toast.LENGTH_LONG).show();
                            myMainActivity.hideProgress();
                        }

                        @Override
                        public void onError(String msg) {
                            myMainActivity.hideProgress();
                            Toast.makeText(myMainActivity, msg, Toast.LENGTH_SHORT).show();
                            submitBTN.setEnabled(true);
                        }
                    });
                }else{
                    submitBTN.setEnabled(true);
                    myMainActivity.hideProgress();
                    queryTextView.setError("Please fill in your query...");
                }
                queryTextView.getText().clear();
            });
            readFaqBTN.setOnClickListener(view -> {
                Intent intent = new Intent(context, StudentHelpActivity.class);
                intent.setAction("faq");
                startActivity(intent);
            });
            return rootView;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), StudentHelpActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    //testing the faq here
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class FAQFragment extends Fragment {

        private StudentHelpActivity myMainActivity;
        private WebView webView;
        private Context context;
        private StudentHelpActivity activity;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            myMainActivity = (StudentHelpActivity) getActivity();
            View rootView = inflater.inflate(R.layout.custom_faq_fragment, container, false);
            webView = rootView.findViewById(R.id.activity_main_webview);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    activity.showProgress();
                    activity.progressBar.setVisibility(View.VISIBLE);
                    activity.loadingRl.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    activity.hideProgress();
                    activity.progressBar.setVisibility(View.GONE);
                    activity.loadingRl.setVisibility(View.GONE);
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
            webView.loadUrl("https://xume.xyz/faq");
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            return rootView;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    startActivity(new Intent(getActivity(), StudentHelpActivity.class));
                }
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            this.context = context;
            this.activity = (StudentHelpActivity) context;
        }
    }

    public static class GeneralUrlLoaderFrag extends Fragment {
        private Context context;
        private StudentHelpActivity activity;
        private StudentHelpActivity myMainActivity;
        private WebView webView;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            myMainActivity = (StudentHelpActivity) getActivity();
            View rootView = inflater.inflate(R.layout.custom_faq_fragment, container, false);
            webView = rootView.findViewById(R.id.activity_main_webview);
            webView.setWebViewClient(new WebViewClient());
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    activity.showProgress();
                    activity.progressBar.setVisibility(View.VISIBLE);
                    activity.loadingRl.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    activity.hideProgress();
                    activity.progressBar.setVisibility(View.GONE);
                    activity.loadingRl.setVisibility(View.GONE);
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
            webView.loadUrl(loadingURL);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            return rootView;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    startActivity(new Intent(getActivity(), StudentHelpActivity.class));
                }
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onAttach(Context context) {
            this.context = context;
            this.activity = (StudentHelpActivity) context;
            super.onAttach(context);
        }
    }

}
