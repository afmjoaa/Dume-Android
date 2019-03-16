package io.dume.dume.common.aboutUs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;

import static io.dume.dume.util.DumeUtils.configureAppbar;

public class AboutUsActivity extends CustomStuAppCompatActivity implements AboutUsContact.View {

    private AboutUsContact.Presenter mPresenter;
    private static final String TAG = "AboutUsActivity";
    private static final int fromFlag = 13;
    private WebView aboutView;


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
        aboutView.getSettings().setJavaScriptEnabled(true);
        MyJavaScriptInterface myinterface = new MyJavaScriptInterface();
        aboutView.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");
        aboutView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                Log.w(TAG, "onPageFinished: ");


            }


            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }
        });
        aboutView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        aboutView.loadUrl("https://www.google.com/");
    }

    @Override
    public void initAboutUs() {

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

    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        public MyJavaScriptInterface() {

        }

        public void showHTML(String html) {
            Log.w(TAG, "showHTML: " + html);
            new AlertDialog.Builder(ctx).setTitle("HTML").setMessage(html)
                    .setPositiveButton(android.R.string.ok, null).setCancelable(false).create().show();
        }

    }
}
