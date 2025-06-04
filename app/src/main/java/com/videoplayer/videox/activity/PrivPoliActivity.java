package com.videoplayer.videox.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.videoplayer.videox.R;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;


public class PrivPoliActivity extends AppCompatActivity {
    private Context mContext;
    String policy;
    ProgressBar progressBar;

    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        this.mContext = this;
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressBar = progressBar;
        progressBar.setVisibility(View.VISIBLE);
        WebView webView = (WebView) findViewById(R.id.webView);
        TextView textView = (TextView) findViewById(R.id.tv_title);
        this.policy = "https://privacypolicydripphotoeditor.blogspot.com/2022/12/privacy-policy.html";
        textView.setText(R.string.privacy_policy);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl(this.policy);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.PrivPoliActivity$$ExternalSyntheticLambda0
            @Override
            public final void onClick(View view) {
                PrivPoliActivity.this.m519x2bf43f65(view);
            }
        });
    }

    /* renamed from: lambda$onCreate$0$com-videoplayer-videox-activity-PrivPoliActivity */
    void m519x2bf43f65(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        AdmobAdsHelper.ShowFullAds(this.mContext, true);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class MyWebViewClient extends WebViewClient {
        private MyWebViewClient() {
        }

        @Override // android.webkit.WebViewClient
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            PrivPoliActivity.this.progressBar.setVisibility(View.GONE);
        }
    }
}
