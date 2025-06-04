package com.videoplayer.videox.uti.vid;

import android.app.Activity;
import android.view.WindowManager;

import com.videoplayer.videox.cv.CustStyPlayView;


public class BrightCon {
    private static BrightCon sInstance;
    private int currentBrightnessLevel = 75;

    public float levelToBrightness(int i) {
        double d = (i * 0.00936d) + 0.064d;
        return (float) (d * d);
    }

    private BrightCon() {
    }

    public static BrightCon getInstance() {
        if (sInstance == null) {
            sInstance = new BrightCon();
        }
        return sInstance;
    }

    public void setScreenBrightness(Activity activity, float f) {
        WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
        attributes.screenBrightness = f;
        activity.getWindow().setAttributes(attributes);
    }

    public int getCurrentBrightnessLevel() {
        return this.currentBrightnessLevel;
    }

    public void setCurrentBrightnessLevel(int i) {
        this.currentBrightnessLevel = i;
    }

    public void changeBrightness(Activity activity, CustStyPlayView customStyledPlayerView, boolean z, boolean z2) {
        int i = z ? this.currentBrightnessLevel + 1 : this.currentBrightnessLevel - 1;
        if (z2 && i < 0) {
            this.currentBrightnessLevel = -1;
        } else if (i >= 0 && i <= 100) {
            this.currentBrightnessLevel = i;
        }
        int i2 = this.currentBrightnessLevel;
        if (i2 == -1 && z2) {
            setScreenBrightness(activity, -1.0f);
        } else if (i2 != -1) {
            setScreenBrightness(activity, levelToBrightness(i2));
        }
        int i3 = this.currentBrightnessLevel;
        customStyledPlayerView.showBrightness(i3, i3 == -1 && z2);
    }
}
