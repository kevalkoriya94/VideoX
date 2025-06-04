package com.videoplayer.videox;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.appizona.yehiahd.fastsave.FastSave;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.FirebaseApp;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.uti.ads.AppOpenManager;

import java.util.Locale;


public class XApplication extends MultiDexApplication {
    private static XApplication singleton;

    static void lambda$onCreate$0(InitializationStatus initializationStatus) {
    }

    public static XApplication getInstance() {
        return singleton;
    }

    public XApplication() {
        singleton = this;
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        singleton = this;
        MultiDex.install(this);
        FastSave.init(this);
        FirebaseApp.initializeApp(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() { // from class: com.videoplayer.videox.XApplication$$ExternalSyntheticLambda0
            @Override // com.google.android.gms.ads.initialization.OnInitializationCompleteListener
            public final void onInitializationComplete(InitializationStatus initializationStatus) {
                XApplication.lambda$onCreate$0(initializationStatus);
            }
        });
        AudienceNetworkAds.initialize(this);
        try {
            new AppOpenManager(this);
            OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);
            OneSignal.initWithContext(this, "c05a2577-f66c-4c69-bc91-4f3a4fd7ebf0");
            OneSignal.getUser().getPushSubscription().optIn();
        } catch (Exception unused) {
        }
        switch (new SettingPrefUtils(getBaseContext()).getLanguage()) {
            case 1:
                setLanguageApp("en");
                break;
            case 2:
                setLanguageApp("zu");
                break;
            case 3:
                setLanguageApp("bn");
                break;
            case 4:
                setLanguageApp("es");
                break;
            case 5:
                setLanguageApp("hi");
                break;
            case 6:
                setLanguageApp("in");
                break;
            case 7:
                setLanguageApp("ira");
                break;
            case 8:
                setLanguageApp("phi");
                break;
            case 9:
                setLanguageApp("pt");
                break;
            case 10:
                setLanguageApp("ur");
                break;
            case 11:
                setLanguageApp("vi");
                break;
            default:
                String language = Resources.getSystem().getConfiguration().locale.getLanguage();
                setLanguageApp(language);
                Log.d("binhnk08", "systemLanguage = " + language);
                break;
        }
    }

    public void setLanguageApp(String str) {
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        if (configuration.locale.getLanguage().equals(str)) {
            return;
        }
        Locale locale = new Locale(str);
        Locale.setDefault(locale);
        Configuration configuration2 = new Configuration(configuration);
        configuration2.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration2, getBaseContext().getResources().getDisplayMetrics());
    }
}
