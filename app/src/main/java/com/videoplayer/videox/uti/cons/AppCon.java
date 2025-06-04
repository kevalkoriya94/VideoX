package com.videoplayer.videox.uti.cons;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.videoplayer.videox.R;


public final class AppCon {
    public static final String REMOVE_ADS_KEY = "REMOVE_ADS";
    public static final String REMOVE_ADS_PRODUCT_ID = "ad.free_remove.ads";
    static boolean isConnected = false;
    public static boolean is_open = true;

    public static class IntentExtra {
        public static final String EXTRA_DARK_MODE_CHANGE = "EXTRA_DARK_MODE_CHANGE";
        public static final String EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION";
        public static final String EXTRA_MUSIC_ARRAY = "EXTRA_MUSIC_ARRAY";
        public static final String EXTRA_MUSIC_NUMBER = "EXTRA_MUSIC_NUMBER";
        public static final String EXTRA_MUSIC_PLAYING = "EXTRA_MUSIC_PLAYING";
        public static final String EXTRA_MUSIC_SONG = "EXTRA_MUSIC_SONG";
        public static final String EXTRA_VIDEO_AFTER_DOWNLOAD = "EXTRA_VIDEO_AFTER_DOWNLOAD";
        public static final String EXTRA_VIDEO_ARRAY = "EXTRA_VIDEO_ARRAY";
        public static final String EXTRA_VIDEO_AUDIO_MODE = "EXTRA_VIDEO_AUDIO_MODE";
        public static final String EXTRA_VIDEO_HIDDEN = "EXTRA_VIDEO_HIDDEN";
        public static final String EXTRA_VIDEO_NUMBER = "EXTRA_VIDEO_NUMBER";
        public static final String EXTRA_VIDEO_PATH_TRIMMER = "EXTRA_VIDEO_PATH_TRIMMER";
        public static final String EXTRA_VIDEO_URL = "EXTRA_VIDEO_URL";
    }

    public static class Themes {
        public static final int[] THEMES = {R.drawable.theme_1, R.drawable.theme_2};
        public static final int[] THEMES_STYLE = {R.style.Theme_MediaPlayer_LightMode};
        public static final int[] THEMES_COIN = {0, 0};
    }

    public static Boolean isOnline(Context mContext) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.getType() == 1) {
                isConnected = true;
            } else if (activeNetworkInfo.getType() == 0) {
                isConnected = true;
            }
        } else {
            isConnected = false;
        }
        return Boolean.valueOf(isConnected);
    }
}
