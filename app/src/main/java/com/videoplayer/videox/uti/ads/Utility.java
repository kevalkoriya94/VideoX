package com.videoplayer.videox.uti.ads;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.appizona.yehiahd.fastsave.FastSave;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.videoplayer.videox.R;
import com.videoplayer.videox.activity.VidPlayActivity;
import com.videoplayer.videox.adapter.mus.MusInfAdapter;
import com.videoplayer.videox.adapter.vid.VidInfAdapter;
import com.videoplayer.videox.dialog.QueDiaBuil;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.ser.MusServ;
import com.videoplayer.videox.uti.SharPrefUti;
import com.videoplayer.videox.uti.down.FilDowUti;
import com.videoplayer.videox.uti.thre.ThrExe;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class Utility {
    public static boolean is_show_open_ad = true;
    public static int showAdsNumberCount = 1;
    public static String sCuttingVideoPath = null;
    public static String sDownloadVideoPath = null;
    public static String sHiddenVideoPath = null;
    public static String sMusicConvertPath = null;
    public static long sTimeExpires = 0;
    public static String sVideoPath = null;

    static InterstitialAd ad_mob_interstitial;
    static AdRequest interstitial_adRequest;

    public static void initAds(Context context) {
        LoadAdMobInterstitialAd(context);
    }

    public static void initFolderPath(Context context) {
        try {
            sDownloadVideoPath = FilDowUti.getMoviesPath(context) + "/" + context.getString(R.string.app_name) + "/VideoDownload/";
            new File(sDownloadVideoPath).mkdirs();
            sHiddenVideoPath = FilDowUti.getMoviesPath(context) + "/" + context.getString(R.string.app_name) + "/.Video/";
            new File(sHiddenVideoPath).mkdirs();
            sMusicConvertPath = FilDowUti.getMusicPath(context) + "/" + context.getString(R.string.app_name) + "/MusicConvert/";
            new File(sMusicConvertPath).mkdirs();
            sCuttingVideoPath = FilDowUti.getMoviesPath(context) + "/" + context.getString(R.string.app_name) + "/VideoCutting/";
            new File(sCuttingVideoPath).mkdirs();
            sVideoPath = FilDowUti.getMoviesPath(context) + "/" + context.getString(R.string.app_name) + "/Video/";
            new File(sVideoPath).mkdirs();
        } catch (Exception unused) {
        }
    }

    private static void LoadAdMobInterstitialAd(Context context) {
        // TODO Auto-generated method stub
        try {
            Bundle non_personalize_bundle = new Bundle();
            non_personalize_bundle.putString("npa", "1");

            interstitial_adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(context, FastSave.getInstance().getString("INTER", ""), interstitial_adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    ad_mob_interstitial = interstitialAd;
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    ad_mob_interstitial = null;
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static boolean checkVideoPlayerIsRunning(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> list = null;
            if (activityManager != null) {
                list = activityManager.getRunningTasks(Integer.MAX_VALUE);
            }
            if (list == null) {
                return false;
            }
            for (ActivityManager.RunningTaskInfo runningTaskInfo : list) {
                if (runningTaskInfo.topActivity.getClassName().equals(VidPlayActivity.class.getCanonicalName())) {
                    return true;
                }
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    public static String convertLongToTime(long j, String str) {
        return new SimpleDateFormat(str, Locale.ENGLISH).format(new Date(j));
    }
    public static String convertLongToDuration(long j) {
        String str;
        if (j > 3600000) {
            int i = (int) (j / 3600000);
            if (i < 10) {
                str = "0" + i + ":";
            } else {
                str = i + ":";
            }


        } else {
            str = "";
        }
        return str + String.format(String.format("%02d", (j / 60000) % 60) + ":" + String.format("%02d", (j / 1000) % 60), new Object[0]);
    }

    public static String convertSize(long j) {
        double d = ((float) j) / 1000000.0f;
        if (d > 1024.0d) {
            return new DecimalFormat("##.## GB").format(d / 1024.0d);
        }
        return new DecimalFormat("##.## MB").format(d);
    }

    public static String convertMBSize(long j) {
        return String.format(Locale.ENGLISH, "%.2f MB", j / 1048576.0d);
    }

    public static String convertSolution(String str) {
        String str2;
        String[] split = str.split("[^\\d]+");
        if (split.length >= 2) {
            str2 = split[1];
        } else {
            if (split.length == 1) {
            }
            return "720P";
        }
        try {
            long parseLong = Long.parseLong(str2);
            return parseLong < 240 ? "144P" : parseLong < 360 ? "240P" : parseLong < 480 ? "360P" : parseLong < 720 ? "480P" : parseLong < 1080 ? "720P" : parseLong < 1440 ? "1080P" : parseLong < 2160 ? "1440P" : parseLong < 4320 ? "4K" : "8K";
        } catch (NumberFormatException unused) {
        }
        return str2;
    }

    public static int getVideoSortMode(Context context) {
        return SharPrefUti.getInt(context, "VIDEO_SORT_TYPE", 0);
    }

    public static int getMusicSortMode(Context context) {
        return SharPrefUti.getInt(context, "MUSIC_SORT_TYPE", 0);
    }

    public static boolean getVideoSortAscending(Context context) {
        return SharPrefUti.getBoolean(context, "VIDEO_SORT_ASCENDING", false);
    }

    public static boolean getMusicSortAscending(Context context) {
        return SharPrefUti.getBoolean(context, "MUSIC_SORT_ASCENDING", false);
    }

    public static void setVideoSortModeAndAscending(Context context, int i, boolean z) {
        SharPrefUti.putInt(context, "VIDEO_SORT_TYPE", i);
        SharPrefUti.putBoolean(context, "VIDEO_SORT_ASCENDING", z);
    }

    public static void setMusicSortModeAndAscending(Context context, int i, boolean z) {
        SharPrefUti.putInt(context, "MUSIC_SORT_TYPE", i);
        SharPrefUti.putBoolean(context, "MUSIC_SORT_ASCENDING", z);
    }

    public static List<VideoInfo> searchVideoByVideoName(List<VideoInfo> list, String str) {
        ArrayList arrayList = new ArrayList();
        for (VideoInfo videoInfo : list) {
            if (videoInfo.getDisplayName().toLowerCase().contains(str.toLowerCase())) {
                arrayList.add(videoInfo);
            }
        }
        return arrayList;
    }

    public static List<MusicInfo> searchMusicByMusicName(List<MusicInfo> list, String str) {
        ArrayList arrayList = new ArrayList();
        for (MusicInfo musicInfo : list) {
            if (musicInfo.getDisplayName().toLowerCase().contains(str.toLowerCase())) {
                arrayList.add(musicInfo);
            }
        }
        return arrayList;
    }

    public static void deleteAVideo(final Context context, VideoInfo videoInfo) {
        final String path = videoInfo.getPath();
        ThrExe.runOnDatabaseThread(() -> Utility.deleteAVideo(path, context));
    }

    public static void deleteAVideo(String str, Context context) {
        String[] strArr = {str};
        ContentResolver contentResolver = context.getContentResolver();

        ArrayList<Uri> arrayList2 = new ArrayList<>();

        long mediaID = getFilePathToMediaID(str, context);
        Uri Uri_one = ContentUris.withAppendedId(MediaStore.Video.Media.getContentUri("external"), mediaID);
        arrayList2.add(Uri_one);
        Collections.addAll(arrayList2);

        IntentSender intentSender = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            intentSender = MediaStore.createDeleteRequest(contentResolver, arrayList2).getIntentSender();
//            IntentSenderRequest senderRequest = new IntentSenderRequest.Builder(intentSender)
//                    .setFillInIntent(null)
//                    .setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, 0)
//                    .build();
//            deleteResultLauncher.launch(senderRequest);
        } else {
            Uri contentUri = MediaStore.Files.getContentUri("external");
            contentResolver.delete(contentUri, "_data=?", strArr);
            File file = new File(str);
            if (file.exists()) {
                contentResolver.delete(contentUri, "_data=?", strArr);
            }
            Log.d("binhnk08", " deleteAVideo = " + file);
        }
    }

//    public static ActivityResultLauncher<IntentSenderRequest> deleteResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartIntentSenderForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                }
//            }
//    );

    public static long getFilePathToMediaID(String songPath, Context context) {
        long id = 0;
        ContentResolver cr = context.getContentResolver();

        Uri uri = MediaStore.Files.getContentUri("external");
        String selection = MediaStore.Images.Media.DATA;
        String[] selectionArgs = {songPath};
        String[] projection = {MediaStore.Images.Media._ID};
        String sortOrder = MediaStore.Images.Media.TITLE + " ASC";

        Cursor cursor = cr.query(uri, projection, selection + "=?", selectionArgs, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                id = Long.parseLong(cursor.getString(idIndex));
            }
        }
        return id;
    }

    public static void deleteAMusic(final Context context, MusicInfo musicInfo) {
        final String path = musicInfo.getPath();
        ThrExe.runOnDatabaseThread(() -> Utility.deleteAMusic(path, context));
    }

    public static void deleteAMusic(String str, Context context) {
        String[] strArr = {str};
        ContentResolver contentResolver = context.getContentResolver();
        Uri contentUri = MediaStore.Files.getContentUri("external");
        contentResolver.delete(contentUri, "_data=?", strArr);
        File file = new File(str);
        if (file.exists()) {
            contentResolver.delete(contentUri, "_data=?", strArr);
        }
        Log.d("binhnk08", " deleteAMusic = " + file);
    }

    public static void renameAVideo(Context context, VideoInfo videoInfo, String str, MediaScannerConnection.OnScanCompletedListener onScanCompletedListener) {
        Log.d("binhnk08", " renameAVideo = " + videoInfo.toString());
        File file = new File(videoInfo.getPath());
        File file2 = new File(file.getAbsolutePath());
        File file3 = new File(file.getAbsolutePath().replace(file.getName(), str));
        if (file3.exists()) {
            Toast.makeText(context, "File name already exists, please use another name", Toast.LENGTH_SHORT).show();
        } else if (file2.renameTo(file3)) {
            MediaScannerConnection.scanFile(context, new String[]{file3.getPath(), file2.getPath()}, null, onScanCompletedListener);
        } else {
            Toast.makeText(context, "Renaming failed", Toast.LENGTH_SHORT).show();
        }
    }

    public static void renameAMusic(Context context, MusicInfo musicInfo, String str, MediaScannerConnection.OnScanCompletedListener onScanCompletedListener) {
        Log.d("binhnk08", " renameAMusic = " + musicInfo.toString());
        File file = new File(musicInfo.getPath());
        File file2 = new File(file.getAbsolutePath());
        File file3 = new File(file.getAbsolutePath().replace(file.getName(), str));
        if (file3.exists()) {
            Toast.makeText(context, "File name already exists, please use another name", Toast.LENGTH_SHORT).show();
        } else if (file2.renameTo(file3)) {
            MediaScannerConnection.scanFile(context, new String[]{file3.getPath(), file2.getPath()}, null, onScanCompletedListener);
        } else {
            Toast.makeText(context, "Renaming failed", Toast.LENGTH_SHORT).show();
        }
    }

    public static void setHiddenAVideo(Context context, VideoInfo videoInfo) {
        File file = new File(sHiddenVideoPath);
        file.mkdirs();
        File file2 = new File(videoInfo.getPath());
        File file3 = new File(file2.getAbsolutePath());
        File file4 = new File(file.getPath() + "/" + file2.getName());
        boolean renameTo = file3.renameTo(file4);
        MediaScannerConnection.scanFile(context, new String[]{file4.getPath(), file3.getPath()}, null, (path, uri) -> {
        });
    }

    public static boolean setUnhiddenAVideo(Context context, VideoInfo videoInfo) {
        File file = new File(sVideoPath);
        file.mkdirs();
        File file2 = new File(videoInfo.getPath());
        if (!file2.exists()) {
            return false;
        }
        File file3 = new File(file2.getAbsolutePath());
        File file4 = new File(file.getPath() + "/" + file2.getName());
        boolean renameTo = file3.renameTo(file4);
        MediaScannerConnection.scanFile(context, new String[]{file4.getPath(), file3.getPath()}, null, (str, uri) -> {
        });
        Log.d("binhnk08", " setUniddenAVideo:success = " + renameTo + " newPath =" + file3.getPath());
        return true;
    }

    public static boolean isFileExist(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return new File(str).exists();
    }

    public static void shareVideo(Context context, VideoInfo videoInfo) {
        Uri uri;
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setType("video/*");
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(videoInfo.getPath()));
            } else {
                uri = Uri.parse(videoInfo.getUri());
            }
            intent.putExtra("android.intent.extra.STREAM", uri);
            if (Build.VERSION.SDK_INT >= 24) {
                intent.addFlags(1);
            }
            context.startActivities(new Intent[]{Intent.createChooser(intent, "Share via")});
        } catch (Exception unused) {
        }
    }

    public static void shareMusic(Context context, MusicInfo musicInfo) {
        Uri uri;
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setType("audio/*");
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(musicInfo.getPath()));
            } else {
                uri = Uri.fromFile(new File(musicInfo.getPath()));
            }
            intent.putExtra("android.intent.extra.STREAM", uri);
            if (Build.VERSION.SDK_INT >= 24) {
                intent.addFlags(1);
            }
            context.startActivities(new Intent[]{Intent.createChooser(intent, "Share via")});
        } catch (Exception unused) {
        }
    }

    public static void convertToMp3(Context context, String str, VidInfAdapter.Callback callback) {
        sMusicConvertPath = FilDowUti.getMusicPath(context) + "/" + context.getString(R.string.app_name) + "/MusicConvert/";
        new File(sMusicConvertPath).mkdirs();
        String str2 = sMusicConvertPath + ("Mp3_Converter_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date())) + ".mp3";
        Dialog dialog = new Dialog(context);
        Window window = dialog.getWindow();
        Objects.requireNonNull(window);
        window.requestFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_loading_request_video);
        dialog.setCancelable(false);
        ((TextView) dialog.findViewById(R.id.tv_action)).setText(R.string.convert_mp3_wait_moment);
        dialog.show();

        Log.e("TAG", str + " convertToMp3: " + str2);
        FFmpeg.executeAsync(new String[]{"-i", str, "-vn", str2}, new AnonymousClass4(context, str2, callback, dialog));
    }

    public static void convertToMp32(Context context, String str, MusInfAdapter.Callback callback) {
        sMusicConvertPath = FilDowUti.getMusicPath(context) + "/" + context.getString(R.string.app_name) + "/MusicConvert/";
        new File(sMusicConvertPath).mkdirs();
        String str2 = sMusicConvertPath + ("Mp3_Converter_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date())) + ".mp3";
        Dialog dialog = new Dialog(context);
        Window window = dialog.getWindow();
        Objects.requireNonNull(window);
        window.requestFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_loading_request_video);
        dialog.setCancelable(false);
        ((TextView) dialog.findViewById(R.id.tv_action)).setText(R.string.convert_mp3_wait_moment);
        dialog.show();
        FFmpeg.executeAsync(new String[]{"-i", str, "-vn", str2}, new AnonymousClass5(context, str2, callback, dialog));
    }

    public static class AnonymousClass4 implements ExecuteCallback {
        final VidInfAdapter.Callback val$callback;
        final Context val$context;
        final String val$filePath;
        final Dialog val$progressDialog;

        AnonymousClass4(Context context, String str, VidInfAdapter.Callback callback, Dialog dialog) {
            this.val$context = context;
            this.val$filePath = str;
            this.val$callback = callback;
            this.val$progressDialog = dialog;
        }

        @Override
        public void apply(long j, int i) {
            if (i == 0) {
                final Context context = this.val$context;
                final String str = this.val$filePath;
                MediaScannerConnection.scanFile(context, new String[]{str}, null, (str2, uri) -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        try {
                            new QueDiaBuil(context, new AnonymousClass1(str, context, val$callback))
                                    .setTitle(R.string.notice, ContextCompat.getColor(context, R.color.main_orange))
                                    .setOkButtonText(R.string.play)
                                    .setCancelButtonText(R.string.got_it)
                                    .setQuestion(R.string.mp3_convert_done).build().show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                });
            } else {
                Toast.makeText(this.val$context, "No Audio Found", Toast.LENGTH_SHORT).show();
            }
            this.val$progressDialog.dismiss();
        }

        public static class AnonymousClass1 implements QueDiaBuil.OkButtonClickListener {
            final VidInfAdapter.Callback val$callback;
            final Context val$context;
            final String val$filePath;

            @Override
            public void onCancelClick() {
            }

            AnonymousClass1(String str, Context context, VidInfAdapter.Callback callback) {
                this.val$filePath = str;
                this.val$context = context;
                this.val$callback = callback;
            }

            @Override
            public void onOkClick() {
                File file = new File(this.val$filePath);
                if (file.exists()) {
                    final MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setId(-1L);
                    musicInfo.setUri(Uri.fromFile(file).toString());
                    musicInfo.setPath(this.val$filePath);
                    musicInfo.setDisplayName(file.getName());
                    Intent intent = new Intent(this.val$context, MusServ.class);
                    this.val$context.startService(intent);
                    this.val$context.bindService(intent, new ServiceConnection() {
                        @Override
                        public void onServiceDisconnected(ComponentName componentName) {
                        }

                        @Override
                        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                            MusServ service = ((MusServ.MyBinder) iBinder).getService();
                            if (service != null) {
                                service.setMusicInfoList(new ArrayList(Collections.singletonList(musicInfo)));
                                service.setIndex(0);
                                service.setRepeatState(0);
                                service.playMusic();
                            }
                            AnonymousClass1.this.val$context.unbindService(this);
                        }
                    }, 1);
                    final VidInfAdapter.Callback callback = this.val$callback;
                    ((Activity) this.val$context).runOnUiThread(() -> {
                        if (callback != null) {
                            callback.onBottomNaviUpdate();
                            callback.onFavoriteUpdate(0, false);
                        }
                    });
                    Toast.makeText(this.val$context, R.string.successfully + " converted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static class AnonymousClass5 implements ExecuteCallback {
        final MusInfAdapter.Callback val$callback;
        final Context val$context;
        final String val$filePath;
        final Dialog val$progressDialog;

        AnonymousClass5(Context context, String str, MusInfAdapter.Callback callback, Dialog dialog) {
            this.val$context = context;
            this.val$filePath = str;
            this.val$callback = callback;
            this.val$progressDialog = dialog;
        }

        @Override
        public void apply(long j, int i) {
            if (i == 0) {
                final Context context = this.val$context;
                final String str = this.val$filePath;
                MediaScannerConnection.scanFile(context, new String[]{str}, null, (str2, uri) -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        try {
                            new QueDiaBuil(context, new AnonymousClass2(str, context, this.val$callback))
                                    .setTitle(R.string.notice, ContextCompat.getColor(context, R.color.main_orange))
                                    .setOkButtonText(R.string.play).setCancelButtonText(R.string.got_it).setQuestion(R.string.mp3_convert_done).build().show();
                        } catch (Exception e) {
                            new QueDiaBuil(context, new AnonymousClass2(str, context, this.val$callback))
                                    .setTitle(R.string.notice, ContextCompat.getColor(context, R.color.main_orange))
                                    .setOkButtonText(R.string.play).setCancelButtonText(R.string.got_it)
                                    .setQuestion(R.string.mp3_convert_done).build().show();
                            e.printStackTrace();
                        }
                    });
                });
            } else {
                Toast.makeText(this.val$context, "No Audio Found", Toast.LENGTH_SHORT).show();
            }
            val$callback.onFavoriteUpdate(0, true);
            this.val$progressDialog.dismiss();
            Log.d("binhnk08", "apply " + val$filePath);
        }


        public static class AnonymousClass1 implements QueDiaBuil.OkButtonClickListener {
            final VidInfAdapter.Callback val$callback;
            final Context val$context;
            final String val$filePath;

            @Override
            public void onCancelClick() {
            }

            AnonymousClass1(String str, Context context, VidInfAdapter.Callback callback) {
                this.val$filePath = str;
                this.val$context = context;
                this.val$callback = callback;
            }

            @Override
            public void onOkClick() {
                File file = new File(this.val$filePath);
                if (file.exists()) {
                    final MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setId(-1L);
                    musicInfo.setUri(Uri.fromFile(file).toString());
                    musicInfo.setPath(this.val$filePath);
                    musicInfo.setDisplayName(file.getName());
                    Intent intent = new Intent(this.val$context, MusServ.class);
                    this.val$context.startService(intent);
                    this.val$context.bindService(intent, new ServiceConnection() {
                        @Override
                        public void onServiceDisconnected(ComponentName componentName) {
                        }

                        @Override
                        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                            MusServ service = ((MusServ.MyBinder) iBinder).getService();
                            if (service != null) {
                                service.setMusicInfoList(new ArrayList(Collections.singletonList(musicInfo)));
                                service.setIndex(0);
                                service.setRepeatState(0);
                                service.playMusic();
                            }
                            AnonymousClass1.this.val$context.unbindService(this);
                        }
                    }, 1);
                    final VidInfAdapter.Callback callback = this.val$callback;
                    ((Activity) this.val$context).runOnUiThread(() -> {
                        if (callback != null) {
                            callback.onBottomNaviUpdate();
                        }
                    });
                }
            }
        }

        public static class AnonymousClass2 implements QueDiaBuil.OkButtonClickListener {
            final MusInfAdapter.Callback val$callback;
            final Context val$context;
            final String val$filePath;

            @Override
            public void onCancelClick() {
            }

            AnonymousClass2(String str, Context context, MusInfAdapter.Callback callback) {
                this.val$filePath = str;
                this.val$context = context;
                this.val$callback = callback;
            }

            @Override
            public void onOkClick() {
                File file = new File(this.val$filePath);
                if (file.exists()) {
                    final MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setId(-1L);
                    musicInfo.setUri(Uri.fromFile(file).toString());
                    musicInfo.setPath(this.val$filePath);
                    musicInfo.setDisplayName(file.getName());
                    Intent intent = new Intent(this.val$context, MusServ.class);
                    this.val$context.startService(intent);
                    this.val$context.bindService(intent, new ServiceConnection() {
                        @Override
                        public void onServiceDisconnected(ComponentName componentName) {
                        }

                        @Override
                        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                            MusServ service = ((MusServ.MyBinder) iBinder).getService();
                            if (service != null) {
                                service.setMusicInfoList(new ArrayList(Collections.singletonList(musicInfo)));
                                service.setIndex(0);
                                service.setRepeatState(0);
                                service.playMusic();
                            }
                            AnonymousClass2.this.val$context.unbindService(this);
                        }
                    }, 1);
                    final MusInfAdapter.Callback callback = this.val$callback;
                    ((Activity) this.val$context).runOnUiThread(() -> {
                        if (callback != null) {
                            callback.onFavoriteUpdate(0, true);
                        }
                    });
                }
            }
        }
    }

    public static int getColorAttr(Context context, int i) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(i, typedValue, true);
        return ContextCompat.getColor(context, typedValue.resourceId);
    }

    public static int getColorAttrr(Context context, int i) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(i, typedValue, true);
        return Color.WHITE;
    }
}
