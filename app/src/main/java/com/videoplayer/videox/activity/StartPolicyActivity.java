package com.videoplayer.videox.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.appizona.yehiahd.fastsave.FastSave;
import com.facebook.ads.Ad;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAdLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewException;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.videoplayer.videox.R;
import com.videoplayer.videox.dialog.QueDiaBuil;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;

import java.util.Objects;


public class StartPolicyActivity extends AppCompatActivity {
    private static InterstitialAd interstitialAd;
    com.google.android.gms.ads.interstitial.InterstitialAd ad_mob_interstitial;
    private TextView buttonContinue;
    private CheckBox checkboxTerms;
    AdRequest interstitial_adRequest;
    String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    String[] permissions33 = {"android.permission.READ_MEDIA_VIDEO", "android.permission.READ_MEDIA_AUDIO"};

    static void lambda$onResume$3(Task task) {
    }

    @Override
    public void onBackPressed() {
    }

    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_new_start_privacy_policy);
        this.checkboxTerms = findViewById(R.id.checkboxTerms);
        this.buttonContinue = findViewById(R.id.buttonContinue);
        this.checkboxTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.videoplayer.videox.activity.StartPolicyActivity$$ExternalSyntheticLambda2
            @Override 
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                StartPolicyActivity.this.m532x217e8f58(compoundButton, z);
            }
        });
        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.StartPolicyActivity$$ExternalSyntheticLambda3
            @Override 
            public void onClick(View view) {
                StartPolicyActivity.this.m533xaeb940d9(view);
            }
        });
        findViewById(R.id.tvPolicy).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.StartPolicyActivity$$ExternalSyntheticLambda4
            @Override 
            public void onClick(View view) {
                StartPolicyActivity.this.m534x3bf3f25a(view);
            }
        });
        AdmobAdsHelper.smallnativeAds(this, findViewById(R.id.ad_layout), findViewById(R.id.adspace), findViewById(R.id.native_banner_ad_container), 1);

    }

    /* renamed from: lambda$onCreate$0$com-videoplayer-videox-activity-StartPolicyActivity */
    void m532x217e8f58(CompoundButton compoundButton, boolean z) {
        if (z) {
            this.buttonContinue.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorAds)));
            this.buttonContinue.setEnabled(true);
        } else {
            this.buttonContinue.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.gray)));
            this.buttonContinue.setEnabled(false);
        }
    }

    /* renamed from: lambda$onCreate$1$com-videoplayer-videox-activity-StartPolicyActivity */
    void m533xaeb940d9(View view) {
        if (this.checkboxTerms.isChecked()) {
            if (Build.VERSION.SDK_INT >= 33) {
                if (hasStoragePermissions33(this)) {
                    FastSave.getInstance().saveBoolean("ACCEPTED", true);
                    startActivity(new Intent(getApplicationContext(), HomPageActivity.class));
                    DisplayInterstitial(this);
                    finish();
                    return;
                }
                ActivityCompat.requestPermissions(this, this.permissions33, 1);
                return;
            }
            if (Build.VERSION.SDK_INT >= 26) {
                if (hasStoragePermissions(this)) {
                    FastSave.getInstance().saveBoolean("ACCEPTED", true);
                    startActivity(new Intent(getApplicationContext(), HomPageActivity.class));
                    DisplayInterstitial(this);
                    finish();
                    return;
                }
                ActivityCompat.requestPermissions(this, this.permissions, 1);
                return;
            }
            return;
        }
        Toast.makeText(this, "Please accept Term & Conditions", Toast.LENGTH_SHORT).show();
    }

    /* renamed from: lambda$onCreate$2$com-videoplayer-videox-activity-StartPolicyActivity */
    void m534x3bf3f25a(View view) {
        startActivity(new Intent(getApplicationContext(), PrivPoliActivity.class));
        AdmobAdsHelper.ShowFullAds(this, false);
    }

    public static boolean hasStoragePermissions(Context context) {
        return ContextCompat.checkSelfPermission(context, "android.permission.READ_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public static boolean hasStoragePermissions33(Context context) {
        return ContextCompat.checkSelfPermission(context, "android.permission.READ_MEDIA_VIDEO") == 0 && ContextCompat.checkSelfPermission(context, "android.permission.READ_MEDIA_AUDIO") == 0;
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (Build.VERSION.SDK_INT >= 33) {
            if (!hasStoragePermissions33(this)) {
                new QueDiaBuil(this, new QueDiaBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.activity.StartPolicyActivity.1
                    @Override 
                    public void onOkClick() {
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.parse("package:" + StartPolicyActivity.this.getPackageName()));
                        StartPolicyActivity.this.startActivityForResult(intent, 2);
                    }

                    @Override 
                    public void onCancelClick() {
                        StartPolicyActivity.this.finish();
                    }
                }).setTitle(R.string.notice, R.color.white).setQuestion(R.string.question_request_permission).build().show();
                return;
            }
            startActivity(new Intent(getApplicationContext(), HomPageActivity.class));
            DisplayInterstitial(this);
            finish();
            return;
        }
        if (Build.VERSION.SDK_INT >= 26) {
            if (!hasStoragePermissions(this)) {
                new QueDiaBuil(this, new QueDiaBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.activity.StartPolicyActivity.2
                    @Override 
                    public void onOkClick() {
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.parse("package:" + StartPolicyActivity.this.getPackageName()));
                        StartPolicyActivity.this.startActivityForResult(intent, 2);
                    }

                    @Override 
                    public void onCancelClick() {
                        StartPolicyActivity.this.finish();
                    }
                }).setTitle(R.string.notice, R.color.white).setQuestion(R.string.question_request_permission).build().show();
                return;
            }
            startActivity(new Intent(getApplicationContext(), HomPageActivity.class));
            DisplayInterstitial(this);
            finish();
        }
    }

    public void LoadInterstitial(Context context) {
        try {
            this.interstitial_adRequest = new AdRequest.Builder().build();
            com.google.android.gms.ads.interstitial.InterstitialAd.load(context, FastSave.getInstance().getString("inter", ""), this.interstitial_adRequest, new InterstitialAdLoadCallback() { // from class: com.videoplayer.videox.activity.StartPolicyActivity.3
                @Override
                public void onAdLoaded(com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd2) {
                    StartPolicyActivity.this.ad_mob_interstitial = interstitialAd2;
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    StartPolicyActivity.this.ad_mob_interstitial = null;
                    StartPolicyActivity startPolicyActivity = StartPolicyActivity.this;
                    startPolicyActivity.LoadFbInterstitialAd(startPolicyActivity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DisplayInterstitial(Context context) {
        com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd2 = this.ad_mob_interstitial;
        if (interstitialAd2 != null) {
            interstitialAd2.setFullScreenContentCallback(new FullScreenContentCallback() { // from class: com.videoplayer.videox.activity.StartPolicyActivity.4
                @Override // com.google.android.gms.ads.FullScreenContentCallback
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                }

                @Override // com.google.android.gms.ads.FullScreenContentCallback
                public void onAdDismissedFullScreenContent() {
                    AdmobAdsHelper.is_show_open_ad = true;
                }

                @Override // com.google.android.gms.ads.FullScreenContentCallback
                public void onAdShowedFullScreenContent() {
                    StartPolicyActivity.this.ad_mob_interstitial = null;
                }
            });
        }
        com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd3 = this.ad_mob_interstitial;
        if (interstitialAd3 != null) {
            interstitialAd3.show((Activity) context);
        } else {
            InterstitialAd interstitialAd4 = interstitialAd;
            if (interstitialAd4 != null && interstitialAd4.isAdLoaded()) {
                interstitialAd.show();
            }
        }
        AdmobAdsHelper.is_show_open_ad = false;
    }

    public void LoadFbInterstitialAd(Context context) {
        try {
            interstitialAd = new InterstitialAd(context, getString(R.string.fbinter));
            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() { // from class: com.videoplayer.videox.activity.StartPolicyActivity.5
                @Override // com.facebook.ads.InterstitialAdListener
                public void onInterstitialDisplayed(Ad ad) {
                    Log.e("TAG", "Interstitial ad displayed.");
                }

                @Override // com.facebook.ads.InterstitialAdListener
                public void onInterstitialDismissed(Ad ad) {
                    Log.e("TAG", "Interstitial ad dismissed.");
                }

                @Override // com.facebook.ads.AdListener
                public void onError(Ad ad, com.facebook.ads.AdError adError) {
                    Log.e("TAG", "Interstitial ad failed to load: " + adError.getErrorMessage());
                }

                @Override // com.facebook.ads.AdListener
                public void onAdLoaded(Ad ad) {
                    Log.d("TAG", "Interstitial ad is loaded and ready to be displayed!");
                }

                @Override // com.facebook.ads.AdListener
                public void onAdClicked(Ad ad) {
                    Log.d("TAG", "Interstitial ad clicked!");
                }

                @Override // com.facebook.ads.AdListener
                public void onLoggingImpression(Ad ad) {
                    Log.d("TAG", "Interstitial ad impression logged!");
                }
            };
            InterstitialAd interstitialAd2 = interstitialAd;
            interstitialAd2.loadAd(interstitialAd2.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadInterstitial(this);
        LoadFbInterstitialAd(this);
        try {
            final ReviewManager create = ReviewManagerFactory.create(this);
            create.requestReviewFlow().addOnCompleteListener(new OnCompleteListener() { // from class: com.videoplayer.videox.activity.StartPolicyActivity$$ExternalSyntheticLambda0
                @Override // com.google.android.gms.tasks.OnCompleteListener
                public void onComplete(Task task) {
                    StartPolicyActivity.this.m535x73728dcd(create, task);
                }
            });
        } catch (Exception unused) {
        }
    }

    /* renamed from: lambda$onResume$4$com-videoplayer-videox-activity-StartPolicyActivity */
    void m535x73728dcd(ReviewManager reviewManager, Task task) {
        if (task.isSuccessful()) {
            reviewManager.launchReviewFlow(this, (ReviewInfo) task.getResult()).addOnCompleteListener(new OnCompleteListener() { // from class: com.videoplayer.videox.activity.StartPolicyActivity$$ExternalSyntheticLambda1
                @Override // com.google.android.gms.tasks.OnCompleteListener
                public void onComplete(Task task2) {
                    StartPolicyActivity.lambda$onResume$3(task2);
                }
            });
        } else {
            ((ReviewException) task.getException()).getErrorCode();
        }
    }
}
