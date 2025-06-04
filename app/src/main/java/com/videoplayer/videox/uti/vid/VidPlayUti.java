package com.videoplayer.videox.uti.vid;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.Format;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.videoplayer.videox.R;
import com.videoplayer.videox.activity.VidPlayActivity;
import com.videoplayer.videox.cv.CustStyPlayView;
import com.videoplayer.videox.uti.SharPrefUti;

import java.io.File;


public class VidPlayUti {
    public static int boostLevel;

    public static String getRealPathFromURI(Context context, Uri uri) {
        Log.d("binhnk08", " getRealPathFromURI contentURI = " + uri);
        String realPathFromURI2 = getRealPathFromURI2(uri);
        if (realPathFromURI2 != null && realPathFromURI2.substring(realPathFromURI2.lastIndexOf(File.separatorChar)).contains(".")) {
            return realPathFromURI2;
        }
        try {
            Cursor query = context.getContentResolver().query(uri, null, null, null, null);
            if (query != null && query.moveToFirst()) {
                String string = query.getString(query.getColumnIndexOrThrow("_data"));
                query.close();
                return string;
            }
            return realPathFromURI2;
        } catch (Exception unused) {
            return "";
        }
    }

    private static String getRealPathFromURI2(Uri uri) {
        String path = uri.getPath();
        if (path == null) {
            return null;
        }
        try {
            String substring = path.substring(path.indexOf(File.separator) + 1);
            String substring2 = substring.substring(substring.indexOf(File.separator) + 1);
            if ((File.separator + substring2).contains(Environment.getExternalStorageDirectory().toString())) {
                return File.separator + substring2;
            }
            return Environment.getExternalStorageDirectory().toString() + File.separator + substring2;
        } catch (Exception unused) {
            return null;
        }
    }

    public static int dpToPx(int i) {
        return (int) (i * Resources.getSystem().getDisplayMetrics().density);
    }

    public static float pxToDp(float f) {
        return f / Resources.getSystem().getDisplayMetrics().density;
    }

