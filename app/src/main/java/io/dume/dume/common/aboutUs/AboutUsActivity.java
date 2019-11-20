package io.dume.dume.common.aboutUs;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;

import io.dume.dume.R;
import io.dume.dume.student.pojo.BaseAppCompatActivity;

import static io.dume.dume.util.DumeUtils.configureAppbar;

public class AboutUsActivity extends BaseAppCompatActivity implements AboutUsContact.View {

    private AboutUsContact.Presenter mPresenter;
    private static final String TAG = "AboutUsActivity";
    private static final int fromFlag = 13;
    private WebView aboutView;
    private ProgressBar progressBar;
    private Sprite doubleBounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common2_activity_about_us);
        setActivityContext(this, fromFlag);
        mPresenter = new AboutUsPresenter(this, new AboutUsModel());
        mPresenter.aboutUsEnqueue();
        configureAppbar(this, "About us", true);
        findLoadView();
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void findView() {
        aboutView = findViewById(R.id.aboutWebView);
        progressBar = findViewById(R.id.progress_bar);
        doubleBounce = new ChasingDots();
        doubleBounce.setColor(getResources().getColor(R.color.inbox_active_color));
        progressBar.setIndeterminateDrawable(doubleBounce);
    }

    @Override
    public void initAboutUs() {
        aboutView.getSettings().setJavaScriptEnabled(true);
        aboutView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }


        });
        aboutView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgress();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideProgress();
                progressBar.setVisibility(View.GONE);
                aboutView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(AboutUsActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        aboutView.loadUrl("https://dume-2d063.firebaseapp.com/about");
    }

    @Override
    public void configAboutUs() {

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

}
