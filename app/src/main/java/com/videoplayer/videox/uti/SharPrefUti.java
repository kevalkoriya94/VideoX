package com.videoplayer.videox.uti;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SharPrefUti {
    public static void putTotalSize(Context context, long i) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putLong("TotalSize", i);
        edit.apply();
    }

    public static long getTotalSize(Context context, long i) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences != null ? defaultSharedPreferences.getLong("TotalSize", i) : i;
    }

    public static void putTotalSizeAudio(Context context, int i) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putInt("TotalSizeAudio", i);
        edit.apply();
    }

    public static int getTotalSizeAudio(Context context, int i) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences != null ? defaultSharedPreferences.getInt("TotalSizeAudio", i) : i;
    }

    public static void putInt(Context context, String str, int i) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putInt(str, i);
        edit.apply();
    }

    public static void putString(Context context, String str, String str2) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static void putFloat(Context context, String str, float f) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putFloat(str, f);
        edit.apply();
    }

    public static void putBoolean(Context context, String str, boolean z) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putBoolean(str, z);
        edit.apply();
    }

    public static void putLong(Context context, String str, long j) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putLong(str, j);
        edit.apply();
    }

    public static int getInt(Context context, String str, int i) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences != null ? defaultSharedPreferences.getInt(str, i) : i;
    }

    public static String getString(Context context, String str, String str2) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences != null ? defaultSharedPreferences.getString(str, str2) : str2;
    }

    public static float getFloat(Context context, String str, float f) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences != null ? defaultSharedPreferences.getFloat(str, f) : f;
    }

    public static boolean getBoolean(Context context, String str, boolean z) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (defaultSharedPreferences != null) {
            return defaultSharedPreferences.getBoolean(str, z);
        }
        return false;
    }

    public static long getLong(Context context, String str, long j) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences != null ? defaultSharedPreferences.getLong(str, j) : j;
    }
}
