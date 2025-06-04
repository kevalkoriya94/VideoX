package com.videoplayer.videox.uti.ads;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.appizona.yehiahd.fastsave.FastSave;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.videoplayer.videox.R;

import java.util.ArrayList;


public class AdmobAdsHelper {
    public static final String ADS_CONSENT_SET_KEY = "ADS_CONSENT_SET";
    public static final String EEA_USER_KEY = "EEA_USER";
    static InterstitialAd ad_mob_interstitial = null;
    private static com.facebook.ads.InterstitialAd interstitialAd = null;
    static AdRequest interstitial_adRequest = null;
    public static boolean is_show_open_ad = true;
    private static com.facebook.ads.NativeAd nativeAd = null;
    public static int showAdsNumberCount = 1;
    Context context;

    static void lambda$preloadAds$0(InitializationStatus initializationStatus) {
    }

    public AdmobAdsHelper(Context context) {
        this.context = context;
    }

    public static boolean isOnline(Activity activity) {
        try {
            return ((ConnectivityManager) activity.getSystemService("connectivity")).getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception unused) {
            return false;
        }
    }

    public static void preloadAds(Activity activity) {
        MobileAds.initialize(activity, new OnInitializationCompleteListener() { // from class: com.videoplayer.videox.uti.ads.AdmobAdsHelper$$ExternalSyntheticLambda2
            @Override // com.google.android.gms.ads.initialization.OnInitializationCompleteListener
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                AdmobAdsHelper.lambda$preloadAds$0(initializationStatus);
            }
        });
        AudienceNetworkAds.initialize(activity);
        LoadAdMobInterstitialAd(activity);
    }

    public static void bannerAds(Context mContext, ViewGroup frameLayout, TextView adsspace) {
        showAdmobBanner(mContext, frameLayout, adsspace);
    }

    public static void nativeAds(Context mContext, ViewGroup frameLayout, TextView adsspace, NativeAdLayout native_banner_ad_container) {
        showAdmobNative(mContext, frameLayout, adsspace, native_banner_ad_container);
    }

    public static void smallnativeAds(Context mContext, ViewGroup frameLayout, TextView adsspace, NativeAdLayout native_banner_ad_container, int which) {
        loadgoogleSmallNative(mContext, frameLayout, adsspace, native_banner_ad_container, which);
    }

    public static void showAdmobBanner(final Context context, final ViewGroup linearLayout1, TextView adsspace) {
        final AdView adView = new AdView(context);
        adView.setVisibility(View.VISIBLE);
        adView.setEnabled(true);
        adView.setAdUnitId(FastSave.getInstance().getString("BANNER", ""));
        linearLayout1.setVisibility(View.VISIBLE);
        linearLayout1.removeAllViews();
        linearLayout1.addView(adView);
        loadBanner((Activity) context, adView);
        adView.setAdListener(new AdListener() { // from class: com.videoplayer.videox.uti.ads.AdmobAdsHelper.1
            @Override // com.google.android.gms.ads.AdListener
            public void onAdClosed() {
            }

            @Override // com.google.android.gms.ads.AdListener
            public void onAdOpened() {
            }

            @Override
            // com.google.android.gms.ads.AdListener, com.google.android.gms.ads.internal.client.zza
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override // com.google.android.gms.ads.AdListener
            public void onAdLoaded() {
                try {
                    linearLayout1.removeAllViews();
                    linearLayout1.addView(adView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override // com.google.android.gms.ads.AdListener
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Context context2 = context;
                com.facebook.ads.AdView adView2 = new com.facebook.ads.AdView(context2, context2.getString(R.string.fbbanner), AdSize.BANNER_HEIGHT_50);
                linearLayout1.addView(adView2);
                adView2.loadAd(adView2.buildLoadAdConfig().withAdListener(new com.facebook.ads.AdListener() { // from class: com.videoplayer.videox.uti.ads.AdmobAdsHelper.1.1
                    @Override // com.facebook.ads.AdListener
                    public void onAdClicked(Ad ad) {
                    }

                    @Override // com.facebook.ads.AdListener
                    public void onAdLoaded(Ad ad) {
                    }

                    @Override // com.facebook.ads.AdListener
                    public void onLoggingImpression(Ad ad) {
                    }

                    @Override // com.facebook.ads.AdListener
                    public void onError(Ad ad, AdError adError) {
                        Log.e("TAG", "onError: " + adError.getErrorMessage());
                    }
                }).build());
            }
        });
    }

    private static void loadBanner(Activity context, AdView adView) {
        AdRequest build = new AdRequest.Builder().build();
        adView.setAdSize(getAdSize(context));
        adView.loadAd(build);
    }

    private static com.google.android.gms.ads.AdSize getAdSize(Activity context) {
        Display defaultDisplay = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, (int) (displayMetrics.widthPixels / displayMetrics.density));
    }

    public static void showAdmobNative(final Context context, final ViewGroup frameLayout, final TextView adsspace, final NativeAdLayout nativeAdLayout) {
        String adInter = FastSave.getInstance().getString("NATIVE", "");
        AdLoader.Builder builder = new AdLoader.Builder(context, adInter)
                .forNativeAd(nativeAd -> {
                    NativeAdView adView = (NativeAdView) ((Activity) context).getLayoutInflater()
                            .inflate(R.layout.view_native_ad_layout, null);
                    populateUnifiedNativeAdView(nativeAd, adView, false);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                });

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.e("TAG", "onAdFailedToLoad: " + loadAdError.getMessage());
                super.onAdFailedToLoad(loadAdError);
                nativeAdLayout.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                loadNativeAd(context, frameLayout, adsspace, nativeAdLayout);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                nativeAdLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }


    public static void loadgoogleSmallNative(final Context mContext, final ViewGroup frameLayout, final TextView adsspace, final NativeAdLayout nativeAdLayout, final int which) {
        String adInter = FastSave.getInstance().getString("NATIVE", "");
        AdLoader.Builder builder = new AdLoader.Builder(mContext, adInter)
                .forNativeAd(nativeAd -> {
                    int i;
                    i = R.layout.view_small_native_ad_layout;
                    NativeAdView adView = (NativeAdView) ((Activity) mContext).getLayoutInflater()
                            .inflate(i, null);
                    populateUnifiedNativeAdView(nativeAd, adView, true);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                });

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.e("TAG", "onAdFailedToLoad: " + loadAdError.getMessage());
                super.onAdFailedToLoad(loadAdError);
                nativeAdLayout.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                loadNativeAd(mContext, frameLayout, adsspace, nativeAdLayout);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                nativeAdLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private static void populateUnifiedNativeAdView(com.google.android.gms.ads.nativead.NativeAd nativeAd2, NativeAdView adView, boolean isSmall) {
        if (!isSmall) {
            adView.setMediaView(adView.findViewById(R.id.ad_media));
            adView.getMediaView().setMediaContent(nativeAd2.getMediaContent());
            adView.setStoreView(adView.findViewById(R.id.ad_store));
        }
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        ((TextView) adView.getHeadlineView()).setText(nativeAd2.getHeadline());
        if (nativeAd2.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd2.getBody());
        }
        if (nativeAd2.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(nativeAd2.getCallToAction());
        }
        if (nativeAd2.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd2.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }
        if (nativeAd2.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd2.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }
        if (nativeAd2.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd2.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd2);
    }

    public static void loadNativeAd(final Context context, final ViewGroup frameLayout, final TextView adsspace, final NativeAdLayout nativeAdLayout) {
        nativeAd = new com.facebook.ads.NativeAd(context, FastSave.getInstance().getString("FBNATIVE", ""));

        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                adsspace.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                nativeAdLayout.setVisibility(View.GONE);
                bannerAds(context, frameLayout, adsspace);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                frameLayout.setVisibility(View.GONE);
                nativeAdLayout.setVisibility(View.VISIBLE);
                // Race condition, load() called again before last ad was displayed
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                inflateAd(context, nativeAd, nativeAdLayout);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        // Request an ad
        nativeAd.loadAd(
                nativeAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void inflateAd(Context context, com.facebook.ads.NativeAd nativeAd2, NativeAdLayout nativeAdLayout) {
        nativeAd2.unregisterView();
        View inflate = LayoutInflater.from(context).inflate(R.layout.view_custom_small_native, null);
        nativeAdLayout.addView(inflate);
        RelativeLayout relativeLayout = inflate.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd2, inflate.findViewById(R.id.nativview));
        relativeLayout.removeAllViews();
        relativeLayout.addView(adOptionsView, 0);
        TextView textView = inflate.findViewById(R.id.native_ad_title);
        TextView textView2 = inflate.findViewById(R.id.native_ad_social_context);
        TextView textView3 = inflate.findViewById(R.id.native_ad_sponsored_label);
        com.facebook.ads.MediaView mediaView = inflate.findViewById(R.id.native_icon_view);
        TextView textView4 = inflate.findViewById(R.id.native_ad_call_to_action);
        textView4.setText(nativeAd2.getAdCallToAction());
        textView4.setVisibility(nativeAd2.hasCallToAction() ? 0 : 4);
        textView.setText(nativeAd2.getAdvertiserName());
        textView2.setText(nativeAd2.getAdSocialContext());
        textView3.setText(nativeAd2.getSponsoredTranslation());
        ArrayList arrayList = new ArrayList();
        arrayList.add(textView);
        arrayList.add(textView4);
        nativeAd2.registerViewForInteraction(inflate, mediaView, arrayList);
    }

    public static void LoadAdMobInterstitialAd(final Context context) {
        LoadFBInterstitialAd(context);
        try {
            String string = FastSave.getInstance().getString("INTER", "");
            AdRequest build = new AdRequest.Builder().build();
            interstitial_adRequest = build;
            InterstitialAd.load(context, string, build, new InterstitialAdLoadCallback() { // from class: com.videoplayer.videox.uti.ads.AdmobAdsHelper.5
                @Override // com.google.android.gms.ads.AdLoadCallback
                public void onAdLoaded(InterstitialAd interstitialAd2) {
                    AdmobAdsHelper.ad_mob_interstitial = interstitialAd2;
                    Log.e("TAG", "Interstitial admob load: ");
                }

                @Override // com.google.android.gms.ads.AdLoadCallback
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    Log.e("TAG", "Interstitial admob failed to load: " + loadAdError.getMessage());
                    AdmobAdsHelper.ad_mob_interstitial = null;
                    AdmobAdsHelper.LoadFBInterstitialAd(context);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ShowFullAds(final Context context, boolean isok) {
        InterstitialAd interstitialAd2 = ad_mob_interstitial;
        if (interstitialAd2 != null) {
            interstitialAd2.setFullScreenContentCallback(new FullScreenContentCallback() { // from class: com.videoplayer.videox.uti.ads.AdmobAdsHelper.6
                @Override // com.google.android.gms.ads.FullScreenContentCallback
                public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                }

                @Override // com.google.android.gms.ads.FullScreenContentCallback
                public void onAdDismissedFullScreenContent() {
                    AdmobAdsHelper.LoadAdMobInterstitialAd(context);
                }

                @Override // com.google.android.gms.ads.FullScreenContentCallback
                public void onAdShowedFullScreenContent() {
                    AdmobAdsHelper.ad_mob_interstitial = null;
                }
            });
        } else {
            LoadFBInterstitialAd(context);
        }
        int i = showAdsNumberCount + 1;
        showAdsNumberCount = i;
        String string = FastSave.getInstance().getString("CLICK", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE);
        Log.e("TAG", showAdsNumberCount + " <><><> " + i);
        Log.e("TAG", "ShowFullAds: " + string);
        if (string.isEmpty()) {
            if (3 < showAdsNumberCount) {
                InterstitialAd interstitialAd3 = ad_mob_interstitial;
                if (interstitialAd3 != null) {
                    interstitialAd3.show((Activity) context);
                } else {
                    com.facebook.ads.InterstitialAd interstitialAd4 = interstitialAd;
                    if (interstitialAd4 != null && interstitialAd4.isAdLoaded()) {
                        interstitialAd.show();
                    }
                }
                showAdsNumberCount = 0;
            }
        } else if (Integer.parseInt(FastSave.getInstance().getString("CLICK", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE)) < showAdsNumberCount) {
            InterstitialAd interstitialAd5 = ad_mob_interstitial;
            if (interstitialAd5 != null) {
                interstitialAd5.show((Activity) context);
            } else {
                com.facebook.ads.InterstitialAd interstitialAd6 = interstitialAd;
                if (interstitialAd6 != null && interstitialAd6.isAdLoaded()) {
                    interstitialAd.show();
                }
            }
            showAdsNumberCount = 0;
        }
        is_show_open_ad = false;
    }

    public static void ShowFullAdsIntent(final Context context, final Intent intent) {
        InterstitialAd interstitialAd2 = ad_mob_interstitial;
        if (interstitialAd2 != null) {
            interstitialAd2.setFullScreenContentCallback(new FullScreenContentCallback() { // from class: com.videoplayer.videox.uti.ads.AdmobAdsHelper.7
                @Override // com.google.android.gms.ads.FullScreenContentCallback
                public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                    AdmobAdsHelper.is_show_open_ad = true;
                    context.startActivity(intent);
                }

                @Override // com.google.android.gms.ads.FullScreenContentCallback
                public void onAdDismissedFullScreenContent() {
                    AdmobAdsHelper.is_show_open_ad = true;
                    context.startActivity(intent);
                    AdmobAdsHelper.LoadAdMobInterstitialAd(context);
                }

                @Override // com.google.android.gms.ads.FullScreenContentCallback
                public void onAdShowedFullScreenContent() {
                    AdmobAdsHelper.is_show_open_ad = false;
                    AdmobAdsHelper.ad_mob_interstitial = null;
                }
            });
        } else {
            context.startActivity(intent);
            LoadFBInterstitialAd(context);
        }
        int i = showAdsNumberCount + 1;
        showAdsNumberCount = i;
        String string = FastSave.getInstance().getString("CLICK", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE);
        Log.e("TAG", showAdsNumberCount + " <><><> " + i);
        Log.e("TAG", "ShowFullAds: " + string);
        if (string.isEmpty()) {
            if (3 < showAdsNumberCount) {
                InterstitialAd interstitialAd3 = ad_mob_interstitial;
                if (interstitialAd3 != null) {
                    interstitialAd3.show((Activity) context);
                } else {
                    com.facebook.ads.InterstitialAd interstitialAd4 = interstitialAd;
                    if (interstitialAd4 != null && interstitialAd4.isAdLoaded()) {
                        interstitialAd.show();
                    }
                }
                showAdsNumberCount = 0;
            } else {
                context.startActivity(intent);
            }
        } else if (Integer.parseInt(FastSave.getInstance().getString("CLICK", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE)) < showAdsNumberCount) {
            InterstitialAd interstitialAd5 = ad_mob_interstitial;
            if (interstitialAd5 != null) {
                interstitialAd5.show((Activity) context);
            } else {
                com.facebook.ads.InterstitialAd interstitialAd6 = interstitialAd;
                if (interstitialAd6 != null && interstitialAd6.isAdLoaded()) {
                    interstitialAd.show();
                }
            }
            showAdsNumberCount = 0;
        } else {
            context.startActivity(intent);
        }
        is_show_open_ad = false;
    }

    public static void LoadFBInterstitialAd(Context context) {
        try {
            interstitialAd = new com.facebook.ads.InterstitialAd(context, context.getString(R.string.fbinter));
            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() { // from class: com.videoplayer.videox.uti.ads.AdmobAdsHelper.8
                @Override // com.facebook.ads.InterstitialAdListener
                public void onInterstitialDisplayed(Ad ad) {
                    Log.e("TAG", "Interstitial ad displayed.");
                }

                @Override // com.facebook.ads.InterstitialAdListener
                public void onInterstitialDismissed(Ad ad) {
                    Log.e("TAG", "Interstitial ad dismissed.");
                }

                @Override // com.facebook.ads.AdListener
                public void onError(Ad ad, AdError adError) {
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
            com.facebook.ads.InterstitialAd interstitialAd2 = interstitialAd;
            interstitialAd2.loadAd(interstitialAd2.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
