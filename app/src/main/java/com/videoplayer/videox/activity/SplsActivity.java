package com.videoplayer.videox.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.appizona.yehiahd.fastsave.FastSave;
import com.facebook.ads.Ad;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.videoplayer.videox.R;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;

import java.util.concurrent.atomic.AtomicBoolean;


public class SplsActivity extends AppCompatActivity {
    private static final String BANNER = "VBANNER";
    private static final String CLICK = "VCLICK";
    private static final String INTER = "VINTER";
    private static final String NATIV = "VNATIVE";
    private static final String PRIVACY = "VPRIVACY";
    private static final String VOPEN = "VOPEN";
    public static String banner;
    public static String click;
    public static String inter;
    private static InterstitialAd interstitialAd;
    public static String nativ;
    public static String openapp;
    public static String privacy;
    private AppOpenAd appOpenAd;
    private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    ConsentForm mConsentForm;
    ConsentInformation mConsentInformation;
    FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        AdmobAdsHelper.is_show_open_ad = false;
        Utility.initFolderPath(this);
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putBoolean("firstTime", false);
        edit.apply();
        if (AdmobAdsHelper.isOnline(this)) {
            DoConsentProcess();
        } else {
            Toast.makeText(this, "No Internet Connected", Toast.LENGTH_SHORT).show();
            ContinueWithoutAdsProcess();
        }
    }

    public void DoConsentProcess() {
        ConsentRequestParameters build = new ConsentRequestParameters.Builder().setTagForUnderAgeOfConsent(false).build();
        ConsentInformation consentInformation = UserMessagingPlatform.getConsentInformation(this);
        this.mConsentInformation = consentInformation;
        consentInformation.requestConsentInfoUpdate(this, build, new ConsentInformation.OnConsentInfoUpdateSuccessListener() { // from class: com.videoplayer.videox.activity.SplsActivity$$ExternalSyntheticLambda3
            @Override // com.google.android.ump.ConsentInformation.OnConsentInfoUpdateSuccessListener
            public final void onConsentInfoUpdateSuccess() {
                SplsActivity.this.m527x58074119();
            }
        }, new ConsentInformation.OnConsentInfoUpdateFailureListener() { // from class: com.videoplayer.videox.activity.SplsActivity$$ExternalSyntheticLambda4
            @Override // com.google.android.ump.ConsentInformation.OnConsentInfoUpdateFailureListener
            public final void onConsentInfoUpdateFailure(FormError formError) {
                SplsActivity.this.m528x117eceb8(formError);
            }
        });
    }

    /* renamed from: lambda$DoConsentProcess$0$com-videoplayer-videox-activity-SplsActivity */
    void m527x58074119() {
        if (this.mConsentInformation.getConsentStatus() == 1) {
            FastSave.getInstance().saveBoolean("EEA_USER", false);
            Log.e("TAG", "User Consent not required!");
        } else {
            FastSave.getInstance().saveBoolean("EEA_USER", true);
            Log.e("TAG", "User Consent required!");
            if (this.mConsentInformation.isConsentFormAvailable()) {
                Log.e("TAG", "Consent Form needed!");
                FastSave.getInstance().saveBoolean("EEA_USER", true);
                LoadConsentForm();
            }
        }
        initializeMobileAdsSdk();
    }

    /* renamed from: lambda$DoConsentProcess$1$com-videoplayer-videox-activity-SplsActivity */
    void m528x117eceb8(FormError formError) {
        ErrorProcess();
    }

    public void LoadConsentForm() {
        UserMessagingPlatform.loadConsentForm(this, new UserMessagingPlatform.OnConsentFormLoadSuccessListener() { // from class: com.videoplayer.videox.activity.SplsActivity$$ExternalSyntheticLambda0
            @Override // com.google.android.ump.UserMessagingPlatform.OnConsentFormLoadSuccessListener
            public final void onConsentFormLoadSuccess(ConsentForm consentForm) {
                SplsActivity.this.m530xb300c7b8(consentForm);
            }
        }, new UserMessagingPlatform.OnConsentFormLoadFailureListener() { // from class: com.videoplayer.videox.activity.SplsActivity$$ExternalSyntheticLambda1
            @Override // com.google.android.ump.UserMessagingPlatform.OnConsentFormLoadFailureListener
            public final void onConsentFormLoadFailure(FormError formError) {
                SplsActivity.this.m531x6c785557(formError);
            }
        });
    }

    /* renamed from: lambda$LoadConsentForm$3$com-videoplayer-videox-activity-SplsActivity */
    void m530xb300c7b8(ConsentForm consentForm) {
        this.mConsentForm = consentForm;
        if (this.mConsentInformation.getConsentStatus() == 2) {
            consentForm.show(this, new ConsentForm.OnConsentFormDismissedListener() { // from class: com.videoplayer.videox.activity.SplsActivity$$ExternalSyntheticLambda6
                @Override // com.google.android.ump.ConsentForm.OnConsentFormDismissedListener
                public final void onConsentFormDismissed(FormError formError) {
                    SplsActivity.this.m529xf9893a19(formError);
                }
            });
        }
    }

    /* renamed from: lambda$LoadConsentForm$2$com-videoplayer-videox-activity-SplsActivity */
    void m529xf9893a19(FormError formError) {
        LoadConsentForm();
        FastSave.getInstance().saveBoolean("ADS_CONSENT_SET", true);
    }

    /* renamed from: lambda$LoadConsentForm$4$com-videoplayer-videox-activity-SplsActivity */
    void m531x6c785557(FormError formError) {
        ErrorProcess();
    }

    public void initializeMobileAdsSdk() {
        if (this.isMobileAdsInitializeCalled.getAndSet(true)) {
            Log.e("TAG", "Mobile Ads already initialized!");
        } else {
            MobileAds.initialize(this, new OnInitializationCompleteListener() { // from class: com.videoplayer.videox.activity.SplsActivity$$ExternalSyntheticLambda7
                @Override // com.google.android.gms.ads.initialization.OnInitializationCompleteListener
                public final void onInitializationComplete(InitializationStatus initializationStatus) {
                    Log.e("Mobile Ads :", "Mobile Ads initialize complete!");
                }
            });
            getdata();
        }
    }

    public void ErrorProcess() {
        if (!FastSave.getInstance().getBoolean(AppCon.REMOVE_ADS_KEY, false)) {
            getdata();
        } else {
            ContinueWithoutAdsProcess();
        }
    }

    private void getdata() {
        this.mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        this.mFirebaseRemoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600L).build());
        this.mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this, new OnCompleteListener() { // from class: com.videoplayer.videox.activity.SplsActivity$$ExternalSyntheticLambda5
            @Override // com.google.android.gms.tasks.OnCompleteListener
            public final void onComplete(Task task) {
                SplsActivity.this.m860lambda$getdata$6$comvideoplayervideoxactivitySplsActivity(task);
            }
        });
    }

    /* renamed from: lambda$getdata$6$com-videoplayer-videox-activity-SplsActivity, reason: not valid java name */
    void m860lambda$getdata$6$comvideoplayervideoxactivitySplsActivity(Task task) {
        displayWelcomeMessage();
    }

    private void displayWelcomeMessage() {
        banner = this.mFirebaseRemoteConfig.getString(BANNER);
        inter = this.mFirebaseRemoteConfig.getString(INTER);
        openapp = this.mFirebaseRemoteConfig.getString(VOPEN);
        nativ = this.mFirebaseRemoteConfig.getString(NATIV);
        privacy = this.mFirebaseRemoteConfig.getString(PRIVACY);
        click = this.mFirebaseRemoteConfig.getString(CLICK);

        FastSave.getInstance().saveString("APP_OPEN", "ca-app-pub-3940256099942544/9257395921");
        FastSave.getInstance().saveString("INTER", "ca-app-pub-3940256099942544/1033173712");
        FastSave.getInstance().saveString("NATIVE", "ca-app-pub-3940256099942544/2247696110");
        FastSave.getInstance().saveString("BANNER", "ca-app-pub-3940256099942544/9214589741");
        FastSave.getInstance().saveString("REWARD", "ca-app-pub-3940256099942544/5224354917");

//        FastSave.getInstance().saveString("APP_OPEN", openapp);
//        FastSave.getInstance().saveString("INTER", inter);
//        FastSave.getInstance().saveString("NATIVE", nativ);
//        FastSave.getInstance().saveString("BANNER", banner);
        FastSave.getInstance().saveString("CLICK", click);
        FastSave.getInstance().saveString("PRIVACY", privacy);
        AdmobAdsHelper.is_show_open_ad = true;
        AdmobAdsHelper.preloadAds(this);
        loadAppOpenAd();
    }

    private void loadAppOpenAd() {
        AppOpenAd.load(this, FastSave.getInstance().getString("APP_OPEN", ""), new AdRequest.Builder().build(), 1, new AppOpenAd.AppOpenAdLoadCallback() { // from class: com.videoplayer.videox.activity.SplsActivity.1
            @Override // com.google.android.gms.ads.AdLoadCallback
            public void onAdLoaded(AppOpenAd ad) {
                SplsActivity.this.appOpenAd = ad;
                SplsActivity.this.showAppOpenAd();
            }

            @Override // com.google.android.gms.ads.AdLoadCallback
            public void onAdFailedToLoad(LoadAdError error) {
                Log.e("TAG", "errr: " + error.getMessage());
                SplsActivity splsActivity = SplsActivity.this;
                splsActivity.LoadFbInterstitialAd(splsActivity);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAppOpenAd() {
        if (this.appOpenAd != null) {
            FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() { // from class: com.videoplayer.videox.activity.SplsActivity.2
                @Override // com.google.android.gms.ads.FullScreenContentCallback
                public void onAdShowedFullScreenContent() {
                }

                @Override // com.google.android.gms.ads.FullScreenContentCallback
                public void onAdDismissedFullScreenContent() {
                    SplsActivity.this.ContinueAfterAdsProcess();
                }

                @Override // com.google.android.gms.ads.FullScreenContentCallback
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    SplsActivity.this.ContinueAfterAdsProcess();
                    Log.e("TAG", "errr: " + adError.getMessage());
                }
            };
            this.appOpenAd.show(this);
            this.appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            return;
        }
        ContinueAfterAdsProcess();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ContinueAfterAdsProcess() {
        if (FastSave.getInstance().getBoolean("STARTED", false)) {
            startActivity(new Intent(this, (Class<?>) StartPolicyActivity.class).addFlags(65536));
            finish();
        } else {
            startActivity(new Intent(this, (Class<?>) IntroActivity.class).addFlags(65536));
            finish();
        }
    }

    private void ContinueWithoutAdsProcess() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.videoplayer.videox.activity.SplsActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                SplsActivity.this.m526x9113ab8c();
            }
        }, 4000);
    }

    /* renamed from: lambda$ContinueWithoutAdsProcess$7$com-videoplayer-videox-activity-SplsActivity */
    void m526x9113ab8c() {
        if (FastSave.getInstance().getBoolean("STARTED", false)) {
            startActivity(new Intent(this, (Class<?>) StartPolicyActivity.class).addFlags(65536));
            finish();
        } else {
            startActivity(new Intent(this, (Class<?>) IntroActivity.class).addFlags(65536));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG", requestCode + " onRequestPermissionsResult: " + resultCode);
        if (requestCode == 2) {
            if (AppCon.isOnline(this).booleanValue()) {
                Log.e("TAG", "1: ");
                getdata();
            } else {
                ContinueWithoutAdsProcess();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void LoadFbInterstitialAd(Context context) {
        try {
            interstitialAd = new InterstitialAd(context, FastSave.getInstance().getString("FINTER", ""));
            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() { // from class: com.videoplayer.videox.activity.SplsActivity.3
                @Override // com.facebook.ads.InterstitialAdListener
                public void onInterstitialDisplayed(Ad ad) {
                    Log.e("TAG", "Interstitial ad displayed.");
                }

                @Override // com.facebook.ads.InterstitialAdListener
                public void onInterstitialDismissed(Ad ad) {
                    Log.e("TAG", "Interstitial ad dismissed.");
                    SplsActivity.this.ContinueAfterAdsProcess();
                }

                @Override // com.facebook.ads.AdListener
                public void onError(Ad ad, com.facebook.ads.AdError adError) {
                    Log.e("TAG", "Interstitial ad failed to load: " + adError.getErrorMessage());
                    Log.e("TAG", "onError " + adError.getErrorCode());
                    SplsActivity.this.ContinueAfterAdsProcess();
                }

                @Override // com.facebook.ads.AdListener
                public void onAdLoaded(Ad ad) {
                    Log.d("TAG", "Interstitial ad is loaded and ready to be displayed!");
                    if (SplsActivity.interstitialAd == null || !SplsActivity.interstitialAd.isAdLoaded()) {
                        return;
                    }
                    SplsActivity.interstitialAd.show();
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
}
