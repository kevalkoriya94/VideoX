package com.videoplayer.videox.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.NativeAdLayout;
import com.videoplayer.videox.R;
import com.videoplayer.videox.fragment.HistFrag;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;


public class HistActivity extends AppCompatActivity {
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        String stringExtra = getIntent().getStringExtra("TYPE");
        TextView textView = (TextView) findViewById(R.id.tv_title);
        if (stringExtra.equals("watched")) {
            textView.setText(R.string.watched_media);
        } else {
            textView.setText(R.string.history);
        }
        getSupportFragmentManager().beginTransaction().add(R.id.frame, HistFrag.newInstance(), "history").commit();
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.HistActivity$$ExternalSyntheticLambda0
            @Override
            public final void onClick(View view) {
                HistActivity.this.m857lambda$onCreate$0$comvideoplayervideoxactivityHistActivity(view);
            }
        });
        AdmobAdsHelper.smallnativeAds(this, (ViewGroup) findViewById(R.id.layout_ads), (TextView) findViewById(R.id.adspace), (NativeAdLayout) findViewById(R.id.native_banner_ad_container), 1);
    }

    /* renamed from: lambda$onCreate$0$com-videoplayer-videox-activity-HistActivity, reason: not valid java name */
    void m857lambda$onCreate$0$comvideoplayervideoxactivityHistActivity(View view) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onBackClick() {
        AdmobAdsHelper.ShowFullAds(this, true);
        finish();
    }

    @Override
    public void onBackPressed() {
        onBackClick();
    }
}
