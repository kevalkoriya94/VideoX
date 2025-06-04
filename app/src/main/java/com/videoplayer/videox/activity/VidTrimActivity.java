package com.videoplayer.videox.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.exifinterface.media.ExifInterface;

import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.videoplayer.videox.R;
import com.videoplayer.videox.dialog.QueDiaBuil;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;
import com.videoplayer.videox.uti.vid.VidPlayUti;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class VidTrimActivity extends AppCompatActivity {
    int duration;
    ImageView ivPlayPause;
    LibVLC mLibVLC;
    MediaPlayer mediaPlayer;
    RangeSeekBar<Integer> rangeSeekBar;
    TextView tvEnd;
    TextView tvStart;
    String videoPath;
    VLCVideoLayout vlcVideoLayout;
    private final Handler handlerSeekbar = new Handler();
    private final Runnable runnableSeekbar = new Runnable() { // from class: com.videoplayer.videox.activity.VidTrimActivity.1
        @Override // java.lang.Runnable
        public void run() {
            if (VidTrimActivity.this.mediaPlayer != null && VidTrimActivity.this.mediaPlayer.getTime() >= VidTrimActivity.this.rangeSeekBar.getSelectedMaxValue().intValue()) {
                VidTrimActivity.this.mediaPlayer.setTime(VidTrimActivity.this.rangeSeekBar.getSelectedMinValue().intValue());
            }
            VidTrimActivity.this.handlerSeekbar.postDelayed(this, 500L);
        }
    };

    @Override 
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.getDecorView().setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.ui_controls_background));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.ui_controls_background));
        if (Build.VERSION.SDK_INT >= 28) {
            window.getAttributes().layoutInDisplayCutoutMode = 1;
        }
        setContentView(R.layout.activity_video_trimmer);
        this.videoPath = getIntent().getStringExtra(AppCon.IntentExtra.EXTRA_VIDEO_PATH_TRIMMER);
        this.vlcVideoLayout = findViewById(R.id.vlc_video_view);
        this.rangeSeekBar = findViewById(R.id.range_seek_bar);
        this.tvStart = findViewById(R.id.tv_start);
        this.tvEnd = findViewById(R.id.tv_end);
        this.ivPlayPause = findViewById(R.id.icon_video_play);
        this.rangeSeekBar.setNotifyWhileDragging(false);
        this.rangeSeekBar.setScrollBarSize(VidPlayUti.dpToPx(10));
        this.rangeSeekBar.setTextAboveThumbsColor(R.color.transparent);
        this.rangeSeekBar.setTextAboveThumbsColorResource(R.color.transparent);
        this.rangeSeekBar.setOnRangeSeekBarChangeListener((RangeSeekBar.OnRangeSeekBarChangeListener)
                (rangeSeekBar, obj, obj2) ->
                        onCreate0VideoTrimmerActivity(rangeSeekBar, (Integer) obj, (Integer) obj2));

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidTrimActivity$$ExternalSyntheticLambda3
            @Override 
            public void onClick(View view) {
                VidTrimActivity.this.m547xa2ca9878(view);
            }
        });
        findViewById(R.id.tv_cut).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidTrimActivity$$ExternalSyntheticLambda4
            @Override 
            public void onClick(View view) {
                VidTrimActivity.this.m548xa2543279(view);
            }
        });
        this.ivPlayPause.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidTrimActivity$$ExternalSyntheticLambda5
            @Override 
            public void onClick(View view) {
                VidTrimActivity.this.onCreate3VideoTrimmerActivity(view);
            }
        });
        AdmobAdsHelper.smallnativeAds(this, findViewById(R.id.layout_ads), findViewById(R.id.adspace), findViewById(R.id.native_banner_ad_container), 1);
    }

    /* renamed from: lambda$onCreate$1$com-videoplayer-videox-activity-VidTrimActivity */
    void m547xa2ca9878(View view) {
        onBackPressed();
    }

    /* renamed from: lambda$onCreate$2$com-videoplayer-videox-activity-VidTrimActivity */
    void m548xa2543279(View view) {
        trimVideo();
    }

    public void onCreate0VideoTrimmerActivity(RangeSeekBar rangeSeekBar, Integer num, Integer num2) {
        this.mediaPlayer.setTime(rangeSeekBar.getSelectedMinValue().longValue());
        this.tvStart.setText(Utility.convertLongToDuration(rangeSeekBar.getSelectedMinValue().longValue()));
        this.tvEnd.setText(Utility.convertLongToDuration(rangeSeekBar.getSelectedMaxValue().longValue()));
    }

    public void onCreate3VideoTrimmerActivity(View view) {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null) {
            return;
        }
        if (mediaPlayer.isPlaying()) {
            this.mediaPlayer.pause();
        } else {
            this.mediaPlayer.play();
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        ArrayList arrayList = new ArrayList();
        arrayList.add("--no-drop-late-frames");
        arrayList.add("--no-skip-frames");
        arrayList.add("--rtsp-tcp");
        arrayList.add("-vvv");
        this.mLibVLC = new LibVLC(this, arrayList);
        if (this.mediaPlayer == null) {
            this.mediaPlayer = new MediaPlayer(this.mLibVLC);
        }
        this.mediaPlayer.setEventListener(new MediaPlayer.EventListener() { // from class: com.videoplayer.videox.activity.VidTrimActivity$$ExternalSyntheticLambda0
            @Override // org.videolan.libvlc.VLCEvent.Listener
            public void onEvent(MediaPlayer.Event event) {
                VidTrimActivity.this.m863lambda$onStart$3$comvideoplayervideoxactivityVidTrimActivity(event);
            }
        });
        try {
            this.mediaPlayer.attachViews(this.vlcVideoLayout, null, true, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Media media = new Media(this.mLibVLC, this.videoPath);
        media.setHWDecoderEnabled(true, false);
        this.mediaPlayer.setMedia(media);
        this.mediaPlayer.play();
        media.release();
        this.handlerSeekbar.post(this.runnableSeekbar);
    }

    /* renamed from: onStart4VideoTrimmerActivity, reason: merged with bridge method [inline-methods] */
    public void m863lambda$onStart$3$comvideoplayervideoxactivityVidTrimActivity(MediaPlayer.Event event) {
        int i = event.type;
        if (i == 258) {
            if (this.duration <= 0) {
                this.duration = (int) this.mediaPlayer.getLength();
                this.rangeSeekBar.setRangeValues(0, Integer.valueOf(this.duration));
                this.rangeSeekBar.setSelectedMaxValue(Integer.valueOf(this.duration));
                this.rangeSeekBar.setSelectedMinValue(0);
                this.tvStart.setText("00:00");
                this.tvEnd.setText(Utility.convertLongToDuration(this.duration));
                return;
            }
            return;
        }
        if (i == 265) {
            MediaPlayer mediaPlayer = this.mediaPlayer;
            mediaPlayer.setMedia(mediaPlayer.getMedia());
            this.mediaPlayer.play();
            this.mediaPlayer.setTime(this.rangeSeekBar.getSelectedMinValue().intValue());
            return;
        }
        if (i != 260) {
            if (i == 261) {
                this.ivPlayPause.setImageResource(R.drawable.baseline_play_circle_24);
                return;
            }
            return;
        }
        if (this.duration <= 0) {
            this.duration = (int) this.mediaPlayer.getLength();
            this.rangeSeekBar.setRangeValues(0, Integer.valueOf(this.duration));
            this.rangeSeekBar.setSelectedMaxValue(Integer.valueOf(this.duration));
            this.rangeSeekBar.setSelectedMinValue(0);
            this.tvStart.setText("00:00");
            this.tvEnd.setText(Utility.convertLongToDuration(this.duration));
        }
        this.ivPlayPause.setImageResource(R.drawable.baseline_pause_circle_24);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.mediaPlayer.detachViews();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
            this.mLibVLC.release();
        }
        this.handlerSeekbar.removeCallbacks(this.runnableSeekbar);
    }

    private void trimVideo() {
        try {
            new File(Utility.sCuttingVideoPath).mkdirs();
            final String str = Utility.sCuttingVideoPath + "Video_Player_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".mp4";
            long intValue = this.rangeSeekBar.getSelectedMinValue().intValue();
            long intValue2 = this.rangeSeekBar.getSelectedMaxValue().intValue() - intValue;
            if (intValue2 < ExoPlayer.DEFAULT_DETACH_SURFACE_TIMEOUT_MS) {
                Toast.makeText(this, R.string.video_too_short, Toast.LENGTH_SHORT).show();
                return;
            }
            String[] strArr = {"-ss", "" + (intValue / 1000), "-y", "-i", this.videoPath, "-t", "" + (intValue2 / 1000), "-vcodec", "mpeg4", "-b:v", "2097152", "-b:a", "48000", "-ac", ExifInterface.GPS_MEASUREMENT_2D, "-ar", "22050", str};
            final Dialog dialog = new Dialog(this);
            Window window = dialog.getWindow();
            Objects.requireNonNull(window);
            window.requestFeature(1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setContentView(R.layout.dialog_loading_request_video);
            dialog.setCancelable(false);
            AdmobAdsHelper.smallnativeAds(this, dialog.findViewById(R.id.layout_ads), dialog.findViewById(R.id.adspace), dialog.findViewById(R.id.native_banner_ad_container), 1);
            ((TextView) dialog.findViewById(R.id.tv_action)).setText(R.string.cutting_video_wait_moment);
            dialog.show();
            FFmpeg.executeAsync(strArr, new ExecuteCallback() { // from class: com.videoplayer.videox.activity.VidTrimActivity$$ExternalSyntheticLambda6
                @Override // com.arthenica.mobileffmpeg.ExecuteCallback
                public void apply(long j, int i) {
                    VidTrimActivity.this.m549xeb103a5f(str, dialog, j, i);
                }
            });
        } catch (Exception unused) {
        }
    }

    /* renamed from: trimVideo7VideoTrimmerActivity, reason: merged with bridge method [inline-methods] */
    public void m549xeb103a5f(final String str, Dialog dialog, long j, int i) {
        if (i == 0) {
            MediaScannerConnection.scanFile(this, new String[]{str}, null, new MediaScannerConnection.OnScanCompletedListener() { // from class: com.videoplayer.videox.activity.VidTrimActivity$$ExternalSyntheticLambda7
                @Override // android.media.MediaScannerConnection.OnScanCompletedListener
                public void onScanCompleted(String str2, Uri uri) {
                    VidTrimActivity.this.m551xb82859eb(str, str2, uri);
                }
            });
        } else {
            Toast.makeText(this, R.string.an_error_occurred, Toast.LENGTH_SHORT).show();
        }
        AdmobAdsHelper.ShowFullAds(this, false);
        if (dialog != null && dialog.isShowing() && !isFinishing()) {
            dialog.dismiss();
        }
        Log.d("binhnk08", "apply " + i);
    }

    /* renamed from: trimVideo6VideoTrimmerActivity, reason: merged with bridge method [inline-methods] */
    public void m551xb82859eb(final String str, String str2, Uri uri) {
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.videoplayer.videox.activity.VidTrimActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public void run() {
                VidTrimActivity.this.m550xd48640ab(str);
            }
        });
    }

    /* renamed from: trimVideo5VideoTrimmerActivity, reason: merged with bridge method [inline-methods] */
    public void m550xd48640ab(final String str) {
        new QueDiaBuil(this, new QueDiaBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.activity.VidTrimActivity.2
            @Override 
            public void onCancelClick() {
            }

            @Override 
            public void onOkClick() {
                File file = new File(str);
                if (file.exists()) {
                    VideoInfo videoInfo = new VideoInfo();
                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(str);
                    videoInfo.setDuration(Long.parseLong(mediaMetadataRetriever.extractMetadata(9)));
                    videoInfo.setDisplayName(file.getName());
                    videoInfo.setSize(file.length());
                    videoInfo.setPath(file.getPath());
                    videoInfo.setUri(Uri.fromFile(file).toString());
                    Intent intent = new Intent(VidTrimActivity.this, VidPlayActivity.class);
                    intent.addFlags(65536);
                    intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_NUMBER, 0);
                    intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_AFTER_DOWNLOAD, new ArrayList(Collections.singleton(videoInfo)));
                    intent.addFlags(C.ENCODING_PCM_32BIT);
                    AdmobAdsHelper.ShowFullAdsIntent(VidTrimActivity.this, intent);
                }
            }
        }).setTitle(R.string.notice, ContextCompat.getColor(this, R.color.main_orange)).setOkButtonText(R.string.play).setCancelButtonText(R.string.got_it).setQuestion(R.string.video_trim_done).build().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override 
    public void onBackPressed() {
        AdmobAdsHelper.ShowFullAds(this, true);
        finish();
    }
}
