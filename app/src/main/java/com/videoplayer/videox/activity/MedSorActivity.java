package com.videoplayer.videox.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.NativeAdLayout;
import com.videoplayer.videox.R;
import com.videoplayer.videox.fragment.MedFilFrag;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;


public class MedSorActivity extends AppCompatActivity {
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_sort);
        getSupportFragmentManager().beginTransaction().add(R.id.frame, MedFilFrag.newInstance(), "MediaFiles").commit();
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.MedSorActivity$$ExternalSyntheticLambda0
            @Override
            public final void onClick(View view) {
                MedSorActivity.this.m858lambda$onCreate$0$comvideoplayervideoxactivityMedSorActivity(view);
            }
        });
        AdmobAdsHelper.smallnativeAds(this, (ViewGroup) findViewById(R.id.layout_ads), (TextView) findViewById(R.id.adspace), (NativeAdLayout) findViewById(R.id.native_banner_ad_container), 1);
    }

    /* renamed from: lambda$onCreate$0$com-videoplayer-videox-activity-MedSorActivity, reason: not valid java name */
    void m858lambda$onCreate$0$comvideoplayervideoxactivityMedSorActivity(View view) {
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
