package com.videoplayer.videox.uti.down;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;


public class FilDowUti {
    public static String getMoviesPath(Context context) {
        try {
            File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (externalStoragePublicDirectory != null) {
                return externalStoragePublicDirectory.getAbsolutePath();
            }
            File externalStoragePublicDirectory2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            if (externalStoragePublicDirectory2 != null) {
                return externalStoragePublicDirectory2.getAbsolutePath();
            }
            return getCacheDir(context);
        } catch (Exception unused) {
            return getCacheDir(context);
        }
    }

    public static String getMusicPath(Context context) {
        try {
            File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            if (externalStoragePublicDirectory != null) {
                return externalStoragePublicDirectory.getAbsolutePath();
            }
            File externalStoragePublicDirectory2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (externalStoragePublicDirectory2 != null) {
                return externalStoragePublicDirectory2.getAbsolutePath();
            }
            return getCacheDir(context);
        } catch (Exception unused) {
            return getCacheDir(context);
        }
    }

    public static String getCacheDir(Context context) {
        File file;
        try {
            if (Environment.getExternalStorageState().equals("mounted")) {
                file = context.getExternalCacheDir();
                if (file == null || !file.exists()) {
                    file = getExternalCacheDirManual(context);
                }
            } else {
                file = null;
            }
            if (file == null && ((file = context.getCacheDir()) == null || !file.exists())) {
                file = getCacheDirManual(context);
            }
            Log.w("binhnk08", "cache dir = " + file.getAbsolutePath());
            return file.getAbsolutePath();
        } catch (Throwable unused) {
            return "";
        }
    }

    private static File getExternalCacheDirManual(Context context) {
        File file = new File(new File(new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data"), context.getPackageName()), "cache");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.w("binhnk08", "Unable to create external cache directory");
                return null;
            }
            try {
                new File(file, ".nomedia").createNewFile();
            } catch (IOException unused) {
                Log.i("binhnk08", "Can't create \".nomedia\" file in application external cache directory");
            }
        }
        return file;
    }

    private static File getCacheDirManual(Context context) {
        return new File("/data/data/" + context.getPackageName() + "/cache");
    }
}