    public static boolean fileExists(Context context, Uri uri) {
        if ("file".equals(uri.getScheme())) {
            return new File(uri.getPath()).exists();
        }
        try {
            context.getContentResolver().openInputStream(uri).close();
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static void hideSystemUi(CustStyPlayView customStyledPlayerView) {
        customStyledPlayerView.setSystemUiVisibility(4871);
    }

    public static void showSystemUi(CustStyPlayView customStyledPlayerView) {
        customStyledPlayerView.setSystemUiVisibility(1792);
    }

    public static String getFileName(Context context, Uri uri) {
        int lastIndexOf;
        int columnIndex;
        String str = null;
        try {
            if (FirebaseAnalytics.Param.CONTENT.equals(uri.getScheme())) {
                Cursor query = context.getContentResolver().query(uri, new String[]{"_display_name"}, null, null, null);
                if (query != null && query.moveToFirst() && (columnIndex = query.getColumnIndex("_display_name")) > -1) {
                    str = query.getString(columnIndex);
                }
                if (query != null) {
                    query.close();
                }
            }
            if (str == null && (lastIndexOf = (str = uri.getPath()).lastIndexOf(47)) != -1) {
                str = str.substring(lastIndexOf + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str == null ? "" : str;
    }

    public static boolean isVolumeMax(AudioManager audioManager) {
        return audioManager.getStreamVolume(3) == audioManager.getStreamMaxVolume(3);
    }

    public static boolean isVolumeMin(AudioManager audioManager) {
        return audioManager.getStreamVolume(3) == (Build.VERSION.SDK_INT >= 28 ? audioManager.getStreamMinVolume(3) : 0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:53:0x0078, code lost:
    
        r6 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void adjustVolume(Context context, CustStyPlayView customStyledPlayerView, boolean z, boolean z2) {
        int i;
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        if (audioManager != null) {
            int streamVolume = audioManager.getStreamVolume(3);
            int streamMaxVolume = audioManager.getStreamMaxVolume(3);
            if (streamVolume != streamMaxVolume) {
                boostLevel = 0;
            }
            if (streamVolume != streamMaxVolume || ((i = boostLevel) == 0 && !z)) {
                if (VidPlayActivity.loudnessEnhancer != null) {
                    VidPlayActivity.loudnessEnhancer.setEnabled(false);
                }
                try {
                    audioManager.adjustStreamVolume(3, z ? 1 : -1, 8);
                    int streamVolume2 = audioManager.getStreamVolume(3);
                    if (z && streamVolume == streamVolume2 && !isVolumeMin(audioManager)) {
                        audioManager.adjustStreamVolume(3, 1, 9);
                    }
                    boolean z3 = false;
                    customStyledPlayerView.showVolume(streamVolume2, z3);
                } catch (SecurityException unused) {
                }
            } else {
                if (z && i < 10) {
                    boostLevel = i + 1;
                } else if (!z && i > 0) {
                    boostLevel = i - 1;
                }
                if (VidPlayActivity.loudnessEnhancer != null && z2) {
                    try {
                        VidPlayActivity.loudnessEnhancer.setTargetGain(boostLevel * 200);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
                customStyledPlayerView.showVolume(streamMaxVolume + boostLevel, true);
            }
            if (VidPlayActivity.loudnessEnhancer != null) {
                VidPlayActivity.loudnessEnhancer.setEnabled(boostLevel > 0);
            }
        }
    }

    public static void setButtonEnabled(Context context, ImageView imageView, boolean z) {
        imageView.setEnabled(z);
        imageView.setAlpha(context.getResources().getInteger(z ? com.google.android.exoplayer2.R.integer.exo_media_button_opacity_percentage_enabled : com.google.android.exoplayer2.R.integer.exo_media_button_opacity_percentage_disabled) / 100.0f);
    }

    public static void showText(CustStyPlayView customStyledPlayerView, String str, long j) {
        customStyledPlayerView.removeCallbacks(customStyledPlayerView.textClearRunnable);
        customStyledPlayerView.clearIcon();
        customStyledPlayerView.setCustomErrorMessage(str);
        customStyledPlayerView.postDelayed(customStyledPlayerView.textClearRunnable, j);
    }

    public static void showText(CustStyPlayView customStyledPlayerView, String str) {
        showText(customStyledPlayerView, str, 1200L);
    }

    public enum Orientation {
        PORTRAIT(0, R.string.video_orientation_portrait),
        LANDSCAPE(1, R.string.video_orientation_landscape),
        SENSOR(2, R.string.video_orientation_auto);

        public final int description;
        public final int value;

        Orientation(int i, int i2) {
            this.value = i;
            this.description = i2;
        }
    }

    public static class AnonymousClass1 {

        /* renamed from: $SwitchMap$com$video$player$freeplayer$nixplay$zy$play$util$video$VideoPlayerUtils$Orientation */
        static final int[] f588x418caa4e;

        static {
            int[] iArr = new int[Orientation.values().length];
            f588x418caa4e = iArr;
            try {
                iArr[Orientation.LANDSCAPE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f588x418caa4e[Orientation.PORTRAIT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f588x418caa4e[Orientation.SENSOR.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public static void setOrientation(Activity activity, Orientation orientation) {
        int i = AnonymousClass1.f588x418caa4e[orientation.ordinal()];
        if (i == 1) {
            activity.setRequestedOrientation(6);
        } else if (i == 2) {
            activity.setRequestedOrientation(7);
        } else if (i == 3) {
            activity.setRequestedOrientation(4);
        }
    }

    public static Orientation getNextOrientation(Orientation orientation) {
        int i = AnonymousClass1.f588x418caa4e[orientation.ordinal()];
        if (i == 2) {
            return Orientation.LANDSCAPE;
        }
        if (i != 3) {
            return Orientation.SENSOR;
        }
        return Orientation.PORTRAIT;
    }

    public static boolean isRotated(Format format) {
        return format.rotationDegrees == 90 || format.rotationDegrees == 270;
    }

    public static boolean isPortrait(Format format) {
        if (isRotated(format)) {
            if (format.width > format.height) {
                return true;
            }
        } else if (format.height > format.width) {
            return true;
        }
        return false;
    }

    public static String formatMilis(long j) {
        int abs = Math.abs(((int) j) / 1000);
        int i = abs % 60;
        int i2 = (abs % 3600) / 60;
        int i3 = abs / 3600;
        return i3 > 0 ? String.format("%d:%02d:%02d", Integer.valueOf(i3), Integer.valueOf(i2), Integer.valueOf(i)) : String.format("%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i));
    }

    public static String formatMilisSign(long j) {
        if (j > -1000 && j < 1000) {
            return formatMilis(j);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(j < 0 ? "−" : "+");
        sb.append(formatMilis(j));
        return sb.toString();
    }

    public static void setViewParams(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        view.setPadding(i, i2, i3, i4);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(i5, i6, i7, i8);
        view.setLayoutParams(layoutParams);
    }

    public static void setViewParams2(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        view.setPadding(i, i2, i3, i4);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(i5, i6, i7, i8);
        view.setLayoutParams(layoutParams);
    }

    public static void setViewParams3(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        view.setPadding(i, i2, i3, i4);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(i5, i6, i7, i8);
        view.setLayoutParams(layoutParams);
    }

    public static boolean isDeletable(Context context, Uri uri) {
        int columnIndex;
        try {
            if (FirebaseAnalytics.Param.CONTENT.equals(uri.getScheme())) {
                Cursor query = context.getContentResolver().query(uri, new String[]{"flags"}, null, null, null);
                if (query != null && query.moveToFirst() && (columnIndex = query.getColumnIndex("flags")) > -1) {
                    boolean z = (query.getInt(columnIndex) & 4) == 4;
                    if (query != null) {
                        query.close();
                    }
                    return z;
                }
                if (query != null) {
                    query.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isSupportedNetworkUri(Uri uri) {
        String scheme = uri.getScheme();
        return scheme.startsWith("http") || scheme.equals("rtsp");
    }

    public static boolean isTvBox(Activity activity) {
        return activity.getResources().getBoolean(R.bool.tv_box);
    }

    public static boolean isShownGuide(Context context) {
        return SharPrefUti.getBoolean(context, "SHOWN_GUIDE", false);
    }

    public static void setShownGuide(Context context) {
        SharPrefUti.putBoolean(context, "SHOWN_GUIDE", true);
    }
}
