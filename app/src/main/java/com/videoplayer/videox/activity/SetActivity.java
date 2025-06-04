package com.videoplayer.videox.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.NativeAdLayout;
import com.videoplayer.videox.R;
import com.videoplayer.videox.databinding.ActivitySettingsBinding;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.dialog.SetVidModDiaBuil;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;


public class SetActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    private Context mContext;
    private SettingPrefUtils mPrefUtils;

    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingsBinding inflate = ActivitySettingsBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView(inflate.getRoot());
        this.mContext = this;
        this.mPrefUtils = new SettingPrefUtils(this.mContext);
        initSettingView();
        initSwitchView();
    }

    private void initSwitchView() {
        this.binding.swAutoPlayNextVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.videoplayer.videox.activity.SetActivity$$ExternalSyntheticLambda1
            @Override 
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SetActivity.this.m520xbb363db4(compoundButton, z);
            }
        });
        this.binding.swAutoPlayNextMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.videoplayer.videox.activity.SetActivity$$ExternalSyntheticLambda2
            @Override 
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SetActivity.this.m521x55d70035(compoundButton, z);
            }
        });
        this.binding.swResumeVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.videoplayer.videox.activity.SetActivity$$ExternalSyntheticLambda3
            @Override 
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SetActivity.this.m522xf077c2b6(compoundButton, z);
            }
        });
        this.binding.swPitchToZoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.videoplayer.videox.activity.SetActivity$$ExternalSyntheticLambda4
            @Override 
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SetActivity.this.m523x8b188537(compoundButton, z);
            }
        });
        this.binding.swSlideForSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.videoplayer.videox.activity.SetActivity$$ExternalSyntheticLambda5
            @Override 
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SetActivity.this.m524x25b947b8(compoundButton, z);
            }
        });
        this.binding.swSlideForBrightness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.videoplayer.videox.activity.SetActivity$$ExternalSyntheticLambda6
            @Override 
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SetActivity.this.m525xc05a0a39(compoundButton, z);
            }
        });
    }

    /* renamed from: lambda$initSwitchView$0$com-videoplayer-videox-activity-SetActivity */
    void m520xbb363db4(CompoundButton compoundButton, boolean z) {
        this.mPrefUtils.setAutoPlayNextVideo(z);
    }

    /* renamed from: lambda$initSwitchView$1$com-videoplayer-videox-activity-SetActivity */
    void m521x55d70035(CompoundButton compoundButton, boolean z) {
        this.mPrefUtils.setAutoPlayNextMusic(z);
    }

    /* renamed from: lambda$initSwitchView$2$com-videoplayer-videox-activity-SetActivity */
    void m522xf077c2b6(CompoundButton compoundButton, boolean z) {
        this.mPrefUtils.setResumeVideo(z);
    }


    void m523x8b188537(CompoundButton compoundButton, boolean z) {
        this.mPrefUtils.setPitchToZoom(z);
    }

    /* renamed from: lambda$initSwitchView$4$com-videoplayer-videox-activity-SetActivity */
    void m524x25b947b8(CompoundButton compoundButton, boolean z) {
        this.mPrefUtils.setSlideForSound(z);
    }

    /* renamed from: lambda$initSwitchView$5$com-videoplayer-videox-activity-SetActivity */
    void m525xc05a0a39(CompoundButton compoundButton, boolean z) {
        this.mPrefUtils.setSlideForBrightness(z);
    }

    public void initSettingView() {
        this.binding.swAutoPlayNextMusic.setChecked(this.mPrefUtils.isAutoPlayNextMusic());
        this.binding.swAutoPlayNextVideo.setChecked(this.mPrefUtils.isAutoPlayNextVideo());
        this.binding.swResumeVideo.setChecked(this.mPrefUtils.isResumeVideo());
        setVideoMode(this.mPrefUtils.getVideoMode());
        this.binding.swPitchToZoom.setChecked(this.mPrefUtils.isPitchToZoom());
        this.binding.swSlideForSound.setChecked(this.mPrefUtils.isSlideForSound());
        this.binding.swSlideForBrightness.setChecked(this.mPrefUtils.isSlideForBrightness());
        this.binding.ivBack.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.SetActivity.1
            @Override
            public void onClick(View v) {
                SetActivity.this.onBackPressed();
            }
        });
        this.binding.rlAutoPlayNextMusic.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.SetActivity.2
            @Override
            public void onClick(View v) {
                SetActivity.this.onAutoPlayNextMusicClick();
            }
        });
        this.binding.rlResumeVideo.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.SetActivity.3
            @Override
            public void onClick(View v) {
                SetActivity.this.onResumeVideoClick();
            }
        });
        this.binding.rlVideoMode.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.SetActivity.4
            @Override
            public void onClick(View v) {
                SetActivity.this.onVideoModeClick();
            }
        });
        this.binding.rlPitchToZoom.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.SetActivity.5
            @Override
            public void onClick(View v) {
                SetActivity.this.onPitchToZoomClick();
            }
        });
        this.binding.rlSlideForBrightness.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.SetActivity.6
            @Override
            public void onClick(View v) {
                SetActivity.this.onSlideForSoundClick();
            }
        });
        this.binding.rlSlideForSound.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.SetActivity.7
            @Override
            public void onClick(View v) {
                SetActivity.this.onSlideForBrightnessClick();
            }
        });
        this.binding.rlAutoPlayNextVideo.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.SetActivity.8
            @Override
            public void onClick(View v) {
                SetActivity.this.onAutoPlayNextVideoClick();
            }
        });
        loadNativeAds();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadNativeAds() {
        AdmobAdsHelper.smallnativeAds(this.mContext, (ViewGroup) findViewById(R.id.layout_ads), (TextView) findViewById(R.id.adspace), (NativeAdLayout) findViewById(R.id.native_banner_ad_container), 1);
    }

    public void onAutoPlayNextVideoClick() {
        this.binding.swAutoPlayNextVideo.setChecked(!this.binding.swAutoPlayNextVideo.isChecked());
        AdmobAdsHelper.showAdsNumberCount++;
    }

    public void onAutoPlayNextMusicClick() {
        this.binding.swAutoPlayNextMusic.setChecked(!binding.swAutoPlayNextMusic.isChecked());
        AdmobAdsHelper.showAdsNumberCount++;
    }

    public void onResumeVideoClick() {
        this.binding.swResumeVideo.setChecked(!binding.swResumeVideo.isChecked());
        AdmobAdsHelper.showAdsNumberCount++;
    }

    public void onVideoModeClick() {
        AdmobAdsHelper.showAdsNumberCount++;
        BtmMenDialCont.getInstance().showSettingVideoModeDialog(this.mContext, this.mPrefUtils.getVideoMode(), new SetVidModDiaBuil.OkVideoModeListener() { // from class: com.videoplayer.videox.activity.SetActivity$$ExternalSyntheticLambda0
            @Override // com.videoplayer.videox.dialog.SetVidModDiaBuil.OkVideoModeListener
            public final void onClick(int i) {
                SetActivity.this.lambda$onVideoModeClick$9$SettingFragment(i);
            }
        });
    }

    public void lambda$onVideoModeClick$9$SettingFragment(int i) {
        setVideoMode(i);
        this.mPrefUtils.setVideoMode(i);
    }

    public void onPitchToZoomClick() {
        AdmobAdsHelper.showAdsNumberCount++;
        this.binding.swPitchToZoom.setChecked(!binding.swPitchToZoom.isChecked());
    }

    public void onSlideForSoundClick() {
        AdmobAdsHelper.showAdsNumberCount++;
        this.binding.swSlideForSound.setChecked(!binding.swSlideForSound.isChecked());
    }

    public void onSlideForBrightnessClick() {
        AdmobAdsHelper.showAdsNumberCount++;
        this.binding.swSlideForBrightness.setChecked(!binding.swSlideForBrightness.isChecked());
    }

    public void setVideoMode(int i) {
        if (i == 1) {
            this.binding.tvCurrentVideoMode.setText(R.string.sensor);
        } else if (i == 2) {
            this.binding.tvCurrentVideoMode.setText(R.string.portrait);
        } else if (i == 3) {
            this.binding.tvCurrentVideoMode.setText(R.string.landscape);
        }
    }

    @Override
    public void onBackPressed() {
        AdmobAdsHelper.ShowFullAds(this, true);
        finish();
    }
}
