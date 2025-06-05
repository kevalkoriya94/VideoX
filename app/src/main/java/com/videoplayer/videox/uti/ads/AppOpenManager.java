package com.videoplayer.videox.uti.ads;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.appizona.yehiahd.fastsave.FastSave;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.videoplayer.videox.XApplication;
import com.videoplayer.videox.uti.cons.AppCon;

import java.util.Date;


public class AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    private static final String LOG_TAG = "AppOpenManager";
    private static boolean isShowingAd = false;
    private Activity currentActivity;
    private final XApplication flixApplication;
    private AppOpenAd app_open_ad = null;
    private long loadTime = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    public AppOpenManager(XApplication application) {
        this.flixApplication = application;
        application.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public void showAdIfAvailable() {
        if (!FastSave.getInstance().getBoolean(AppCon.REMOVE_ADS_KEY, false)) {
            if (!isShowingAd && isAdAvailable()) {
                FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() { // from class: com.videoplayer.videox.uti.ads.AppOpenManager.1
                    @Override // com.google.android.gms.ads.FullScreenContentCallback
                    public void onAdDismissedFullScreenContent() {
                        AppOpenManager.this.app_open_ad = null;
                        boolean unused = AppOpenManager.isShowingAd = false;
                        AppOpenManager.this.fetchAd();
                        AdmobAdsHelper.is_show_open_ad = true;
                    }

                    @Override // com.google.android.gms.ads.FullScreenContentCallback
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        Log.e("TAG", "showAdIfAvailable: 111 " + adError.getMessage());
                    }

                    @Override // com.google.android.gms.ads.FullScreenContentCallback
                    public void onAdShowedFullScreenContent() {
                        boolean unused = AppOpenManager.isShowingAd = true;
                    }
                };
                Log.e("TAG", "showAdIfAvailable: 111 " + AdmobAdsHelper.is_show_open_ad);
                if (AdmobAdsHelper.is_show_open_ad) {
                    this.app_open_ad.setFullScreenContentCallback(fullScreenContentCallback);
                    this.app_open_ad.show(this.currentActivity);
                    return;
                } else {
                    this.app_open_ad = null;
                    isShowingAd = false;
                    fetchAd();
                    return;
                }
            }
            fetchAd();
            return;
        }
        fetchAd();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        showAdIfAvailable();
    }

    public void fetchAd() {
        if (isAdAvailable()) {
            return;
        }
        AppOpenAd.AppOpenAdLoadCallback appOpenAdLoadCallback = new AppOpenAd.AppOpenAdLoadCallback() { // from class: com.videoplayer.videox.uti.ads.AppOpenManager.2
            @Override
            public void onAdLoaded(AppOpenAd appOpenAd) {
                super.onAdLoaded(appOpenAd);
                AppOpenManager.this.app_open_ad = appOpenAd;
                AppOpenManager.this.loadTime = new Date().getTime();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        };
        AppOpenAd.load(this.flixApplication, FastSave.getInstance().getString("APP_OPEN", ""), getAdRequest(), 1, appOpenAdLoadCallback);
    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    private boolean wasLoadTimeLessThanNHoursAgo() {
        return new Date().getTime() - this.loadTime < 14400000;
    }

    public boolean isAdAvailable() {
        return this.app_open_ad != null && wasLoadTimeLessThanNHoursAgo();
    }

    @Override
    public void onActivityStarted(Activity activity) {
        this.currentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        this.currentActivity = activity;
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        this.currentActivity = null;
    }
}
