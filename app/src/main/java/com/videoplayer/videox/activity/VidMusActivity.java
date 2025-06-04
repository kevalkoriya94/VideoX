package com.videoplayer.videox.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.NativeAdLayout;
import com.videoplayer.videox.R;
import com.videoplayer.videox.fragment.MedManFrag;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;


public class VidMusActivity extends AppCompatActivity {
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_music);
        getSupportFragmentManager().beginTransaction().add(R.id.frame, MedManFrag.newInstance(getIntent().getStringExtra("TYPE")), "MediaManager").commit();
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidMusActivity$$ExternalSyntheticLambda0
            @Override
            public final void onClick(View view) {
                VidMusActivity.this.m861lambda$onCreate$0$comvideoplayervideoxactivityVidMusActivity(view);
            }
        });
        AdmobAdsHelper.smallnativeAds(this, (ViewGroup) findViewById(R.id.layout_ads), (TextView) findViewById(R.id.adspace), (NativeAdLayout) findViewById(R.id.native_banner_ad_container), 1);
    }

    /* renamed from: lambda$onCreate$0$com-videoplayer-videox-activity-VidMusActivity, reason: not valid java name */
    void m861lambda$onCreate$0$comvideoplayervideoxactivityVidMusActivity(View view) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        AdmobAdsHelper.ShowFullAds(this, true);
        finish();
    }
}
