package com.videoplayer.videox.db.utils;

import android.content.Context;

import com.videoplayer.videox.uti.PrefInf;
import com.videoplayer.videox.uti.SharPrefUti;


public class SettingPrefUtils {
    private final Context mContext;

    public SettingPrefUtils(Context context) {
        this.mContext = context;
    }

    public int getLanguage() {
        return SharPrefUti.getInt(this.mContext, PrefInf.Setting.LANGUAGE, 0);
    }

    public void setLanguage(int i) {
        SharPrefUti.putInt(this.mContext, PrefInf.Setting.LANGUAGE, i);
    }

    public int getThemes() {
        return SharPrefUti.getInt(this.mContext, PrefInf.Setting.THEMES, 0);
    }

    public void setThemes(int i) {
        SharPrefUti.putInt(this.mContext, PrefInf.Setting.THEMES, i);
    }

    public boolean isInAppSound() {
        return SharPrefUti.getBoolean(this.mContext, PrefInf.Setting.IN_APP_SOUND, true);
    }

    public void setInAppSound(boolean z) {
        SharPrefUti.putBoolean(this.mContext, PrefInf.Setting.IN_APP_SOUND, z);
    }

    public boolean isShowNotifications() {
        return SharPrefUti.getBoolean(this.mContext, PrefInf.Setting.NOTIFICATIONS, true);
    }

    public void setShowNotifications(boolean z) {
        SharPrefUti.putBoolean(this.mContext, PrefInf.Setting.NOTIFICATIONS, z);
    }

    public boolean isAutoPlayNextVideo() {
        return SharPrefUti.getBoolean(this.mContext, PrefInf.Setting.AUTO_PLAY_NEXT_VIDEO, true);
    }

    public void setAutoPlayNextVideo(boolean z) {
        SharPrefUti.putBoolean(this.mContext, PrefInf.Setting.AUTO_PLAY_NEXT_VIDEO, z);
    }

    public boolean isAutoPlayNextMusic() {
        return SharPrefUti.getBoolean(this.mContext, PrefInf.Setting.AUTO_PLAY_NEXT_MUSIC, true);
    }

    public void setAutoPlayNextMusic(boolean z) {
        SharPrefUti.putBoolean(this.mContext, PrefInf.Setting.AUTO_PLAY_NEXT_MUSIC, z);
    }

    public boolean isResumeVideo() {
        return SharPrefUti.getBoolean(this.mContext, PrefInf.Setting.RESUME_VIDEO, true);
    }

    public void setResumeVideo(boolean z) {
        SharPrefUti.putBoolean(this.mContext, PrefInf.Setting.RESUME_VIDEO, z);
    }

    public int getVideoMode() {
        return SharPrefUti.getInt(this.mContext, PrefInf.Setting.VIDEO_MODE, 1);
    }

    public void setVideoMode(int i) {
        SharPrefUti.putInt(this.mContext, PrefInf.Setting.VIDEO_MODE, i);
    }

    public boolean isPitchToZoom() {
        return SharPrefUti.getBoolean(this.mContext, PrefInf.Setting.PITCH_TO_ZOOM, true);
    }

    public void setPitchToZoom(boolean z) {
        SharPrefUti.putBoolean(this.mContext, PrefInf.Setting.PITCH_TO_ZOOM, z);
    }

    public boolean isSlideForSound() {
        return SharPrefUti.getBoolean(this.mContext, PrefInf.Setting.SLIDE_FOR_SOUND, true);
    }

    public void setSlideForSound(boolean z) {
        SharPrefUti.putBoolean(this.mContext, PrefInf.Setting.SLIDE_FOR_SOUND, z);
    }

    public boolean isSlideForBrightness() {
        return SharPrefUti.getBoolean(this.mContext, PrefInf.Setting.SLIDE_FOR_BRIGHTNESS, true);
    }

    public void setSlideForBrightness(boolean z) {
        SharPrefUti.putBoolean(this.mContext, PrefInf.Setting.SLIDE_FOR_BRIGHTNESS, z);
    }
}
