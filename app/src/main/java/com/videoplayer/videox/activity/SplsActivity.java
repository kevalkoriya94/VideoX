package com.videoplayer.videox.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryPurchasesParams;
import com.appizona.yehiahd.fastsave.FastSave;
import com.facebook.ads.Ad;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.videoplayer.videox.R;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.uti.ads.GoogleMobileAdsConsentManager;
import com.videoplayer.videox.uti.ads.Utility;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class SplsActivity extends AppCompatActivity implements PurchasesUpdatedListener {
    private static final String BANNER = "VBANNER";
    private static final String CLICK = "VCLICK";
    private static final String INTER = "VINTER";
    private static final String NATIV = "VNATIVE";
    private static final String PRIVACY = "VPRIVACY";
    private static final String OPEN = "VOPEN";
    private static final String REWARD = "VREWARD";
    private static final String fbBANNER = "fbVBANNER";
    private static final String fbINTER = "fbVINTER";
    private static final String fbNATIV = "fbVNATIV";
    private static final String removeAD = "removeAD";
    private static final String introKEY = "introKEY";
    public static String banner;
    public static String click;
    public static String inter;
    private static InterstitialAd interstitialAd;
    public static String nativ;
    public static String reward;
    public static String open;
    public static String privacy;
    public static String fbbanner;
    public static String fbinter;
    public static String fbnativ;
    public static String introKey;
    public static String removeAd;
    private AppOpenAd appOpenAd;
    private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);
    ConsentForm mConsentForm;
    ConsentInformation mConsentInformation;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    private BillingClient mBillingClient;
    boolean in_app_check = false;
    private GoogleMobileAdsConsentManager googleMobileAdsConsentManager;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        AdmobAdsHelper.is_show_open_ad = false;
        Utility.initFolderPath(this);
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putBoolean("firstTime", false);
        edit.apply();

        setView();
    }

    private void setView() {
        if (AdmobAdsHelper.isOnline(this)) {
            this.mBillingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
            this.mBillingClient.startConnection(new BillingClientStateListener() {

                @Override
                public void onBillingServiceDisconnected() {
                }

                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    try {
                        mBillingClient.queryPurchasesAsync(QueryPurchasesParams.newBuilder().setProductType("inapp").build(), new PurchasesResponseListener() {

                            @Override
                            public void onQueryPurchasesResponse(BillingResult billingResult, List<Purchase> list) {
                                in_app_check = list.toString().contains(AdmobAdsHelper.REMOVE_ADS_PRODUCT_ID);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            queryPurchases();
            googleMobileAdsConsentManager = GoogleMobileAdsConsentManager.getInstance(getApplicationContext());
            googleMobileAdsConsentManager.gatherConsent(this,
                    consentError -> {
                        if (consentError != null) {
                            // Consent not obtained in current session.
                            Log.w("TAG", String.format("%s: %s", consentError.getErrorCode(), consentError.getMessage()));
                        }
                        if (googleMobileAdsConsentManager.isPrivacyOptionsRequired()) {
                            // Regenerate the options menu to include a privacy setting.
                            invalidateOptionsMenu();
                        }
                    });
            DoConsentProcess();
        } else {
            Toast.makeText(this, "No Internet Connected", Toast.LENGTH_LONG).show();
            ContinueWithoutAdsProcess();
        }
    }

    public void DoConsentProcess() {
        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(this)
                .addTestDeviceHashedId("F11363FD1E7588626FEA7D52970C865E")
                .build();

        ConsentRequestParameters build = new ConsentRequestParameters.Builder().setConsentDebugSettings(debugSettings).setTagForUnderAgeOfConsent(false).build();
        ConsentInformation consentInformation = UserMessagingPlatform.getConsentInformation(this);
        this.mConsentInformation = consentInformation;
        consentInformation.requestConsentInfoUpdate(this, build, () -> {
            if (mConsentInformation.getConsentStatus() == 1) {
                FastSave.getInstance().saveBoolean(AdmobAdsHelper.EEA_USER_KEY, false);
                Log.e("TAG", "User Consent not required!");
            } else {
                FastSave.getInstance().saveBoolean(AdmobAdsHelper.EEA_USER_KEY, true);
                Log.e("TAG", "User Consent required!");
                if (mConsentInformation.isConsentFormAvailable()) {
                    Log.e("TAG", "Consent Form needed!");
                    FastSave.getInstance().saveBoolean(AdmobAdsHelper.EEA_USER_KEY, true);
                    LoadConsentForm();
                }
            }

            if (mConsentInformation.canRequestAds()) {
                initializeMobileAdsSdk();
            } else {
                ContinueWithoutAdsProcess();
            }
        }, formError -> ErrorProcess());
    }

    public void LoadConsentForm() {
        UserMessagingPlatform.loadConsentForm(this, consentForm -> {
            mConsentForm = consentForm;
            if (mConsentInformation.getConsentStatus() == 2) {
                consentForm.show(SplsActivity.this, formError -> {
                    LoadConsentForm();
                    FastSave.getInstance().saveBoolean(AdmobAdsHelper.ADS_CONSENT_SET_KEY, true);
                    getAddata();
                });
            }
        }, formError -> ErrorProcess());
    }

    public void initializeMobileAdsSdk() {
        if (this.isMobileAdsInitializeCalled.getAndSet(true)) {
            Log.e("TAG", "Mobile Ads already initialized!");
            return;
        }
        MobileAds.initialize(this, initializationStatus -> Log.e("Mobile Ads :", "Mobile Ads initialize complete!"));
        getAddata();
    }

    private void getAddata() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                boolean updated = task.getResult();
                Log.e("TAG", "Config params updated: " + updated);
                displayWelcomeMessage();
            } else {
                ContinueWithoutAdsProcess();
            }
        });
    }

    public void ErrorProcess() {
        if (!FastSave.getInstance().getBoolean(AdmobAdsHelper.REMOVE_ADS_KEY, false)) {
            getAddata();
        } else {
            ContinueWithoutAdsProcess();
        }
    }


    private void displayWelcomeMessage() {
        banner = mFirebaseRemoteConfig.getString(BANNER);
        inter = mFirebaseRemoteConfig.getString(INTER);
        reward = mFirebaseRemoteConfig.getString(REWARD);
        nativ = mFirebaseRemoteConfig.getString(NATIV);
        open = mFirebaseRemoteConfig.getString(OPEN);
        click = mFirebaseRemoteConfig.getString(CLICK);
        privacy = mFirebaseRemoteConfig.getString(PRIVACY);
        fbbanner = mFirebaseRemoteConfig.getString(fbBANNER);
        fbinter = mFirebaseRemoteConfig.getString(fbINTER);
        fbnativ = mFirebaseRemoteConfig.getString(fbNATIV);
        introKey = mFirebaseRemoteConfig.getString(introKEY);
        removeAd = mFirebaseRemoteConfig.getString(removeAD);

        Log.e("TAG", "onCreate: " + introKey);

//        FastSave.getInstance().saveString("APP_OPEN", "ca-app-pub-3940256099942544/9257395921");
//        FastSave.getInstance().saveString("INTER", "ca-app-pub-3940256099942544/1033173712");
//        FastSave.getInstance().saveString("NATIVE", "ca-app-pub-3940256099942544/2247696110");
//        FastSave.getInstance().saveString("BANNER", "ca-app-pub-3940256099942544/9214589741");
//        FastSave.getInstance().saveString("REWARD", "ca-app-pub-3940256099942544/5224354917");

        FastSave.getInstance().saveString("APP_OPEN", open);
        FastSave.getInstance().saveString("INTER", inter);
        FastSave.getInstance().saveString("NATIVE", nativ);
        FastSave.getInstance().saveString("BANNER", banner);
        FastSave.getInstance().saveString("REWARD", reward);
        FastSave.getInstance().saveString("fbBANNER", fbbanner);
        FastSave.getInstance().saveString("fbINTER", fbinter);
        FastSave.getInstance().saveString("fbNATIV", fbnativ);

        FastSave.getInstance().saveString("CLICK", click);
        FastSave.getInstance().saveString("PRIVACY", privacy);
        FastSave.getInstance().saveString("introKey", introKey);
        FastSave.getInstance().saveString("removeAd", removeAd);


        if (!FastSave.getInstance().getBoolean(AdmobAdsHelper.REMOVE_ADS_KEY, false)) {
            AdmobAdsHelper.is_show_open_ad = true;
            AdmobAdsHelper.preloadAds(this);
            loadAppOpenAd();
        } else {
            ContinueWithoutAdsProcess();
        }

    }

    private void loadAppOpenAd() {
        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(
                this,
                FastSave.getInstance().getString("APP_OPEN", ""),
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd ad) {
                        appOpenAd = ad;
                        showAppOpenAd();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError error) {
                        Log.e("TAG", "onAdFailedToShowFullScreenContent: " + error.getMessage());
                        ContinueAfterAdsProcess();
                    }
                });
    }

    private void showAppOpenAd() {
        if (appOpenAd != null) {
            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Ad dismissed, redirect to the main screen.
                            ContinueAfterAdsProcess();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            Log.e("TAG", "onAdFailedToShowFullScreenContent: " + adError.getMessage());
                            LoadAdMobInterstitialAd();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Ad shown.
                        }
                    };

            appOpenAd.show(this);
            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
        } else {
            // Ad not ready, redirect to the main screen.
            ContinueAfterAdsProcess();
        }
    }

    private void queryPurchases() {
        this.mBillingClient.queryPurchasesAsync(QueryPurchasesParams.newBuilder().setProductType("inapp").build(), new PurchasesResponseListener() {
            @Override
            public void onQueryPurchasesResponse(BillingResult billingResult, List<Purchase> list) {
                if (list != null && !list.isEmpty()) {
                    for (Purchase purchase : list) {
                        if (purchase.getProducts().contains(AdmobAdsHelper.REMOVE_ADS_PRODUCT_ID)) {
                            FastSave.getInstance().saveBoolean(AdmobAdsHelper.REMOVE_ADS_KEY, true);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> list) {
        if (billingResult.getResponseCode() == 0 && list != null) {
            for (Purchase purchase : list) {
                handlePurchase(purchase);
            }
        } else if (billingResult.getResponseCode() == 1) {
            String str = "TAG";
            Log.d(str, "User Canceled" + billingResult.getResponseCode());
        } else if (billingResult.getResponseCode() == 7) {
            FastSave.getInstance().saveBoolean(AdmobAdsHelper.REMOVE_ADS_KEY, true);
        }
    }

    private void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == 1) {
            AcknowledgePurchaseResponseListener r0 = new AcknowledgePurchaseResponseListener() {

                @Override
                public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                    Log.e("result", billingResult.getResponseCode() + "::" + billingResult.getDebugMessage());
                    if (billingResult.getResponseCode() == 0) {
                        FastSave.getInstance().saveBoolean(AdmobAdsHelper.REMOVE_ADS_KEY, true);
                        ContinueWithoutAdsProcess();
                    }
                }
            };
            if (!purchase.isAcknowledged()) {
                this.mBillingClient.acknowledgePurchase(AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build(), r0);
            } else if (purchase.getProducts().contains(AdmobAdsHelper.REMOVE_ADS_PRODUCT_ID)) {
                FastSave.getInstance().saveBoolean(AdmobAdsHelper.REMOVE_ADS_KEY, true);
                ContinueWithoutAdsProcess();
            }
        }
    }

    private void LoadFbInterstitialAd() {
        // TODO Auto-generated method stub
        try {
            AdSettings.addTestDevice("1056b5da-e507-4ad1-9251-3545e5bcc9dc");
            interstitialAd = new com.facebook.ads.InterstitialAd(this, FastSave.getInstance().getString("fbINTER", ""));
            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    Log.e("TAG", "Interstitial ad displayed.");
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    Log.e("TAG", "Interstitial ad dismissed.");
                    ContinueWithoutAdsProcess();
                }

                @Override
                public void onError(Ad ad, com.facebook.ads.AdError adError) {
                    Log.e("TAG", "Interstitial ad failed to load: " + adError.getErrorMessage());
                    LoadAdMobInterstitialAd();
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Log.e("TAG", "Interstitial ad is loaded and ready to be displayed!");
                    if (interstitialAd.isAdLoaded() && interstitialAd != null) {
                        interstitialAd.show();
                    } else {
                        ContinueWithoutAdsProcess();
                    }
                }

                @Override
                public void onAdClicked(Ad ad) {
                    Log.e("TAG", "Interstitial ad clicked!");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    Log.e("TAG", "Interstitial ad impression logged!");
                }
            };
            interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
        } catch (Exception e) {
            ContinueWithoutAdsProcess();
        }
    }

    com.google.android.gms.ads.interstitial.InterstitialAd ad_mob_interstitial;
    AdRequest interstitial_adRequest;

    private void LoadAdMobInterstitialAd() {
        // TODO Auto-generated method stub
        try {
            interstitial_adRequest = new AdRequest.Builder().build();

            com.google.android.gms.ads.interstitial.InterstitialAd.load(this, FastSave.getInstance().getString("INTER", ""), interstitial_adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                    ad_mob_interstitial = interstitialAd;
                    ShowAdsOpenFile();
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    ad_mob_interstitial = null;
                    LoadFbInterstitialAd();
                }
            });
        } catch (Exception e) {
            ContinueAfterAdsProcess();
            e.printStackTrace();
        }
    }

    public void ShowAdsOpenFile() {
        com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd = ad_mob_interstitial;
        if (interstitialAd != null) {
            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    ContinueAfterAdsProcess();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    ContinueAfterAdsProcess();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    ad_mob_interstitial = null;
                }
            });
        }
        if (ad_mob_interstitial != null) {
            ad_mob_interstitial.show(this);
            AdmobAdsHelper.is_show_open_ad = false;
        }
    }

    private void ContinueAfterAdsProcess() {
        if (FastSave.getInstance().getString("introKey", "").equals("0")) {
            if (FastSave.getInstance().getBoolean("STARTED", false)) {
                startActivity(new Intent(SplsActivity.this, StartPolicyActivity.class));
                finish();
            } else {
                startActivity(new Intent(this, IntroActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                finish();
            }
        } else {
            startActivity(new Intent(this, IntroActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            finish();
        }

    }

    private void ContinueWithoutAdsProcess() {
        new Handler(Looper.getMainLooper()).postDelayed(SplsActivity.this::ContinueAfterAdsProcess, 4000L);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
    }
}
