package com.videoplayer.videox.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.mus.MusInfAdapter;
import com.videoplayer.videox.fragment.vid.p015df.SeaVidDiaFrag;
import com.videoplayer.videox.fragment.vid.tab.VideAudConvFrag;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;

import java.util.List;


public class AudFolActivity extends AppCompatActivity {
    static MusInfAdapter.Callback callback;

    public static void setCallback(MusInfAdapter.Callback callbacks) {
        callback = callbacks;
    }

    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folders);
        getSupportFragmentManager().beginTransaction().add(R.id.frame, VideAudConvFrag.newInstance(new VideAudConvFrag.Callback() { // from class: com.videoplayer.videox.activity.AudFolActivity$$ExternalSyntheticLambda0
            @Override // com.videoplayer.videox.fragment.vid.tab.VideAudConvFrag.Callback
            public final void onDone(List list) {
                AudFolActivity.callback.onFavoriteUpdate(0, true);
            }
        }), "video").commit();
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.AudFolActivity$$ExternalSyntheticLambda1
            @Override
            public final void onClick(View view) {
                AudFolActivity.this.m854lambda$onCreate$1$comvideoplayervideoxactivityAudFolActivity(view);
            }
        });
    }

    /* renamed from: lambda$onCreate$1$com-videoplayer-videox-activity-AudFolActivity, reason: not valid java name */
    void m854lambda$onCreate$1$comvideoplayervideoxactivityAudFolActivity(View view) {
        onBackClick();
    }

    public void onBackClick() {
        AdmobAdsHelper.ShowFullAds(this, true);
        finish();
    }

    public void onSearchClick() {
        SeaVidDiaFrag.newInstance().show(getSupportFragmentManager().beginTransaction(), "dialog_search");
    }
}
