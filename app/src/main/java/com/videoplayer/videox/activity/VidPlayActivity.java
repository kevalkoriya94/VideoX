package com.videoplayer.videox.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Icon;
import android.media.audiofx.LoudnessEnhancer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.accessibility.CaptioningManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.WorkRequest;

import com.appizona.yehiahd.fastsave.FastSave;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ui.CaptionStyleCompat;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.vid.NexVidPlaAdapter;
import com.videoplayer.videox.adapter.vid.VidSubAdapter;
import com.videoplayer.videox.dialog.VidPlaEndVidDiaBuil;
import com.videoplayer.videox.dialog.VidPlaTimDialBuil;
import com.videoplayer.videox.dialog.VidPlayEquDiaBuil;
import com.videoplayer.videox.dialog.VidPlayGuiDialBuil;
import com.videoplayer.videox.dialog.VidPlayInfDialBuil;
import com.videoplayer.videox.dialog.VidPlayMorDialBuil;
import com.videoplayer.videox.dialog.VidPlayNxtInDialBuil;
import com.videoplayer.videox.dialog.VidPlaySubDialBuil;
import com.videoplayer.videox.dialog.VidPlaySubtFileListDialBuil;
import com.videoplayer.videox.cv.CustStyPlayView;
import com.videoplayer.videox.cv.DbleTapPlayView;
import com.videoplayer.videox.cv.YTOver;
import com.videoplayer.videox.db.datasource.VideoDatabaseControl;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.entity.video.VideoSubtitle;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.db.utils.VideoFavoriteUtil;
import com.videoplayer.videox.pre.vid.VidPlayPre;
import com.videoplayer.videox.ser.MusServ;
import com.videoplayer.videox.uti.SharPrefUti;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;
import com.videoplayer.videox.uti.vid.BrightCon;
import com.videoplayer.videox.uti.vid.SubUti;
import com.videoplayer.videox.uti.vid.VidPlayUti;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class VidPlayActivity extends BaseActivity<VidPlayPre> implements VidPlayMorDialBuil.Callback {
    private static final String ACTION_MEDIA_CONTROL = "media_control";
    private static final String EXTRA_CONTROL_TYPE = "control_type";
    public static boolean controllerVisible = false;
    public static boolean controllerVisibleFully = false;
    private static boolean isChooseVideo = false;
    public static boolean isEnableSubtitle = false;
    private static boolean isOnlinePlay = false;
    public static LoudnessEnhancer loudnessEnhancer = null;
    public static MediaPlayer mediaPlayer = null;
    public static SimpleExoPlayer player = null;
    public static int repeatState = 2;
    public static int subtitleColor = 1;
    public static float subtitleSize = 20.0f;
    public static String subtitleUri = "";
    private ImageView buttonAspectRatio;
    private ImageView buttonAudio;
    private ImageView buttonPiP;
    private ImageView buttonRotation;
    private ImageView buttonUnlock;
    private ImageView buttonVolume;
    private StyledPlayerControlView controlView;
    private CountDownTimer countDownTimer;
    private VideoInfo currentVideo;
    private int currentWindowIndex;
    private ImageView exoNext;
    private ImageView exoPlayPause;
    private ImageView exoPrev;
    private boolean isAudioMode;
    private ImageView ivMore;
    private ImageView ivPlaylist;
    private LinearLayout layoutBottomEnd;
    private RelativeLayout layoutBottomPlay;
    private FrameLayout layoutControlBottom;
    private RelativeLayout layoutControlTop;
    private RelativeLayout layoutTimer;
    FrameLayout layout_ads;
    private ProgressBar loadingProgressBar;
    private VidPlaEndVidDiaBuil mEndVideoDialogBuilder;
    private Dialog mEqualizerDialog;
    private Dialog mGuideDialog;
    private Dialog mInfoVideoDialog;
    private VidPlayMorDialBuil mMoreVideoDialogBuilder;
    private VidPlayNxtInDialBuil mNextInPlaylistDialogBuilder;
    private Object mPictureInPictureParamsBuilder;
    private BroadcastReceiver mReceiver;
    private SettingPrefUtils mSettingPrefUtils;
    private VidPlaySubDialBuil mSubtitleDialogBuilder;
    private VidPlaTimDialBuil mTimerDialogBuilder;
    private MediaSessionCompat mediaSession;
    private MediaSessionConnector mediaSessionConnector;
    private CustStyPlayView playerView;
    private SeekBar timeBar;
    private TextView titleView;
    private TextView tvDuration;
    private TextView tvPosition;
    private TextView tvSpeed;
    private TextView tvTimer;
    private boolean videoLoading;
    private VLCVideoLayout vlcVideoLayout;
    private YTOver youTubeOverlay;
    private LibVLC mLibVLC = null;
    private boolean isPlayVlcBeforeRelease = false;
    private boolean isCallOnStop = false;
    private boolean isPlayAudioBeforeUserLeave = false;
    private List<VideoInfo> mVideos = new ArrayList();
    private long currentSeek = 1;
    private float currentVolume = 0.0f;
    private int brightness = -1;
    private int resizeMode = 0;
    private VidPlayUti.Orientation orientation = VidPlayUti.Orientation.SENSOR;
    private float scale = 1.0f;
    private final Handler handlerSeekbar = new Handler();
    private final Runnable runnableSeekbar = new Runnable() { // from class: com.videoplayer.videox.activity.VidPlayActivity.1
        @Override // java.lang.Runnable
        public void run() {
            long duration;
            long currentPosition;
            if (VidPlayActivity.this.playerView.isPlayVlc()) {
                if (VidPlayActivity.mediaPlayer != null) {
                    duration = VidPlayActivity.mediaPlayer.getLength();
                    currentPosition = VidPlayActivity.mediaPlayer.getTime();
                }
                duration = 0;
                currentPosition = 0;
            } else {
                if (VidPlayActivity.player != null) {
                    duration = VidPlayActivity.player.getDuration();
                    currentPosition = VidPlayActivity.player.getCurrentPosition();
                }
                duration = 0;
                currentPosition = 0;
            }
            VidPlayActivity.this.tvDuration.setText(Utility.convertLongToDuration(duration));
            VidPlayActivity.this.tvPosition.setText(Utility.convertLongToDuration(currentPosition));
            VidPlayActivity.this.timeBar.setMax((int) duration);
            VidPlayActivity.this.timeBar.setProgress((int) currentPosition);
            VidPlayActivity.this.handlerSeekbar.postDelayed(this, 500L);
        }
    };
    private final BroadcastReceiver stopVideoReceiver = new BroadcastReceiver() { // from class: com.videoplayer.videox.activity.VidPlayActivity.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (VidPlayActivity.this.isPlayAudioBeforeUserLeave) {
                return;
            }
            VidPlayActivity.this.finishAndRemoveTask();
        }
    };
    private final MediaPlayer.EventListener vlcListener = event -> {
        int i = event.type;
        if (i == 258) {
            if (currentVideo != null) {
                if (!isChooseVideo && !isOnlinePlay && mPresenter != null) {
                    mPresenter.updateVideoHistoryData(currentVideo);
                }
                titleView.setText(currentVideo.getDisplayName());
                if (mediaPlayer != null) {
                    if (isPlayVlcBeforeRelease) {
                        mediaPlayer.setTime(currentSeek);
                        isPlayVlcBeforeRelease = false;
                    } else {
                        mediaPlayer.setTime(getVideoTimeData());
                    }
                }
            }
            Log.d("binhnk08", "MediaPlayer.Event.Opening ");
        } else if (i == 260) {
            exoPlayPause.setImageResource(R.drawable.baseline_pause_circle_24);
            if (isPiPSupported()) {
                updatePictureInPictureActions(true);
            }
        } else if (i == 261) {
            exoPlayPause.setImageResource(R.drawable.baseline_play_circle_24);
            if (isPiPSupported()) {
                updatePictureInPictureActions(false);
            }
        } else if (i == 265) {
            if (mVideos != null && !mVideos.isEmpty()) {
                updateVideoTimeData(0L);
                if (repeatState == 0) {
                    if (isInPip()) {
                        if (currentWindowIndex < mVideos.size() - 1) {
                            playNewVideo((currentWindowIndex + 1) % mVideos.size());
                        } else {
                            finishAndRemoveTask();
                        }
                    } else if (currentWindowIndex < mVideos.size() - 1) {
                        dismissAllDialog();
                        showDialogControl(7);
                    }
                } else if (repeatState == 1) {
                    if (mediaPlayer != null) {
                        mediaPlayer.setMedia(mediaPlayer.getMedia());
                        mediaPlayer.play();
                        mediaPlayer.setTime(0L);
                    }
                } else if (repeatState == 2) {
                    if (isInPip()) {
                        playNewVideo((currentWindowIndex + 1) % mVideos.size());
                    } else {
                        dismissAllDialog();
                        showDialogControl(7);
                    }
                }
            }
            Log.d("binhnk08", "MediaPlayer.Event.EndReached");
        } else if (i == 266) {
            StringBuilder sb = new StringBuilder();
            sb.append("VLC play error, video info = ");
            sb.append(currentVideo == null ? "null" : currentVideo.toString());
        }
    };

    public static boolean onCreate$0(View view, MotionEvent motionEvent) {
        return true;
    }

    static int access$1008(VidPlayActivity VidPlayActivity) {
        int i = VidPlayActivity.currentWindowIndex;
        VidPlayActivity.currentWindowIndex = i + 1;
        return i;
    }

    static int access$1010(VidPlayActivity VidPlayActivity) {
        int i = VidPlayActivity.currentWindowIndex;
        VidPlayActivity.currentWindowIndex = i - 1;
        return i;
    }

    @Override // com.videoplayer.videox.activity.BaseActivity
    public VidPlayPre createPresenter() {
        return new VidPlayPre(null, new VideoDataRepository(this));
    }

    @Override
    // com.videoplayer.videox.activity.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d("binhnk08", "onCreate");
        Window window = getWindow();
        window.getDecorView().setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.ui_controls_background));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.ui_controls_background));
        if (Build.VERSION.SDK_INT >= 28) {
            window.getAttributes().layoutInDisplayCutoutMode = 1;
        }
        setContentView(R.layout.activity_video_player);
        this.isPlayVlcBeforeRelease = false;
        this.mSettingPrefUtils = new SettingPrefUtils(this);
        isOnlinePlay = false;
        isChooseVideo = false;
        if (getIntent() != null) {
            Intent intent = getIntent();
            if (intent.getData() != null) {
                isChooseVideo = true;
                Uri data = intent.getData();
                this.mVideos = new ArrayList();
                VideoInfo videoInfo = new VideoInfo();
                this.currentVideo = videoInfo;
                videoInfo.setUri(data.toString());
                this.currentVideo.setMimeType(intent.getType());
                String fileName = VidPlayUti.getFileName(this, data);
                this.currentVideo.setPath(VidPlayUti.getRealPathFromURI(this, data));
                this.currentVideo.setDisplayName(fileName);
                this.mVideos.add(this.currentVideo);
                this.currentWindowIndex = 0;
            } else {
                String stringExtra = intent.getStringExtra(AppCon.IntentExtra.EXTRA_VIDEO_URL);
                ArrayList arrayList = (ArrayList) intent.getSerializableExtra(AppCon.IntentExtra.EXTRA_VIDEO_HIDDEN);
                ArrayList arrayList2 = (ArrayList) intent.getSerializableExtra(AppCon.IntentExtra.EXTRA_VIDEO_AFTER_DOWNLOAD);
                if (!TextUtils.isEmpty(stringExtra)) {
                    isOnlinePlay = true;
                    this.mVideos = new ArrayList();
                    this.currentVideo = new VideoInfo();
                    Log.d("binhnk08", " url = " + stringExtra);
                    this.currentVideo.setUri(stringExtra);
                    this.mVideos.add(this.currentVideo);
                } else if (arrayList != null) {
                    isChooseVideo = true;
                    this.mVideos = new ArrayList(arrayList);
                    int intExtra = intent.getIntExtra(AppCon.IntentExtra.EXTRA_VIDEO_NUMBER, 0);
                    this.currentWindowIndex = intExtra;
                    if (intExtra < this.mVideos.size()) {
                        this.currentVideo = this.mVideos.get(this.currentWindowIndex);
                    }
                    Log.e("TAG", "onCreate:2 " + this.currentVideo);
                } else if (arrayList2 != null) {
                    isChooseVideo = true;
                    this.mVideos = new ArrayList(arrayList2);
                    int intExtra2 = intent.getIntExtra(AppCon.IntentExtra.EXTRA_VIDEO_NUMBER, 0);
                    this.currentWindowIndex = intExtra2;
                    if (intExtra2 < this.mVideos.size()) {
                        this.currentVideo = this.mVideos.get(this.currentWindowIndex);
                    }
                    Log.e("TAG", "onCreate:1 " + this.currentVideo);
                } else {
                    this.mVideos = new ArrayList();
                    ArrayList arrayList3 = (ArrayList) intent.getSerializableExtra(AppCon.IntentExtra.EXTRA_VIDEO_ARRAY);
                    if (arrayList3 != null) {
                        Iterator it = arrayList3.iterator();
                        while (it.hasNext()) {
                            VideoInfo videoById = VideoDatabaseControl.getInstance().getVideoById(((Long) it.next()).longValue());
                            if (videoById != null) {
                                this.mVideos.add(videoById);
                            }
                        }
                    }
                    int intExtra3 = intent.getIntExtra(AppCon.IntentExtra.EXTRA_VIDEO_NUMBER, 0);
                    this.currentWindowIndex = intExtra3;
                    if (intExtra3 < this.mVideos.size()) {
                        this.currentVideo = this.mVideos.get(this.currentWindowIndex);
                    }
                    Log.e("TAG", "onCreate: " + this.currentVideo);
                    this.isAudioMode = intent.getBooleanExtra(AppCon.IntentExtra.EXTRA_VIDEO_AUDIO_MODE, false);
                }
            }
        } else {
            finishAndRemoveTask();
        }
        this.currentSeek = getVideoTimeData();
        findViewById();
        initOrientation();
        initPlayer();
        initTimeBar();
        initButtonControl();
        initCutout();
        initConfig();
        findViewById(R.id.exo_bottom_button).setOnTouchListener((view, motionEvent) -> {
            boolean onCreate$0;
            onCreate$0 = VidPlayActivity.onCreate$0(view, motionEvent);
            return onCreate$0;
        });
        if (VidPlayUti.isShownGuide(this)) {
            return;
        }
        Dialog build = new VidPlayGuiDialBuil(this).build();
        this.mGuideDialog = build;
        build.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity.3
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                if (SharPrefUti.getBoolean(VidPlayActivity.this, "TAP_TARGET_MORE", false)) {
                    return;
                }
                VidPlayActivity vidPlayActivity = VidPlayActivity.this;
                TapTargetView.showFor(vidPlayActivity, TapTarget.forView(vidPlayActivity.ivMore, VidPlayActivity.this.getString(R.string.other_feature), VidPlayActivity.this.getString(R.string.other_feature_content)).outerCircleColor(R.color.color_1696F3).targetCircleColor(R.color.white).titleTextSize(20).titleTextColor(R.color.white).descriptionTextSize(16).cancelable(true), new TapTargetView.Listener() { // from class: com.videoplayer.videox.activity.VidPlayActivity.3.1
                    @Override // com.getkeepsafe.taptargetview.TapTargetView.Listener
                    public void onTargetClick(TapTargetView tapTargetView) {
                        super.onTargetClick(tapTargetView);
                        VidPlayActivity.this.ivMore.performClick();
                    }

                    @Override // com.getkeepsafe.taptargetview.TapTargetView.Listener
                    public void onTargetDismissed(TapTargetView tapTargetView, boolean z) {
                        super.onTargetDismissed(tapTargetView, z);
                    }
                });
                SharPrefUti.putBoolean(VidPlayActivity.this, "TAP_TARGET_MORE", true);
            }
        });
        this.mGuideDialog.show();
    }

    private void findViewById() {
        this.layout_ads = findViewById(R.id.layout_ads);
        this.playerView = findViewById(R.id.video_view);
        this.vlcVideoLayout = findViewById(R.id.vlc_video_view);
        this.exoPlayPause = findViewById(R.id.iv_play_pause);
        this.exoPrev = findViewById(R.id.iv_prev);
        this.exoNext = findViewById(R.id.iv_next);
        this.loadingProgressBar = findViewById(R.id.loading);
        this.titleView = findViewById(R.id.tv_title);
        this.buttonAspectRatio = findViewById(R.id.iv_fullscreen);
        this.buttonRotation = findViewById(R.id.iv_rotation);
        this.buttonVolume = findViewById(R.id.image_volume);
        this.buttonAudio = findViewById(R.id.iv_audio);
        this.tvSpeed = findViewById(R.id.tv_speed);
        this.ivPlaylist = findViewById(R.id.iv_playlist);
        this.ivMore = findViewById(R.id.iv_more);
        this.layoutBottomEnd = findViewById(R.id.layout_bottom_end);
        this.controlView = this.playerView.findViewById(com.google.android.exoplayer2.R.id.exo_controller);
        this.layoutControlTop = this.playerView.findViewById(R.id.exo_basic_controls);
        this.layoutControlBottom = this.playerView.findViewById(R.id.exo_bottom_button);
        this.layoutBottomPlay = this.playerView.findViewById(R.id.layout_controls);
        this.buttonUnlock = this.playerView.findViewById(R.id.iv_unlock);
        this.tvTimer = this.playerView.findViewById(R.id.tv_timer);
        this.layoutTimer = this.playerView.findViewById(R.id.layout_timer);
        this.tvPosition = findViewById(R.id.tv_position);
        this.tvDuration = findViewById(R.id.tv_duration);
        this.timeBar = findViewById(R.id.seekbar_progress);
        this.titleView.setSelected(true);
    }

    private void initOrientation() {
        int videoMode = this.mSettingPrefUtils.getVideoMode();
        if (videoMode == 1) {
            this.orientation = VidPlayUti.Orientation.SENSOR;
        } else if (videoMode == 2) {
            this.orientation = VidPlayUti.Orientation.PORTRAIT;
        } else if (videoMode == 3) {
            this.orientation = VidPlayUti.Orientation.LANDSCAPE;
        }
        VidPlayUti.setOrientation(this, this.orientation);
        setIconOrientation(this.orientation);
    }

    private void initButtonControl() {
        this.exoPlayPause.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda17
            @Override 
            public void onClick(View view) {
                VidPlayActivity.this.initButtonControlVVidPlayActivity(view);
            }
        });
        this.exoPrev.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda1
            @Override 
            public void onClick(View view) {
                VidPlayActivity.this.initButtonControl$2$VidPlayActivity(view);
            }
        });
        this.exoNext.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda2
            @Override 
            public void onClick(View view) {
                VidPlayActivity.this.initButtonControl$3$VidPlayActivity(view);
            }
        });
        List<VideoInfo> list = this.mVideos;
        if (list != null && list.size() == 1) {
            VidPlayUti.setButtonEnabled(this, this.exoNext, false);
            VidPlayUti.setButtonEnabled(this, this.exoPrev, false);
            this.ivPlaylist.setVisibility(View.GONE);
        }
        this.ivPlaylist.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda3
            @Override 
            public void onClick(View view) {
                VidPlayActivity.this.initButtonControl$4$VidPlayActivity(view);
            }
        });
        findViewById(R.id.iv_cc).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda4
            @Override 
            public void onClick(View view) {
                VidPlayActivity.this.initButtonControl$5$VidPlayActivity(view);
            }
        });
        this.buttonAudio.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda5
            @Override 
            public void onClick(View view) {
                VidPlayActivity.this.initButtonControl$6$VidPlayActivity(view);
            }
        });
        this.ivMore.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda6
            @Override 
            public void onClick(View view) {
                VidPlayActivity.this.initButtonControl$7$VidPlayActivity(view);
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda7
            @Override 
            public void onClick(View view) {
                VidPlayActivity.this.initButtonControl$8$VidPlayActivity(view);
            }
        });
        if (isPiPSupported()) {
            this.mPictureInPictureParamsBuilder = new PictureInPictureParams.Builder();
            updatePictureInPictureActions(false);
            ImageView imageView = findViewById(R.id.iv_pip);
            this.buttonPiP = imageView;
            imageView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda8
                @Override 
                public void onClick(View view) {
                    VidPlayActivity.this.initButtonControl$9$VidPlayActivity(view);
                }
            });
        }
        this.buttonAspectRatio.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda9
            @Override 
            public void onClick(View view) {
                VidPlayActivity.this.initButtonControl$10$VidPlayActivity(view);
            }
        });
        this.buttonAspectRatio.setVisibility(View.GONE);
        this.buttonRotation.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda18
            @Override 
            public void onClick(View view) {
                VidPlayActivity.this.initButtonControl$11$VidPlayActivity(view);
            }
        });
        this.buttonVolume.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda19
            @Override 
            public void onClick(View view) {
                VidPlayActivity.this.initButtonControl$12$VidPlayActivity(view);
            }
        });
        this.tvSpeed.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda20
            @Override 
            public void onClick(View view) {
                VidPlayActivity.this.initButtonControl$14$VidPlayActivity(view);
            }
        });
        this.buttonUnlock.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda21
            @Override 
            public void onClick(View view) {
                VidPlayActivity.this.initButtonControl$15$VidPlayActivity(view);
            }
        });
        setAudioMode(this.isAudioMode);
    }

    public void initButtonControlVVidPlayActivity(View view) {
        AdmobAdsHelper.showAdsNumberCount++;
        if (this.playerView.isPlayVlc()) {
            MediaPlayer mediaPlayer2 = mediaPlayer;
            if (mediaPlayer2 == null) {
                return;
            }
            if (mediaPlayer2.isPlaying()) {
                mediaPlayer.pause();
                return;
            } else {
                mediaPlayer.play();
                return;
            }
        }
        SimpleExoPlayer simpleExoPlayer = player;
        if (simpleExoPlayer == null) {
            return;
        }
        if (simpleExoPlayer.isPlaying()) {
            player.pause();
        } else {
            player.play();
        }
    }

    public void initButtonControl$2$VidPlayActivity(View view) {
        SimpleExoPlayer simpleExoPlayer;
        AdmobAdsHelper.showAdsNumberCount++;
        List<VideoInfo> list = this.mVideos;
        if (list == null || list.isEmpty()) {
            return;
        }
        if (this.playerView.isPlayVlc() || (simpleExoPlayer = player) == null) {
            MediaPlayer mediaPlayer2 = mediaPlayer;
            if (mediaPlayer2 != null) {
                updateVideoTimeData(mediaPlayer2.getTime());
            }
        } else {
            updateVideoTimeData(simpleExoPlayer.getCurrentPosition());
        }
        if (this.currentWindowIndex <= 0) {
            this.currentWindowIndex = this.mVideos.size();
        }
        int i = this.currentWindowIndex - 1;
        this.currentWindowIndex = i;
        playNewVideo(i);
    }

    public void initButtonControl$3$VidPlayActivity(View view) {
        SimpleExoPlayer simpleExoPlayer;
        AdmobAdsHelper.showAdsNumberCount++;
        List<VideoInfo> list = this.mVideos;
        if (list == null || list.isEmpty()) {
            return;
        }
        if (this.playerView.isPlayVlc() || (simpleExoPlayer = player) == null) {
            MediaPlayer mediaPlayer2 = mediaPlayer;
            if (mediaPlayer2 != null) {
                updateVideoTimeData(mediaPlayer2.getTime());
            }
        } else {
            updateVideoTimeData(simpleExoPlayer.getCurrentPosition());
        }
        if (this.currentWindowIndex >= this.mVideos.size() - 1) {
            this.currentWindowIndex = -1;
        }
        int i = this.currentWindowIndex + 1;
        this.currentWindowIndex = i;
        playNewVideo(i);
    }

    public void initButtonControl$4$VidPlayActivity(View view) {
        AdmobAdsHelper.showAdsNumberCount++;
        showDialogControl(1);
    }

    public void initButtonControl$5$VidPlayActivity(View view) {
        AdmobAdsHelper.showAdsNumberCount++;
        if (this.playerView.isPlayVlc()) {
            Toast.makeText(this, "Video format does not support this function yet", Toast.LENGTH_SHORT).show();
        } else {
            showDialogControl(2);
        }
    }

    public void initButtonControl$6$VidPlayActivity(View view) {
        AdmobAdsHelper.showAdsNumberCount++;
        setAudioMode(!this.isAudioMode);
    }

    public void initButtonControl$7$VidPlayActivity(View view) {
        AdmobAdsHelper.showAdsNumberCount++;
        showDialogControl(3);
    }

    public void initButtonControl$8$VidPlayActivity(View view) {
        AdmobAdsHelper.showAdsNumberCount++;
        if (!isPiPSupported() || hasPiPPermission() || this.playerView.isAudioMode()) {
            finishAndRemoveTask();
        } else {
            enterPiP();
        }
    }

    public void initButtonControl$9$VidPlayActivity(View view) {
        AdmobAdsHelper.showAdsNumberCount++;
        if (Build.VERSION.SDK_INT >= 26) {
            enterPiP();
        }
    }

    public void initButtonControl$10$VidPlayActivity(View view) {
        AdmobAdsHelper.showAdsNumberCount++;
        if (this.playerView.isPlayVlc()) {
            Toast.makeText(this, "Video format does not support this function yet", Toast.LENGTH_SHORT).show();
        }
        this.playerView.setScale(1.0f);
        if (this.playerView.getResizeMode() == 0) {
            this.playerView.setResizeMode(4);
            this.buttonAspectRatio.setImageResource(R.drawable.baseline_zoom_in_map_24);
        } else {
            this.playerView.setResizeMode(0);
            this.buttonAspectRatio.setImageResource(R.drawable.baseline_fullscreen_24);
        }
        resetHideCallbacks();
    }

    public void initButtonControl$11$VidPlayActivity(View view) {
        AdmobAdsHelper.showAdsNumberCount++;
        VidPlayUti.Orientation nextOrientation = VidPlayUti.getNextOrientation(this.orientation);
        this.orientation = nextOrientation;
        setIconOrientation(nextOrientation);
        VidPlayUti.setOrientation(this, this.orientation);
        VidPlayUti.showText(this.playerView, getString(this.orientation.description), 2500L);
        resetHideCallbacks();
    }

    public void initButtonControl$12$VidPlayActivity(View view) {
        AdmobAdsHelper.showAdsNumberCount++;
        setMuteAction(!this.buttonVolume.isActivated());
    }

    public void initButtonControl$14$VidPlayActivity(View view) {
        AdmobAdsHelper.showAdsNumberCount++;
        PopupMenu popupMenu = new PopupMenu(this, this.tvSpeed);
        popupMenu.getMenuInflater().inflate(R.menu.menu_speed, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda12
            @Override // android.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                return VidPlayActivity.this.m536x44355043(menuItem);
            }
        });
        popupMenu.show();
    }

    /* renamed from: initButtonControl$13$VidPlayActivity, reason: merged with bridge method [inline-methods] */
    public boolean m536x44355043(MenuItem menuItem) {
        float f;
        int itemId = menuItem.getItemId();
        if (itemId == R.id.speed1) {
            f = 0.25f;
        } else if (itemId == R.id.speed2) {
            f = 0.5f;
        } else if (itemId == R.id.speed3) {
            f = 0.75f;
        } else if (itemId == R.id.speed5) {
            f = 1.25f;
        } else if (itemId == R.id.speed6) {
            f = 1.5f;
        } else if (itemId == R.id.speed7) {
            f = 1.75f;
        } else {
            f = itemId == R.id.speed8 ? 2.0f : 1.0f;
        }
        PlaybackParameters playbackParameters = new PlaybackParameters(f);
        SimpleExoPlayer simpleExoPlayer = player;
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlaybackParameters(playbackParameters);
        }
        MediaPlayer mediaPlayer2 = mediaPlayer;
        if (mediaPlayer2 != null) {
            mediaPlayer2.setRate(f);
        }
        this.tvSpeed.setText(f + " X");
        return true;
    }

    public void initButtonControl$15$VidPlayActivity(View view) {
        AdmobAdsHelper.showAdsNumberCount++;
        this.layoutControlTop.setVisibility(View.VISIBLE);
        this.layoutControlBottom.setVisibility(View.VISIBLE);
        this.buttonUnlock.setVisibility(View.GONE);
        this.playerView.setLockMode(false);
    }

    private void initTimeBar() {
        this.timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity.4
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    if (VidPlayActivity.this.playerView.isPlayVlc()) {
                        if (VidPlayActivity.mediaPlayer != null) {
                            VidPlayActivity.mediaPlayer.setTime(i);
                        }
                    } else if (VidPlayActivity.player != null) {
                        VidPlayActivity.player.seekTo(i);
                    }
                }
            }
        });
    }

    private void initConfig() {
        if (this.brightness >= 0) {
            BrightCon.getInstance().setCurrentBrightnessLevel(this.brightness);
            BrightCon.getInstance().setScreenBrightness(this, BrightCon.getInstance().levelToBrightness(BrightCon.getInstance().getCurrentBrightnessLevel()));
        }
        YTOver yTOver = findViewById(R.id.youtube_overlay);
        this.youTubeOverlay = yTOver;
        yTOver.performListener(new YTOver.PerformListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity.5
            @Override // com.videoplayer.videox.cv.YTOver.PerformListener
            public void onAnimationStart() {
                VidPlayActivity.this.youTubeOverlay.setAlpha(1.0f);
                VidPlayActivity.this.youTubeOverlay.setVisibility(View.VISIBLE);
            }

            @Override // com.videoplayer.videox.cv.YTOver.PerformListener
            public void onAnimationEnd() {
                VidPlayActivity.this.youTubeOverlay.animate().alpha(0.0f).setDuration(300L).setListener(new AnimatorListenerAdapter() { // from class: com.videoplayer.videox.activity.VidPlayActivity.5.1
                    @Override
                    // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        VidPlayActivity.this.youTubeOverlay.setVisibility(View.GONE);
                        VidPlayActivity.this.youTubeOverlay.setAlpha(1.0f);
                    }
                });
            }
        });
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this, getString(R.string.app_name));
        this.mediaSession = mediaSessionCompat;
        this.mediaSessionConnector = new MediaSessionConnector(mediaSessionCompat);
    }

    private void initPlayer() {
        this.playerView.setShowNextButton(false);
        this.playerView.setShowPreviousButton(false);
        this.playerView.setShowFastForwardButton(false);
        this.playerView.setShowRewindButton(false);
        this.playerView.setControllerHideOnTouch(false);
        this.playerView.setControllerAutoShow(true);
        ((DbleTapPlayView) this.playerView).setDoubleTapEnabled(false);
        this.playerView.setAudioMode(this.isAudioMode);
        this.playerView.setControllerVisibilityListener(new StyledPlayerControlView.VisibilityListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda13
            @Override // com.google.android.exoplayer2.ui.StyledPlayerControlView.VisibilityListener
            public void onVisibilityChange(int i) {
                VidPlayActivity.this.initPlayer$16$VidPlayActivity(i);
            }
        });
    }

    public void initPlayer$16$VidPlayActivity(int i) {
        controllerVisible = i == 0;
        controllerVisibleFully = this.playerView.isControllerFullyVisible();
        SubtitleView subtitleView = this.playerView.getSubtitleView();
        if (i == 0) {
            VidPlayUti.showSystemUi(this.playerView);
            if (subtitleView != null) {
                subtitleView.setBottomPaddingFraction(0.28f);
                return;
            }
            return;
        }
        VidPlayUti.hideSystemUi(this.playerView);
        if (subtitleView != null) {
            subtitleView.setBottomPaddingFraction(0.12f);
        }
    }

    private void initCutout() {
        final int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.exo_styled_bottom_bar_time_padding);
        this.controlView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda11
            @Override // android.view.View.OnApplyWindowInsetsListener
            public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                return VidPlayActivity.this.m537xcb196f4d(dimensionPixelOffset, view, windowInsets);
            }
        });
    }

    /* renamed from: initCutout$17$VidPlayActivity, reason: merged with bridge method [inline-methods] */
    public WindowInsets m537xcb196f4d(int i, View view, WindowInsets windowInsets) {
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        if (windowInsets != null) {
            view.setPadding(0, windowInsets.getSystemWindowInsetTop(), 0, windowInsets.getSystemWindowInsetBottom());
            int systemWindowInsetLeft = windowInsets.getSystemWindowInsetLeft();
            int systemWindowInsetRight = windowInsets.getSystemWindowInsetRight();
            if (Build.VERSION.SDK_INT < 28 || windowInsets.getDisplayCutout() == null) {
                i2 = systemWindowInsetLeft;
                i3 = 0;
                i4 = systemWindowInsetRight;
            } else {
                if (windowInsets.getDisplayCutout().getSafeInsetLeft() == systemWindowInsetLeft) {
                    i6 = systemWindowInsetLeft;
                    i5 = 0;
                } else {
                    i5 = systemWindowInsetLeft;
                    i6 = 0;
                }
                if (windowInsets.getDisplayCutout().getSafeInsetRight() == systemWindowInsetRight) {
                    VidPlayUti.setViewParams(findViewById(R.id.exo_basic_controls), i6 + i, i, systemWindowInsetRight + i, i, i5, 0, 0, 0);
                    VidPlayUti.setViewParams(findViewById(R.id.exo_bottom_button), i6, VidPlayUti.dpToPx(15), systemWindowInsetRight, 0, i5, 0, 0, 0);
                    windowInsets.consumeSystemWindowInsets();
                    i2 = i5;
                    i3 = i6;
                    i4 = 0;
                } else {
                    i4 = systemWindowInsetRight;
                    i2 = i5;
                    i3 = i6;
                }
            }
            VidPlayUti.setViewParams(findViewById(R.id.exo_basic_controls), i3 + i, i, i, i, i2, 0, i4, 0);
            VidPlayUti.setViewParams(findViewById(R.id.exo_bottom_button), i3, VidPlayUti.dpToPx(15), 0, 0, i2, 0, i4, 0);
            windowInsets.consumeSystemWindowInsets();
        }
        return windowInsets;
    }

    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        Log.d("binhnk08", "onStart ");
        setSubtitleColor(subtitleColor);
        setSubtitleSize(subtitleSize, getResources().getConfiguration().orientation);
        initializePlayer();
    }

    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        this.isCallOnStop = true;
        CountDownTimer countDownTimer = this.countDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            this.layoutTimer.setVisibility(View.INVISIBLE);
        }
        releasePlayer();
        if (this.playerView.isAudioMode()) {
            startMusicServiceForPlayBackground();
        }
    }

    @Override
    public void onBackPressed() {
        AdmobAdsHelper.showAdsNumberCount++;
        if (!isPiPSupported() || hasPiPPermission() || this.playerView.isAudioMode()) {
            finishAndRemoveTask();
        } else {
            enterPiP();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("binhnk08", "onNewIntent ");
        if (intent != null) {
            isOnlinePlay = false;
            isChooseVideo = false;
            if (intent.getData() != null) {
                isChooseVideo = true;
                Uri data = intent.getData();
                this.mVideos = new ArrayList();
                VideoInfo videoInfo = new VideoInfo();
                this.currentVideo = videoInfo;
                videoInfo.setUri(data.toString());
                this.currentVideo.setMimeType(intent.getType());
                String fileName = VidPlayUti.getFileName(this, data);
                this.currentVideo.setPath(VidPlayUti.getRealPathFromURI(this, data));
                this.currentVideo.setDisplayName(fileName);
                this.mVideos.add(this.currentVideo);
                this.currentWindowIndex = 0;
            } else {
                String stringExtra = intent.getStringExtra(AppCon.IntentExtra.EXTRA_VIDEO_URL);
                ArrayList arrayList = (ArrayList) intent.getSerializableExtra(AppCon.IntentExtra.EXTRA_VIDEO_HIDDEN);
                ArrayList arrayList2 = (ArrayList) intent.getSerializableExtra(AppCon.IntentExtra.EXTRA_VIDEO_AFTER_DOWNLOAD);
                if (!TextUtils.isEmpty(stringExtra)) {
                    isOnlinePlay = true;
                    this.currentVideo = new VideoInfo();
                    Log.d("binhnk08", " url = " + stringExtra);
                    this.currentVideo.setUri(stringExtra);
                    this.mVideos.add(this.currentVideo);
                } else if (arrayList != null) {
                    isChooseVideo = true;
                    this.mVideos = new ArrayList(arrayList);
                    int intExtra = intent.getIntExtra(AppCon.IntentExtra.EXTRA_VIDEO_NUMBER, 0);
                    this.currentWindowIndex = intExtra;
                    if (intExtra < this.mVideos.size()) {
                        this.currentVideo = this.mVideos.get(this.currentWindowIndex);
                    }
                } else if (arrayList2 != null) {
                    isChooseVideo = true;
                    this.mVideos = new ArrayList(arrayList2);
                    int intExtra2 = intent.getIntExtra(AppCon.IntentExtra.EXTRA_VIDEO_NUMBER, 0);
                    this.currentWindowIndex = intExtra2;
                    if (intExtra2 < this.mVideos.size()) {
                        this.currentVideo = this.mVideos.get(this.currentWindowIndex);
                    }
                } else {
                    this.mVideos = new ArrayList();
                    ArrayList arrayList3 = (ArrayList) intent.getSerializableExtra(AppCon.IntentExtra.EXTRA_VIDEO_ARRAY);
                    if (arrayList3 != null) {
                        Iterator it = arrayList3.iterator();
                        while (it.hasNext()) {
                            VideoInfo videoById = VideoDatabaseControl.getInstance().getVideoById(((Long) it.next()).longValue());
                            if (videoById != null) {
                                this.mVideos.add(videoById);
                            }
                        }
                    }
                    this.isAudioMode = intent.getBooleanExtra(AppCon.IntentExtra.EXTRA_VIDEO_AUDIO_MODE, false);
                    int intExtra3 = intent.getIntExtra(AppCon.IntentExtra.EXTRA_VIDEO_NUMBER, 0);
                    this.currentWindowIndex = intExtra3;
                    if (intExtra3 < this.mVideos.size()) {
                        this.currentVideo = this.mVideos.get(this.currentWindowIndex);
                    }
                    Log.e("TAG", "onCreate:3 " + this.currentVideo);
                }
            }
            this.currentSeek = getVideoTimeData();
            VideoInfo videoInfo2 = this.currentVideo;
            if (videoInfo2 != null) {
                setMediaItem(videoInfo2);
            }
            SimpleExoPlayer simpleExoPlayer = player;
            if (simpleExoPlayer != null) {
                simpleExoPlayer.seekTo(this.currentSeek);
            }
            List<VideoInfo> list = this.mVideos;
            if (list == null || list.size() != 1) {
                VidPlayUti.setButtonEnabled(this, this.exoNext, true);
                VidPlayUti.setButtonEnabled(this, this.exoPrev, true);
                this.ivPlaylist.setVisibility(View.VISIBLE);
            } else {
                VidPlayUti.setButtonEnabled(this, this.exoNext, false);
                VidPlayUti.setButtonEnabled(this, this.exoPrev, false);
                this.ivPlaylist.setVisibility(View.GONE);
            }
            setAudioMode(this.isAudioMode);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override
    // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        SimpleExoPlayer simpleExoPlayer;
        SimpleExoPlayer simpleExoPlayer2;
        long j;
        long currentPosition;
        if (i != 4) {
            if (i != 62 && i != 66 && i != 85 && i != 96 && i != 160) {
                if (i != 104) {
                    if (i != 105 && i != 108 && i != 109 && i != 126 && i != 127) {
                        switch (i) {
                            case 21:
                            case 22:
                            case 23:
                                break;
                            case 24:
                            case 25:
                                CustStyPlayView custStyPlayView = this.playerView;
                                custStyPlayView.removeCallbacks(custStyPlayView.textClearRunnable);
                                VidPlayUti.adjustVolume(this, this.playerView, i == 24, keyEvent.getRepeatCount() == 0);
                                if (this.buttonVolume.isActivated()) {
                                    setMuteAction(false);
                                    break;
                                }
                                break;
                            default:
                                if (!controllerVisibleFully && !isInPip()) {
                                    this.playerView.showController();
                                    break;
                                }
                                break;
                        }
                        return true;
                    }
                    if (!controllerVisibleFully && player != null && mediaPlayer != null) {
                        CustStyPlayView custStyPlayView2 = this.playerView;
                        custStyPlayView2.removeCallbacks(custStyPlayView2.textClearRunnable);
                        Log.d("binhnk08", "KEYCODE_BUTTON_R2 " + this.playerView.isPlayVlc());
                        if (this.playerView.isPlayVlc()) {
                            currentPosition = mediaPlayer.getTime() + WorkRequest.MIN_BACKOFF_MILLIS;
                            long length = mediaPlayer.getLength();
                            if (currentPosition > length) {
                                currentPosition = length;
                            }
                            Log.d("binhnk08", "KEYCODE_BUTTON_R2 seekTo = " + currentPosition);
                            mediaPlayer.setTime(currentPosition);
                        } else {
                            currentPosition = player.getCurrentPosition() + WorkRequest.MIN_BACKOFF_MILLIS;
                            long duration = player.getDuration();
                            if (duration != C.TIME_UNSET && currentPosition > duration) {
                                currentPosition = duration;
                            }
                            player.setSeekParameters(SeekParameters.NEXT_SYNC);
                            player.seekTo(currentPosition);
                        }
                        this.playerView.setCustomErrorMessage(VidPlayUti.formatMilis(currentPosition));
                        return true;
                    }
                }
                if (!controllerVisibleFully && player != null && mediaPlayer != null) {
                    CustStyPlayView custStyPlayView3 = this.playerView;
                    custStyPlayView3.removeCallbacks(custStyPlayView3.textClearRunnable);
                    Log.d("binhnk08", "KEYCODE_BUTTON_R2 " + this.playerView.isPlayVlc());
                    if (this.playerView.isPlayVlc()) {
                        long time = mediaPlayer.getTime() - WorkRequest.MIN_BACKOFF_MILLIS;
                        j = time > 0 ? time : 1L;
                        Log.d("binhnk08", "KEYCODE_BUTTON_R2 seekTo = " + j);
                        mediaPlayer.setTime(j);
                    } else {
                        long currentPosition2 = player.getCurrentPosition() - WorkRequest.MIN_BACKOFF_MILLIS;
                        j = currentPosition2 > 0 ? currentPosition2 : 1L;
                        player.setSeekParameters(SeekParameters.PREVIOUS_SYNC);
                        player.seekTo(j);
                    }
                    this.playerView.setCustomErrorMessage(VidPlayUti.formatMilis(j));
                    return true;
                }
            }
            if (!controllerVisibleFully && (simpleExoPlayer2 = player) != null) {
                if (simpleExoPlayer2.isPlaying()) {
                    player.pause();
                } else {
                    player.play();
                }
                return true;
            }
        } else if (VidPlayUti.isTvBox(this) && controllerVisible && (simpleExoPlayer = player) != null && simpleExoPlayer.isPlaying()) {
            this.playerView.hideController();
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int r5, KeyEvent r6) {
        CustStyPlayView custStyPlayView = this.playerView;
        custStyPlayView.postDelayed(custStyPlayView.textClearRunnable, 800L);
        if (this.buttonVolume.isActivated()) {
            setMuteAction(false);
        }
        return super.onKeyUp(r5, r6);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean z, Configuration configuration) {
        super.onPictureInPictureModeChanged(z, configuration);
        if (z) {
            setSubtitleSizePiP();
            this.playerView.setScale(1.0f);
            this.mReceiver = new BroadcastReceiver() { // from class: com.videoplayer.videox.activity.VidPlayActivity.6
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if (intent == null || !VidPlayActivity.ACTION_MEDIA_CONTROL.equals(intent.getAction()) || VidPlayActivity.player == null) {
                        return;
                    }
                    Log.d("binhnk08", " EXTRA_CONTROL_TYPE = " + intent.getIntExtra(VidPlayActivity.EXTRA_CONTROL_TYPE, 0));
                    int intExtra = intent.getIntExtra(VidPlayActivity.EXTRA_CONTROL_TYPE, 0);
                    if (intExtra == 1) {
                        if (VidPlayActivity.this.playerView.isPlayVlc()) {
                            if (VidPlayActivity.mediaPlayer == null) {
                                return;
                            }
                            if (VidPlayActivity.mediaPlayer.isPlaying()) {
                                VidPlayActivity.mediaPlayer.pause();
                                return;
                            } else {
                                VidPlayActivity.mediaPlayer.play();
                                return;
                            }
                        }
                        if (VidPlayActivity.player == null) {
                            return;
                        }
                        if (VidPlayActivity.player.isPlaying()) {
                            VidPlayActivity.player.pause();
                            return;
                        } else {
                            VidPlayActivity.player.play();
                            return;
                        }
                    }
                    if (intExtra != 2) {
                        if (intExtra != 3 || VidPlayActivity.this.mVideos == null || VidPlayActivity.this.mVideos.size() <= 1) {
                            return;
                        }
                        if (!VidPlayActivity.this.playerView.isPlayVlc() && VidPlayActivity.player != null) {
                            VidPlayActivity.this.updateVideoTimeData(VidPlayActivity.player.getCurrentPosition());
                        } else if (VidPlayActivity.mediaPlayer != null) {
                            VidPlayActivity.this.updateVideoTimeData(VidPlayActivity.mediaPlayer.getTime());
                        }
                        if (VidPlayActivity.this.currentWindowIndex <= 0) {
                            VidPlayActivity vidPlayActivity = VidPlayActivity.this;
                            vidPlayActivity.currentWindowIndex = vidPlayActivity.mVideos.size();
                        }
                        VidPlayActivity.access$1010(VidPlayActivity.this);
                        VidPlayActivity vidPlayActivity2 = VidPlayActivity.this;
                        vidPlayActivity2.playNewVideo(vidPlayActivity2.currentWindowIndex);
                        return;
                    }
                    if (VidPlayActivity.this.mVideos == null || VidPlayActivity.this.mVideos.size() <= 1) {
                        return;
                    }
                    if (!VidPlayActivity.this.playerView.isPlayVlc() && VidPlayActivity.player != null) {
                        VidPlayActivity.this.updateVideoTimeData(VidPlayActivity.player.getCurrentPosition());
                    } else if (VidPlayActivity.mediaPlayer != null) {
                        VidPlayActivity.this.updateVideoTimeData(VidPlayActivity.mediaPlayer.getTime());
                    }
                    if (VidPlayActivity.this.currentWindowIndex >= VidPlayActivity.this.mVideos.size() - 1) {
                        VidPlayActivity.this.currentWindowIndex = -1;
                    }
                    VidPlayActivity.access$1008(VidPlayActivity.this);
                    VidPlayActivity vidPlayActivity3 = VidPlayActivity.this;
                    vidPlayActivity3.playNewVideo(vidPlayActivity3.currentWindowIndex);
                }
            };
            if (Build.VERSION.SDK_INT >= 33) {
                registerReceiver(this.mReceiver, new IntentFilter(ACTION_MEDIA_CONTROL), 2);
                return;
            } else {
                registerReceiver(this.mReceiver, new IntentFilter(ACTION_MEDIA_CONTROL));
                return;
            }
        }
        BroadcastReceiver broadcastReceiver = this.mReceiver;
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            this.mReceiver = null;
        }
        if (this.isCallOnStop) {
            finishAndRemoveTask();
            return;
        }
        setSubtitleSize(subtitleSize, getResources().getConfiguration().orientation);
        if (this.resizeMode == 4) {
            this.playerView.setScale(this.scale);
        }
        this.playerView.setControllerAutoShow(true);
        SimpleExoPlayer simpleExoPlayer = player;
        if (simpleExoPlayer == null) {
            return;
        }
        if (simpleExoPlayer.isPlaying()) {
            VidPlayUti.hideSystemUi(this.playerView);
        } else {
            this.playerView.showController();
        }
    }

    public void playNewVideo(int i) {
        List<VideoInfo> list = this.mVideos;
        if (list == null || i < 0 || i >= list.size()) {
            return;
        }
        VideoInfo videoInfo = this.mVideos.get(i);
        this.currentVideo = videoInfo;
        this.currentWindowIndex = i;
        if (videoInfo == null || player == null) {
            return;
        }
        setMediaItem(videoInfo);
        player.seekTo(getVideoTimeData());
        if (player.isPlaying()) {
            return;
        }
        player.play();
    }

    public void setMediaItem(VideoInfo videoInfo) {
        MediaItem.Builder builder;
        if (player != null) {
            if (isOnlinePlay) {
                builder = new MediaItem.Builder().setUri(videoInfo.getUri()).setLiveMaxPlaybackSpeed(1.02f);
            } else {
                MediaItem.Builder mimeType = new MediaItem.Builder().setUri(videoInfo.getUri()).setMimeType(videoInfo.getMimeType());
                if (!isChooseVideo) {
                    subtitleUri = SubUti.getSubtitleFileUri(this, videoInfo.getVideoId());
                }
                if (isEnableSubtitle && !TextUtils.isEmpty(subtitleUri)) {
                    Uri parse = Uri.parse(subtitleUri);
                    mimeType.setSubtitles(Collections.singletonList(new MediaItem.Subtitle(parse, SubUti.getSubtitleMime(parse), SubUti.getSubtitleLanguage(parse), 1, 128, VidPlayUti.getFileName(this, parse))));
                }
                builder = mimeType;
            }
            player.setMediaItem(builder.build());
            player.prepare();
        }
    }

    public void initializePlayer() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("--no-drop-late-frames");
        arrayList.add("--no-skip-frames");
        arrayList.add("--rtsp-tcp");
        arrayList.add("-vvv");
        LibVLC libVLC = new LibVLC(this, arrayList);
        this.mLibVLC = libVLC;
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer(libVLC);
        }
        mediaPlayer.setEventListener(this.vlcListener);
        try {
            mediaPlayer.attachViews(this.vlcVideoLayout, null, true, false);
        } catch (Exception unused) {
        }
        if (player == null) {
            player = new SimpleExoPlayer.Builder(this).build();
        }
        this.youTubeOverlay.player(player, mediaPlayer);
        this.playerView.setPlayer(player);
        this.mediaSessionConnector.setPlayer(player);
        this.mediaSessionConnector.setMediaMetadataProvider(new MediaSessionConnector.MediaMetadataProvider() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda0
            @Override
            // com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector.MediaMetadataProvider
            public MediaMetadataCompat getMetadata(Player player2) {
                return VidPlayActivity.this.m538x6b674be3(player2);
            }
        });
        this.playerView.setControllerShowTimeoutMs(-1);
        this.playerView.setResizeMode(this.resizeMode);
        if (this.resizeMode == 4) {
            this.playerView.setScale(this.scale);
        } else {
            this.playerView.setScale(1.0f);
        }
        LoudnessEnhancer loudnessEnhancer2 = loudnessEnhancer;
        if (loudnessEnhancer2 != null) {
            loudnessEnhancer2.release();
        }
        try {
            int generateAudioSessionIdV21 = C.generateAudioSessionIdV21(this);
            loudnessEnhancer = new LoudnessEnhancer(generateAudioSessionIdV21);
            player.setAudioSessionId(generateAudioSessionIdV21);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        player.addAnalyticsListener(new AnalyticsListener() { // from class: com.videoplayer.videox.activity.VidPlayActivity.7
            @Override // com.google.android.exoplayer2.analytics.AnalyticsListener
            public void onAudioAttributesChanged(AnalyticsListener.EventTime eventTime, AudioAttributes audioAttributes) {
                onAudioAttributesChanged(eventTime, audioAttributes);
            }

            @Override // com.google.android.exoplayer2.analytics.AnalyticsListener
            public void onSkipSilenceEnabledChanged(AnalyticsListener.EventTime eventTime, boolean z) {
                onSkipSilenceEnabledChanged(eventTime, z);
            }

            @Override // com.google.android.exoplayer2.analytics.AnalyticsListener
            public void onVolumeChanged(AnalyticsListener.EventTime eventTime, float f) {
                onVolumeChanged(eventTime, f);
            }

            @Override // com.google.android.exoplayer2.analytics.AnalyticsListener
            public void onAudioSessionIdChanged(AnalyticsListener.EventTime eventTime, int i) {
                if (VidPlayActivity.loudnessEnhancer != null) {
                    VidPlayActivity.loudnessEnhancer.release();
                }
                try {
                    VidPlayActivity.loudnessEnhancer = new LoudnessEnhancer(i);
                } catch (RuntimeException e2) {
                    e2.printStackTrace();
                }
            }
        });
        this.videoLoading = true;
        updateLoading(true);
        this.titleView.setVisibility(View.VISIBLE);
        if (this.buttonPiP != null && isPiPSupported()) {
            this.buttonPiP.setVisibility(View.VISIBLE);
        }
        this.buttonAspectRatio.setVisibility(View.VISIBLE);
        this.buttonVolume.setActivated(false);
        this.tvSpeed.setText("1.0 X");
        ((DbleTapPlayView) this.playerView).setDoubleTapEnabled(true);
        player.setHandleAudioBecomingNoisy(true);
        this.mediaSession.setActive(true);
        
        player.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean z) {
                Log.d("binhnk08 ", "onIsPlayingChanged isPlaying = " + z);
                VidPlayActivity.this.playerView.setKeepScreenOn(z);
                if (VidPlayActivity.this.isPiPSupported()) {
                    VidPlayActivity.this.updatePictureInPictureActions(z);
                }
                VidPlayActivity.this.exoPlayPause.setImageResource(z ? R.drawable.baseline_pause_circle_24 : R.drawable.baseline_play_circle_24);
                if (z) {
                    VidPlayActivity.this.playerView.setControllerShowTimeoutMs(2500);
                } else {
                    VidPlayActivity.this.playerView.setControllerShowTimeoutMs(-1);
                }
            }

            @Override
            public void onPlaybackStateChanged(int i) {
                Log.d("binhnk08 ", "onPlaybackStateChanged state = " + i);
                if (VidPlayActivity.player != null) {
                    if (i == 3) {
                        VidPlayActivity.this.playerView.setPlayVlc(false);
                        if (VidPlayActivity.mediaPlayer != null) {
                            VidPlayActivity.mediaPlayer.stop();
                        }
                        if (!(VidPlayActivity.this.mVideos == null || VidPlayActivity.this.mVideos.isEmpty() || VidPlayActivity.this.currentVideo == null)) {
                            if (!VidPlayActivity.isChooseVideo && !VidPlayActivity.isOnlinePlay && VidPlayActivity.this.mPresenter != null) {
                                VidPlayActivity.this.mPresenter.updateVideoHistoryData(VidPlayActivity.this.currentVideo);
                            }
                            VidPlayActivity.this.titleView.setText(VidPlayActivity.this.currentVideo.getDisplayName());
                        }
                        if (VidPlayActivity.this.videoLoading) {
                            VidPlayActivity.this.videoLoading = false;
                            VidPlayActivity.this.updateLoading(false);
                        }
                    } else if (i != 4) {
                    } else {
                        if (VidPlayActivity.isOnlinePlay || VidPlayActivity.isChooseVideo) {
                            VidPlayActivity.player.seekTo(1L);
                        } else if (VidPlayActivity.this.mVideos != null && !VidPlayActivity.this.mVideos.isEmpty()) {
                            VidPlayActivity.this.updateVideoTimeData(0L);
                            if (VidPlayActivity.repeatState == 0) {
                                if (VidPlayActivity.this.isInPip()) {
                                    if (VidPlayActivity.this.currentWindowIndex < VidPlayActivity.this.mVideos.size() - 1) {
                                        VidPlayActivity VidPlayActivity = VidPlayActivity.this;
                                        VidPlayActivity.playNewVideo((VidPlayActivity.currentWindowIndex + 1) % VidPlayActivity.this.mVideos.size());
                                        return;
                                    }
                                    VidPlayActivity.this.finishAndRemoveTask();
                                } else if (VidPlayActivity.this.currentWindowIndex < VidPlayActivity.this.mVideos.size() - 1) {
                                    VidPlayActivity.this.dismissAllDialog();
                                    VidPlayActivity.this.showDialogControl(7);
                                }
                            } else if (VidPlayActivity.repeatState == 1) {
                                if (VidPlayActivity.player != null) {
                                    VidPlayActivity.player.seekTo(1L);
                                }
                            } else if (VidPlayActivity.repeatState != 2) {
                            } else {
                                if (VidPlayActivity.this.isInPip()) {
                                    VidPlayActivity VidPlayActivity2 = VidPlayActivity.this;
                                    VidPlayActivity2.playNewVideo((VidPlayActivity2.currentWindowIndex + 1) % VidPlayActivity.this.mVideos.size());
                                    return;
                                }
                                VidPlayActivity.this.dismissAllDialog();
                                VidPlayActivity.this.showDialogControl(7);
                            }
                        }
                    }
                }
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                VidPlayActivity.this.updateLoading(false);
                Log.d("binhnk08", "error = " + error.getMessage());
                if (!VidPlayActivity.isOnlinePlay) {
                    try {
                        String path = VidPlayActivity.this.currentVideo.getPath();
                        Log.d("binhnk08", "video: path = " + path + ", uri = " + VidPlayActivity.this.currentVideo.getUri());
                        if (TextUtils.isEmpty(path)) {
                            VidPlayActivity.this.dismissAllDialog();
                            if (!VidPlayActivity.this.isInPip()) {
                                new AlertDialog.Builder(VidPlayActivity.this).setTitle(R.string.notice).setMessage(R.string.notice_video_play_error).setPositiveButton(R.string.got_it, (dialogInterface, i) -> onPlayerError$0$VidPlayActivity$PlaybackStateListener(dialogInterface, i)).show();
                            } else {
                                VidPlayActivity.this.finishAndRemoveTask();
                                Toast.makeText(VidPlayActivity.this, R.string.notice_video_play_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Media media = new Media(VidPlayActivity.this.mLibVLC, path);
                            media.setHWDecoderEnabled(true, false);
                            VidPlayActivity.mediaPlayer.setMedia(media);
                            media.release();
                            VidPlayActivity.mediaPlayer.play();
                            VidPlayActivity.this.playerView.setPlayVlc(true);
                        }
                    } catch (Exception e) {
                        Log.d("binhnk08", "Exception e = " + e);
                    }
                } else {
                    VidPlayActivity.this.dismissAllDialog();
                    VidPlayActivity VidPlayActivity = VidPlayActivity.this;
                    if (!VidPlayActivity.this.isInPip()) {
                        new AlertDialog.Builder(VidPlayActivity.this).setTitle(R.string.notice).setMessage(R.string.notice_video_play_error).setPositiveButton(R.string.got_it, (dialogInterface, i) -> onPlayerError$1$VidPlayActivity$PlaybackStateListener(dialogInterface, i)).show();
                        return;
                    }
                    VidPlayActivity.this.finishAndRemoveTask();
                    Toast.makeText(VidPlayActivity.this, R.string.notice_video_play_error, Toast.LENGTH_SHORT).show();
                }
            }

            public void onPlayerError$0$VidPlayActivity$PlaybackStateListener(DialogInterface dialogInterface, int i) {
                VidPlayActivity.this.finishAndRemoveTask();
            }

            public void onPlayerError$1$VidPlayActivity$PlaybackStateListener(DialogInterface dialogInterface, int i) {
                VidPlayActivity.this.finishAndRemoveTask();
            }
        });
        
        if (!isInPip()) {
            this.playerView.showController();
        }
        VideoInfo videoInfo = this.currentVideo;
        if (videoInfo != null) {
            setMediaItem(videoInfo);
        }
        player.seekTo(this.currentSeek);
        player.play();
        Log.d("binhnk08", "playerView.isPlayVlc() = " + this.playerView.isPlayVlc());
        this.handlerSeekbar.post(this.runnableSeekbar);
        LocalBroadcastManager.getInstance(this).registerReceiver(this.stopVideoReceiver, new IntentFilter("RECEIVER_STOP_VIDEO"));
    }


    public MediaMetadataCompat m538x6b674be3(Player player2) {
        VideoInfo videoInfo = this.currentVideo;
        String displayName = videoInfo == null ? null : videoInfo.getDisplayName();
        if (displayName == null) {
            displayName = "title_null:" + this.currentWindowIndex;
        }
        return new MediaMetadataCompat.Builder().putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, displayName).putString(MediaMetadataCompat.METADATA_KEY_TITLE, displayName).build();
    }

    public void releasePlayer() {
        if (player != null) {
            this.mediaSession.setActive(false);
            this.mediaSession.release();
            if (BrightCon.getInstance().getCurrentBrightnessLevel() >= -1) {
                this.brightness = BrightCon.getInstance().getCurrentBrightnessLevel();
            }
            if (!this.playerView.isPlayVlc() && player.isCurrentWindowSeekable()) {
                long currentPosition = player.getCurrentPosition();
                this.currentSeek = currentPosition;
                updateVideoTimeData(currentPosition);
            }
            this.resizeMode = this.playerView.getResizeMode();
            this.scale = this.playerView.getVideoSurfaceView().getScaleX();
            player.removeListener(new Player.Listener() {
                @Override
                public void onIsPlayingChanged(boolean z) {
                    Log.d("binhnk08 ", "onIsPlayingChanged isPlaying = " + z);
                    VidPlayActivity.this.playerView.setKeepScreenOn(z);
                    if (VidPlayActivity.this.isPiPSupported()) {
                        VidPlayActivity.this.updatePictureInPictureActions(z);
                    }
                    VidPlayActivity.this.exoPlayPause.setImageResource(z ? R.drawable.baseline_pause_circle_24 : R.drawable.baseline_play_circle_24);
                    if (z) {
                        VidPlayActivity.this.playerView.setControllerShowTimeoutMs(2500);
                    } else {
                        VidPlayActivity.this.playerView.setControllerShowTimeoutMs(-1);
                    }
                }

                @Override
                public void onPlaybackStateChanged(int i) {
                    Log.d("binhnk08 ", "onPlaybackStateChanged state = " + i);
                    if (VidPlayActivity.player != null) {
                        if (i == 3) {
                            VidPlayActivity.this.playerView.setPlayVlc(false);
                            if (VidPlayActivity.mediaPlayer != null) {
                                VidPlayActivity.mediaPlayer.stop();
                            }
                            if (!(VidPlayActivity.this.mVideos == null || VidPlayActivity.this.mVideos.isEmpty() || VidPlayActivity.this.currentVideo == null)) {
                                if (!VidPlayActivity.isChooseVideo && !VidPlayActivity.isOnlinePlay && VidPlayActivity.this.mPresenter != null) {
                                    VidPlayActivity.this.mPresenter.updateVideoHistoryData(VidPlayActivity.this.currentVideo);
                                }
                                VidPlayActivity.this.titleView.setText(VidPlayActivity.this.currentVideo.getDisplayName());
                            }
                            if (VidPlayActivity.this.videoLoading) {
                                VidPlayActivity.this.videoLoading = false;
                                VidPlayActivity.this.updateLoading(false);
                            }
                        } else if (i != 4) {
                        } else {
                            if (VidPlayActivity.isOnlinePlay || VidPlayActivity.isChooseVideo) {
                                VidPlayActivity.player.seekTo(1L);
                            } else if (VidPlayActivity.this.mVideos != null && !VidPlayActivity.this.mVideos.isEmpty()) {
                                VidPlayActivity.this.updateVideoTimeData(0L);
                                if (VidPlayActivity.repeatState == 0) {
                                    if (VidPlayActivity.this.isInPip()) {
                                        if (VidPlayActivity.this.currentWindowIndex < VidPlayActivity.this.mVideos.size() - 1) {
                                            VidPlayActivity VidPlayActivity = VidPlayActivity.this;
                                            VidPlayActivity.playNewVideo((VidPlayActivity.currentWindowIndex + 1) % VidPlayActivity.this.mVideos.size());
                                            return;
                                        }
                                        VidPlayActivity.this.finishAndRemoveTask();
                                    } else if (VidPlayActivity.this.currentWindowIndex < VidPlayActivity.this.mVideos.size() - 1) {
                                        VidPlayActivity.this.dismissAllDialog();
                                        VidPlayActivity.this.showDialogControl(7);
                                    }
                                } else if (VidPlayActivity.repeatState == 1) {
                                    if (VidPlayActivity.player != null) {
                                        VidPlayActivity.player.seekTo(1L);
                                    }
                                } else if (VidPlayActivity.repeatState != 2) {
                                } else {
                                    if (VidPlayActivity.this.isInPip()) {
                                        VidPlayActivity VidPlayActivity2 = VidPlayActivity.this;
                                        VidPlayActivity2.playNewVideo((VidPlayActivity2.currentWindowIndex + 1) % VidPlayActivity.this.mVideos.size());
                                        return;
                                    }
                                    VidPlayActivity.this.dismissAllDialog();
                                    VidPlayActivity.this.showDialogControl(7);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onPlayerError(PlaybackException exoPlaybackException) {
                    VidPlayActivity.this.updateLoading(false);
                    Log.d("binhnk08", "error = " + exoPlaybackException.getMessage());
                    if (!VidPlayActivity.isOnlinePlay) {
                        try {
                            String path = VidPlayActivity.this.currentVideo.getPath();
                            Log.d("binhnk08", "video: path = " + path + ", uri = " + VidPlayActivity.this.currentVideo.getUri());
                            if (TextUtils.isEmpty(path)) {
                                VidPlayActivity.this.dismissAllDialog();
                                if (!VidPlayActivity.this.isInPip()) {
                                    new AlertDialog.Builder(VidPlayActivity.this).setTitle(R.string.notice).setMessage(R.string.notice_video_play_error).setPositiveButton(R.string.got_it, (dialogInterface, i) -> onPlayerError$0$VidPlayActivity$PlaybackStateListener(dialogInterface, i)).show();
                                } else {
                                    VidPlayActivity.this.finishAndRemoveTask();
                                    Toast.makeText(VidPlayActivity.this, R.string.notice_video_play_error, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Media media = new Media(VidPlayActivity.this.mLibVLC, path);
                                media.setHWDecoderEnabled(true, false);
                                VidPlayActivity.mediaPlayer.setMedia(media);
                                media.release();
                                VidPlayActivity.mediaPlayer.play();
                                VidPlayActivity.this.playerView.setPlayVlc(true);
                            }
                        } catch (Exception e) {
                            Log.d("binhnk08", "Exception e = " + e);
                        }
                    } else {
                        VidPlayActivity.this.dismissAllDialog();
                        VidPlayActivity VidPlayActivity = VidPlayActivity.this;
                        if (!VidPlayActivity.this.isInPip()) {
                            new AlertDialog.Builder(VidPlayActivity.this).setTitle(R.string.notice).setMessage(R.string.notice_video_play_error).setPositiveButton(R.string.got_it, (dialogInterface, i) -> onPlayerError$1$VidPlayActivity$PlaybackStateListener(dialogInterface, i)).show();
                            return;
                        }
                        VidPlayActivity.this.finishAndRemoveTask();
                        Toast.makeText(VidPlayActivity.this, R.string.notice_video_play_error, Toast.LENGTH_SHORT).show();
                    }
                }


                public void onPlayerError$0$VidPlayActivity$PlaybackStateListener(DialogInterface dialogInterface, int i) {
                    VidPlayActivity.this.finishAndRemoveTask();
                }

                public void onPlayerError$1$VidPlayActivity$PlaybackStateListener(DialogInterface dialogInterface, int i) {
                    VidPlayActivity.this.finishAndRemoveTask();
                }
            });
            player.clearMediaItems();
            player.release();
            player = null;
        }
        this.titleView.setVisibility(View.GONE);
        ImageView imageView = this.buttonPiP;
        if (imageView != null) {
            imageView.setVisibility(View.GONE);
        }
        this.buttonAspectRatio.setVisibility(View.GONE);
        this.handlerSeekbar.removeCallbacks(this.runnableSeekbar);
        if (mediaPlayer != null) {
            if (this.playerView.isPlayVlc()) {
                this.isPlayVlcBeforeRelease = true;
                long time = mediaPlayer.getTime();
                this.currentSeek = time;
                updateVideoTimeData(time);
            } else {
                this.isPlayVlcBeforeRelease = false;
            }
            mediaPlayer.stop();
            mediaPlayer.detachViews();
            mediaPlayer.release();
            mediaPlayer = null;
            this.mLibVLC.release();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.stopVideoReceiver);
    }


    @Override
    public void onMoreClick(int i, boolean z) {
        if (z) {
            if (i == 0) {
                AdmobAdsHelper.showAdsNumberCount++;
                startCastActivity();
                return;
            }
            if (i == 1) {
                AdmobAdsHelper.showAdsNumberCount++;
                int i2 = repeatState;
                if (i2 == 0) {
                    repeatState = 1;
                } else if (i2 == 1) {
                    repeatState = 2;
                } else {
                    repeatState = 0;
                }
                VidPlayMorDialBuil vidPlayMorDialBuil = this.mMoreVideoDialogBuilder;
                if (vidPlayMorDialBuil != null) {
                    vidPlayMorDialBuil.setRepeatMode(repeatState);
                    return;
                }
                return;
            }
            if (i == 2) {
                AdmobAdsHelper.showAdsNumberCount++;
                boolean isNightMode = this.playerView.isNightMode();
                VidPlayMorDialBuil vidPlayMorDialBuil2 = this.mMoreVideoDialogBuilder;
                if (vidPlayMorDialBuil2 != null) {
                    vidPlayMorDialBuil2.setNightMode(!isNightMode);
                }
                this.playerView.setNightMode(!isNightMode);
                return;
            }
            if (i == 3) {
                AdmobAdsHelper.showAdsNumberCount++;
                boolean isMirror = this.playerView.isMirror();
                VidPlayMorDialBuil vidPlayMorDialBuil3 = this.mMoreVideoDialogBuilder;
                if (vidPlayMorDialBuil3 != null) {
                    vidPlayMorDialBuil3.setMirror(!isMirror);
                }
                this.playerView.setMirror(!isMirror);
                return;
            }
            if (i == 4) {
                AdmobAdsHelper.showAdsNumberCount++;
                if (this.playerView.isPlayVlc()) {
                    Toast.makeText(this, "Video format does not support this function yet", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    showDialogControl(4);
                    return;
                }
            }
            if (i == 5) {
                AdmobAdsHelper.showAdsNumberCount++;
                showDialogControl(5);
                return;
            } else {
                if (i == 6) {
                    AdmobAdsHelper.showAdsNumberCount++;
                    this.mMoreVideoDialogBuilder.dismiss();
                    this.playerView.setLockMode(true);
                    this.layoutControlTop.setVisibility(View.GONE);
                    this.layoutControlBottom.setVisibility(View.GONE);
                    this.buttonUnlock.setVisibility(View.VISIBLE);
                    return;
                }
                return;
            }
        }
        if (i == 0) {
            AdmobAdsHelper.showAdsNumberCount++;
            Intent intent = new Intent(this, VidTrimActivity.class);
            intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_PATH_TRIMMER, this.currentVideo.getPath());
            startActivity(intent);
            return;
        }
        if (i == 1) {
            AdmobAdsHelper.showAdsNumberCount++;
            VideoInfo videoInfo = this.currentVideo;
            if (videoInfo != null) {
                long videoId = videoInfo.getVideoId();
                boolean checkFavoriteVideoIdExisted = VideoFavoriteUtil.checkFavoriteVideoIdExisted(this, videoId);
                VidPlayMorDialBuil vidPlayMorDialBuil4 = this.mMoreVideoDialogBuilder;
                if (vidPlayMorDialBuil4 != null) {
                    vidPlayMorDialBuil4.setFavorite(!checkFavoriteVideoIdExisted);
                }
                VideoFavoriteUtil.addFavoriteVideoId(this, videoId, !checkFavoriteVideoIdExisted);
                return;
            }
            return;
        }
        if (i == 2) {
            AdmobAdsHelper.showAdsNumberCount++;
            startCastActivity();
            return;
        }
        if (i == 3) {
            AdmobAdsHelper.showAdsNumberCount++;
            int i3 = repeatState;
            if (i3 == 0) {
                repeatState = 1;
            } else if (i3 == 1) {
                repeatState = 2;
            } else {
                repeatState = 0;
            }
            VidPlayMorDialBuil vidPlayMorDialBuil5 = this.mMoreVideoDialogBuilder;
            if (vidPlayMorDialBuil5 != null) {
                vidPlayMorDialBuil5.setRepeatMode(repeatState);
                return;
            }
            return;
        }
        if (i == 4) {
            AdmobAdsHelper.showAdsNumberCount++;
            boolean isNightMode2 = this.playerView.isNightMode();
            VidPlayMorDialBuil vidPlayMorDialBuil6 = this.mMoreVideoDialogBuilder;
            if (vidPlayMorDialBuil6 != null) {
                vidPlayMorDialBuil6.setNightMode(!isNightMode2);
            }
            this.playerView.setNightMode(!isNightMode2);
            return;
        }
        if (i == 5) {
            AdmobAdsHelper.showAdsNumberCount++;
            if (this.playerView.isPlayVlc()) {
                Toast.makeText(this, "Video format does not support this function yet", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean isMirror2 = this.playerView.isMirror();
            VidPlayMorDialBuil vidPlayMorDialBuil7 = this.mMoreVideoDialogBuilder;
            if (vidPlayMorDialBuil7 != null) {
                vidPlayMorDialBuil7.setMirror(!isMirror2);
            }
            this.playerView.setMirror(!isMirror2);
            return;
        }
        if (i == 6) {
            AdmobAdsHelper.showAdsNumberCount++;
            if (this.playerView.isPlayVlc()) {
                Toast.makeText(this, "Video format does not support this function yet", Toast.LENGTH_SHORT).show();
                return;
            } else {
                showDialogControl(4);
                return;
            }
        }
        if (i == 7) {
            AdmobAdsHelper.showAdsNumberCount++;
            VideoInfo videoInfo2 = this.currentVideo;
            if (videoInfo2 != null) {
                Utility.shareVideo(this, videoInfo2);
                return;
            }
            return;
        }
        if (i != 8) {
            if (i == 9) {
                AdmobAdsHelper.showAdsNumberCount++;
                showDialogControl(5);
                return;
            }
            if (i != 10) {
                if (i == 11) {
                    AdmobAdsHelper.showAdsNumberCount++;
                    showDialogControl(6);
                    return;
                }
                return;
            }
            AdmobAdsHelper.showAdsNumberCount++;
            this.mMoreVideoDialogBuilder.dismiss();
            this.playerView.setLockMode(true);
            this.layoutControlTop.setVisibility(View.GONE);
            this.layoutControlBottom.setVisibility(View.GONE);
            this.buttonUnlock.setVisibility(View.VISIBLE);
            return;
        }
        AdmobAdsHelper.showAdsNumberCount++;
        Media media = new Media(this.mLibVLC, this.currentVideo.getPath());
        Log.e("TAG", "onMoreClick: " + FastSave.getInstance().getBoolean("HWD", false));
        if (!FastSave.getInstance().getBoolean("HWD", false)) {
            media.setHWDecoderEnabled(true, false);
            FastSave.getInstance().saveBoolean("HWD", true);
            VidPlayMorDialBuil vidPlayMorDialBuil8 = this.mMoreVideoDialogBuilder;
            if (vidPlayMorDialBuil8 != null) {
                vidPlayMorDialBuil8.setHWD(true);
                return;
            }
            return;
        }
        media.setHWDecoderEnabled(false, false);
        FastSave.getInstance().saveBoolean("HWD", false);
        VidPlayMorDialBuil vidPlayMorDialBuil9 = this.mMoreVideoDialogBuilder;
        if (vidPlayMorDialBuil9 != null) {
            vidPlayMorDialBuil9.setHWD(false);
        }
    }

    public void setSubtitleFile(String str) {
        MediaItem.Builder mimeType;
        SimpleExoPlayer simpleExoPlayer = player;
        if (simpleExoPlayer != null) {
            long currentPosition = simpleExoPlayer.getCurrentPosition();
            if (this.currentVideo != null) {
                if (isOnlinePlay) {
                    mimeType = new MediaItem.Builder().setUri(this.currentVideo.getUri());
                    if (isEnableSubtitle && !TextUtils.isEmpty(str)) {
                        subtitleUri = str;
                        Uri parse = Uri.parse(str);
                        mimeType.setSubtitles(Collections.singletonList(new MediaItem.Subtitle(parse, SubUti.getSubtitleMime(parse), SubUti.getSubtitleLanguage(parse), 1, 128, VidPlayUti.getFileName(this, parse))));
                    }
                } else {
                    mimeType = new MediaItem.Builder().setUri(this.currentVideo.getUri()).setMimeType(this.currentVideo.getMimeType());
                    if (isEnableSubtitle && !TextUtils.isEmpty(str)) {
                        subtitleUri = str;
                        if (!isChooseVideo) {
                            SubUti.cacheSubtitleFileUri(this, this.currentVideo.getVideoId(), str);
                        }
                        Uri parse2 = Uri.parse(str);
                        mimeType.setSubtitles(Collections.singletonList(new MediaItem.Subtitle(parse2, SubUti.getSubtitleMime(parse2), SubUti.getSubtitleLanguage(parse2), 1, 128, VidPlayUti.getFileName(this, parse2))));
                    }
                }
                player.setMediaItem(mimeType.build());
                if (currentPosition <= 0) {
                    currentPosition = 1;
                }
                player.seekTo(currentPosition);
            }
        }
    }

    public void setSubtitleSize(float f, int i) {
        float f2;
        SubtitleView subtitleView = this.playerView.getSubtitleView();
        if (subtitleView != null) {
            if (i == 2) {
                f2 = f * 0.0533f;
            } else {
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                float f3 = displayMetrics.heightPixels / displayMetrics.widthPixels;
                if (f3 < 1.0f) {
                    f3 = 1.0f / f3;
                }
                f2 = (f * 0.0533f) / f3;
            }
            subtitleView.setFractionalTextSize(f2 * 0.0533f);
        }
    }

    void setSubtitleSizePiP() {
        SubtitleView subtitleView = this.playerView.getSubtitleView();
        if (subtitleView != null) {
            subtitleView.setFractionalTextSize(0.0533f);
        }
    }

    void setSubtitleColor(int i) {
        String str = i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? "FFFFFF" : "#5EFFE2" : "#7DD0FF" : "#FFA6A6" : "#FFC692" : "#FFFFFF";
        CaptioningManager captioningManager = (CaptioningManager) getSystemService("captioning");
        SubtitleView subtitleView = this.playerView.getSubtitleView();
        if (captioningManager == null || captioningManager.isEnabled()) {
            if (subtitleView != null) {
                subtitleView.setUserDefaultStyle();
                subtitleView.setApplyEmbeddedStyles(false);
                return;
            }
            return;
        }
        CaptionStyleCompat captionStyleCompat = new CaptionStyleCompat(Color.parseColor(str), 0, 0, 1, ViewCompat.MEASURED_STATE_MASK, Typeface.DEFAULT_BOLD);
        if (subtitleView != null) {
            subtitleView.setStyle(captionStyleCompat);
            subtitleView.setApplyEmbeddedStyles(true);
        }
    }

    boolean isPiPSupported() {
        return Build.VERSION.SDK_INT >= 26 && getPackageManager().hasSystemFeature("android.software.picture_in_picture");
    }

    boolean hasPiPPermission() {
        AppOpsManager appOpsManager = (AppOpsManager) getSystemService("appops");
        return appOpsManager == null || appOpsManager.checkOpNoThrow("android:picture_in_picture", Process.myUid(), getPackageName()) != 0;
    }

    void updatePictureInPictureActions(boolean z) {
        ArrayList arrayList = new ArrayList();
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 3, new Intent(ACTION_MEDIA_CONTROL).putExtra(EXTRA_CONTROL_TYPE, 3), 67108864);
        Icon createWithResource = Icon.createWithResource(this, R.drawable.ic_prev_pip);
        PendingIntent broadcast2 = PendingIntent.getBroadcast(this, 1, new Intent(ACTION_MEDIA_CONTROL).putExtra(EXTRA_CONTROL_TYPE, 1), 67108864);
        Icon createWithResource2 = Icon.createWithResource(this, z ? R.drawable.ic_pause_pip : R.drawable.baseline_play_arrow_24_);
        PendingIntent broadcast3 = PendingIntent.getBroadcast(this, 2, new Intent(ACTION_MEDIA_CONTROL).putExtra(EXTRA_CONTROL_TYPE, 2), 67108864);
        Icon createWithResource3 = Icon.createWithResource(this, R.drawable.ic_next_pip);
        arrayList.add(new RemoteAction(createWithResource, "Previous", "Previous", broadcast));
        arrayList.add(new RemoteAction(createWithResource2, z ? "Pause" : "Play", z ? "Pause" : "Play", broadcast2));
        arrayList.add(new RemoteAction(createWithResource3, "Next", "Next", broadcast3));
        Object obj = this.mPictureInPictureParamsBuilder;
        if (obj instanceof PictureInPictureParams.Builder) {
            ((PictureInPictureParams.Builder) obj).setActions(arrayList);
            try {
                setPictureInPictureParams(((PictureInPictureParams.Builder) this.mPictureInPictureParamsBuilder).build());
            } catch (IllegalStateException unused) {
            }
        }
    }

    public boolean isInPip() {
        if (isPiPSupported()) {
            return isInPictureInPictureMode();
        }
        return false;
    }

    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (!isInPip()) {
            setSubtitleSize(subtitleSize, configuration.orientation);
        }
        dismissAllDialog();
    }

    void resetHideCallbacks() {
        SimpleExoPlayer simpleExoPlayer = player;
        if (simpleExoPlayer == null || !simpleExoPlayer.isPlaying()) {
            return;
        }
        this.playerView.setControllerShowTimeoutMs(DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS);
    }

    public void updateLoading(boolean z) {
        Log.e("TAG", "updateLoading: " + z);
        if (z) {
            this.layoutBottomPlay.setVisibility(View.GONE);
            this.loadingProgressBar.setVisibility(View.VISIBLE);
        } else {
            this.loadingProgressBar.setVisibility(View.GONE);
            this.layoutBottomPlay.setVisibility(View.VISIBLE);
        }
    }

    @Override // android.app.Activity
    protected void onUserLeaveHint() {
        if (!isPiPSupported() || hasPiPPermission() || this.playerView.isAudioMode()) {
            super.onUserLeaveHint();
        } else {
            enterPiP();
        }
    }

    private void enterPiP() {
        Format videoFormat;
        try {
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService("appops");
            if (appOpsManager != null && appOpsManager.checkOpNoThrow("android:picture_in_picture", Process.myUid(), getPackageName()) != 0) {
                startActivity(new Intent("android.settings.PICTURE_IN_PICTURE_SETTINGS", Uri.fromParts("package", getPackageName(), null)));
                return;
            }
            this.playerView.setControllerAutoShow(false);
            this.playerView.hideController();
            SimpleExoPlayer simpleExoPlayer = player;
            if (simpleExoPlayer != null && (videoFormat = simpleExoPlayer.getVideoFormat()) != null) {
                View videoSurfaceView = this.playerView.getVideoSurfaceView();
                if (videoSurfaceView instanceof SurfaceView) {
                    ((SurfaceView) videoSurfaceView).getHolder().setFixedSize(videoFormat.width, videoFormat.height);
                }
                Object obj = this.mPictureInPictureParamsBuilder;
                if (obj instanceof PictureInPictureParams.Builder) {
                    ((PictureInPictureParams.Builder) obj).setAspectRatio(new Rational(16, 9));
                }
            }
            Object obj2 = this.mPictureInPictureParamsBuilder;
            if (obj2 instanceof PictureInPictureParams.Builder) {
                enterPictureInPictureMode(((PictureInPictureParams.Builder) obj2).build());
            }
        } catch (Exception unused) {
        }
    }

    private void setMuteAction(boolean z) {
        SimpleExoPlayer simpleExoPlayer = player;
        if (simpleExoPlayer != null) {
            if (z) {
                this.currentVolume = simpleExoPlayer.getDeviceVolume();
                player.setDeviceVolume(0);
            } else {
                simpleExoPlayer.setDeviceVolume((int) this.currentVolume);
            }
            this.buttonVolume.setActivated(z);
        }
    }

    public static class AnonymousClass13 {
        static final int[] OOrientation;

        static {
            int[] iArr = new int[VidPlayUti.Orientation.values().length];
            OOrientation = iArr;
            try {
                iArr[VidPlayUti.Orientation.SENSOR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                OOrientation[VidPlayUti.Orientation.LANDSCAPE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                OOrientation[VidPlayUti.Orientation.PORTRAIT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    private void setIconOrientation(VidPlayUti.Orientation orientation) {
        int i = AnonymousClass13.OOrientation[orientation.ordinal()];
        if (i == 1) {
            this.buttonRotation.setImageResource(R.drawable.baseline_screen_rotation_24);
        } else if (i == 2) {
            this.buttonRotation.setImageResource(R.drawable.baseline_screen_lock_landscape_24);
        } else if (i == 3) {
            this.buttonRotation.setImageResource(R.drawable.baseline_screen_lock_rotation_24);
        }
    }

    public void setAudioMode(boolean z) {
        if (z) {
            this.buttonAudio.setImageResource(R.drawable.baseline_smart_display_24px);
            this.playerView.setAudioMode(true);
            this.layoutBottomEnd.setVisibility(View.GONE);
        } else {
            this.buttonAudio.setImageResource(R.drawable.baseline_headphones_24);
            this.playerView.setAudioMode(false);
            this.layoutBottomEnd.setVisibility(View.VISIBLE);
        }
        this.isAudioMode = z;
    }

    public void showDialogControl(int i) {
        if (isInPip()) {
            return;
        }
        this.playerView.hideController();
        boolean z = true;
        switch (i) {
            case 1:
                VidPlayNxtInDialBuil vidPlayNxtInDialBuil = new VidPlayNxtInDialBuil(this, this.currentWindowIndex, this.mVideos, new NexVidPlaAdapter.Callback() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda15
                    @Override // com.videoplayer.videox.adapter.vid.NexVidPlaAdapter.Callback
                    public void onVideoPlay(int i2) {
                        VidPlayActivity.this.m539x59d8ca31(i2);
                    }
                });
                this.mNextInPlaylistDialogBuilder = vidPlayNxtInDialBuil;
                vidPlayNxtInDialBuil.build().show();
                break;
            case 2:
                VidPlaySubDialBuil vidPlaySubDialBuil = new VidPlaySubDialBuil(this, new AnonymousClass7());
                this.mSubtitleDialogBuilder = vidPlaySubDialBuil;
                vidPlaySubDialBuil.setEnableSubtitle(isEnableSubtitle);
                this.mSubtitleDialogBuilder.setColorSubtitle(subtitleColor);
                this.mSubtitleDialogBuilder.setSubtitleSize(subtitleSize);
                this.mSubtitleDialogBuilder.setCurrentSubtitle(VidPlayUti.getFileName(this, Uri.parse(subtitleUri)));
                this.mSubtitleDialogBuilder.build().show();
                break;
            case 3:
                VideoInfo videoInfo = this.currentVideo;
                if (videoInfo != null) {
                    boolean checkFavoriteVideoIdExisted = VideoFavoriteUtil.checkFavoriteVideoIdExisted(this, videoInfo.getVideoId());
                    if (!isOnlinePlay && !isChooseVideo) {
                        z = false;
                    }
                    VidPlayMorDialBuil vidPlayMorDialBuil = new VidPlayMorDialBuil(this, z, this);
                    this.mMoreVideoDialogBuilder = vidPlayMorDialBuil;
                    vidPlayMorDialBuil.setFavorite(checkFavoriteVideoIdExisted);
                    this.mMoreVideoDialogBuilder.setRepeatMode(repeatState);
                    this.mMoreVideoDialogBuilder.setMirror(this.playerView.isMirror());
                    this.mMoreVideoDialogBuilder.setNightMode(this.playerView.isNightMode());
                    this.mMoreVideoDialogBuilder.build().show();
                    break;
                }
                break;
            case 4:
                if (player != null) {
                    this.mMoreVideoDialogBuilder.dismiss();
                    Dialog build = new VidPlayEquDiaBuil(this, player.getAudioSessionId()).build();
                    this.mEqualizerDialog = build;
                    build.show();
                    break;
                }
                break;
            case 5:
                VidPlayMorDialBuil vidPlayMorDialBuil2 = this.mMoreVideoDialogBuilder;
                if (vidPlayMorDialBuil2 != null) {
                    vidPlayMorDialBuil2.dismiss();
                }
                VidPlaTimDialBuil vidPlaTimDialBuil = new VidPlaTimDialBuil(this, new VidPlaTimDialBuil.Callback() { // from class: com.videoplayer.videox.activity.VidPlayActivity$$ExternalSyntheticLambda16
                    @Override // com.videoplayer.videox.dialog.VidPlaTimDialBuil.Callback
                    public void startTimeCountdown(long j) {
                        VidPlayActivity.this.m540x59626432(j);
                    }
                });
                this.mTimerDialogBuilder = vidPlaTimDialBuil;
                vidPlaTimDialBuil.build().show();
                break;
            case 6:
                VidPlayMorDialBuil vidPlayMorDialBuil3 = this.mMoreVideoDialogBuilder;
                if (vidPlayMorDialBuil3 != null) {
                    vidPlayMorDialBuil3.dismiss();
                }
                VideoInfo videoInfo2 = this.currentVideo;
                if (videoInfo2 != null) {
                    Dialog build2 = new VidPlayInfDialBuil(this, videoInfo2).build();
                    this.mInfoVideoDialog = build2;
                    build2.show();
                    break;
                }
                break;
            case 7:
                List<VideoInfo> list = this.mVideos;
                if (list != null && list.size() > 1 && !isInPip()) {
                    try {
                        List<VideoInfo> list2 = this.mVideos;
                        VidPlaEndVidDiaBuil vidPlaEndVidDiaBuil = new VidPlaEndVidDiaBuil(this, list2.get((this.currentWindowIndex + 1) % list2.size()), new VidPlaEndVidDiaBuil.Callback() { // from class: com.videoplayer.videox.activity.VidPlayActivity.10
                            @Override // com.videoplayer.videox.dialog.VidPlaEndVidDiaBuil.Callback
                            public void onReplayVideo() {
                                if (VidPlayActivity.this.playerView.isPlayVlc()) {
                                    if (VidPlayActivity.mediaPlayer != null) {
                                        VidPlayActivity.mediaPlayer.setMedia(VidPlayActivity.mediaPlayer.getMedia());
                                        VidPlayActivity.mediaPlayer.play();
                                        VidPlayActivity.mediaPlayer.setTime(0L);
                                    }
                                } else if (VidPlayActivity.player != null) {
                                    VidPlayActivity.player.seekTo(1L);
                                }
                                if (VidPlayActivity.this.mEndVideoDialogBuilder != null) {
                                    VidPlayActivity.this.mEndVideoDialogBuilder.dismiss();
                                }
                            }

                            @Override // com.videoplayer.videox.dialog.VidPlaEndVidDiaBuil.Callback
                            public void onNextVideo(VideoInfo videoInfo3) {
                                if (!VidPlayActivity.this.playerView.isPlayVlc() && VidPlayActivity.player != null) {
                                    VidPlayActivity.this.updateVideoTimeData(VidPlayActivity.player.getCurrentPosition());
                                } else if (VidPlayActivity.mediaPlayer != null) {
                                    VidPlayActivity.this.updateVideoTimeData(VidPlayActivity.mediaPlayer.getTime());
                                }
                                VidPlayActivity vidPlayActivity = VidPlayActivity.this;
                                vidPlayActivity.playNewVideo((vidPlayActivity.currentWindowIndex + 1) % VidPlayActivity.this.mVideos.size());
                                if (VidPlayActivity.this.mEndVideoDialogBuilder != null) {
                                    VidPlayActivity.this.mEndVideoDialogBuilder.dismiss();
                                }
                            }
                        });
                        this.mEndVideoDialogBuilder = vidPlaEndVidDiaBuil;
                        if (!isFinishing()) {
                            vidPlaEndVidDiaBuil.build().show();
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    /* renamed from: showDialogControl$19$VidPlayActivity, reason: merged with bridge method [inline-methods] */
    public void m539x59d8ca31(int i) {
        SimpleExoPlayer simpleExoPlayer;
        this.mNextInPlaylistDialogBuilder.updateCurrentPosition(i);
        if (this.playerView.isPlayVlc() || (simpleExoPlayer = player) == null) {
            MediaPlayer mediaPlayer2 = mediaPlayer;
            if (mediaPlayer2 != null) {
                updateVideoTimeData(mediaPlayer2.getTime());
            }
        } else {
            updateVideoTimeData(simpleExoPlayer.getCurrentPosition());
        }
        playNewVideo(i);
    }

    public class AnonymousClass7 implements VidPlaySubDialBuil.Callback {
        VidPlaySubtFileListDialBuil subtitleFileDialog;

        AnonymousClass7() {
        }

        @Override // com.videoplayer.videox.dialog.VidPlaySubDialBuil.Callback
        public void onColorSubtitleChanged(int i) {
            VidPlayActivity.this.setSubtitleColor(i);
            VidPlayActivity.subtitleColor = i;
        }

        @Override // com.videoplayer.videox.dialog.VidPlaySubDialBuil.Callback
        public void onSizeSubtitleChanged(float f) {
            VidPlayActivity vidPlayActivity = VidPlayActivity.this;
            vidPlayActivity.setSubtitleSize(f, vidPlayActivity.getResources().getConfiguration().orientation);
            VidPlayActivity.subtitleSize = f;
        }

        @Override // com.videoplayer.videox.dialog.VidPlaySubDialBuil.Callback
        public void onSubtitleFileSelect() {
            VidPlaySubtFileListDialBuil vidPlaySubtFileListDialBuil = new VidPlaySubtFileListDialBuil(VidPlayActivity.this, new VidSubAdapter.Callback() { // from class: com.videoplayer.videox.activity.VidPlayActivity$AnonymousClass7$$ExternalSyntheticLambda0
                @Override // com.videoplayer.videox.adapter.vid.VidSubAdapter.Callback
                public void onSubtitleSelect(VideoSubtitle videoSubtitle) {
                    AnonymousClass7.this.m545x48e4f125(videoSubtitle);
                }
            });
            this.subtitleFileDialog = vidPlaySubtFileListDialBuil;
            vidPlaySubtFileListDialBuil.build().show();
        }

        /* renamed from: onSubtitleFileSelect$0$VidPlayActivity$7, reason: merged with bridge method [inline-methods] */
        public void m545x48e4f125(VideoSubtitle videoSubtitle) {
            VidPlayActivity.isEnableSubtitle = true;
            VidPlayActivity.this.setSubtitleFile(videoSubtitle.getUri());
            this.subtitleFileDialog.dismiss();
            if (VidPlayActivity.this.mSubtitleDialogBuilder == null || !VidPlayActivity.this.mSubtitleDialogBuilder.isShowing()) {
                return;
            }
            VidPlayActivity.this.mSubtitleDialogBuilder.setCurrentSubtitle(videoSubtitle.getName());
            VidPlayActivity.this.mSubtitleDialogBuilder.setEnableSubtitle(true);
        }

        @Override // com.videoplayer.videox.dialog.VidPlaySubDialBuil.Callback
        public void onSubtitleOnlineSelect() {
            Toast.makeText(VidPlayActivity.this, R.string.coming_soon, Toast.LENGTH_SHORT).show();
        }

        @Override // com.videoplayer.videox.dialog.VidPlaySubDialBuil.Callback
        public void onEnableSubtitle(boolean z) {
            VidPlayActivity.isEnableSubtitle = z;
            VidPlayActivity.this.setSubtitleFile(VidPlayActivity.subtitleUri);
        }
    }

    /* renamed from: showDialogControl$20$VidPlayActivity, reason: merged with bridge method [inline-methods] */
    public void m540x59626432(long j) {
        CountDownTimer countDownTimer = this.countDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (j == -1) {
            this.layoutTimer.setVisibility(View.INVISIBLE);
            return;
        }
        this.layoutTimer.setVisibility(View.VISIBLE);
        CountDownTimer countDownTimer2 = new CountDownTimer(j, 1000L) { // from class: com.videoplayer.videox.activity.VidPlayActivity.11
            @Override // android.os.CountDownTimer
            public void onTick(long j2) {
                VidPlayActivity.this.tvTimer.setText(Utility.convertLongToDuration(j2));
            }

            @Override // android.os.CountDownTimer
            public void onFinish() {
                VidPlayActivity.this.finishAndRemoveTask();
            }
        };
        this.countDownTimer = countDownTimer2;
        countDownTimer2.start();
    }

    public void dismissAllDialog() {
        Dialog dialog = this.mEqualizerDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mEqualizerDialog.dismiss();
        }
        Dialog dialog2 = this.mInfoVideoDialog;
        if (dialog2 != null && dialog2.isShowing()) {
            this.mInfoVideoDialog.dismiss();
        }
        Dialog dialog3 = this.mGuideDialog;
        if (dialog3 != null && dialog3.isShowing()) {
            this.mGuideDialog.dismiss();
        }
        VidPlayNxtInDialBuil vidPlayNxtInDialBuil = this.mNextInPlaylistDialogBuilder;
        if (vidPlayNxtInDialBuil != null && vidPlayNxtInDialBuil.isShowing()) {
            this.mNextInPlaylistDialogBuilder.dismiss();
        }
        VidPlayMorDialBuil vidPlayMorDialBuil = this.mMoreVideoDialogBuilder;
        if (vidPlayMorDialBuil != null && vidPlayMorDialBuil.isShowing()) {
            this.mMoreVideoDialogBuilder.dismiss();
        }
        VidPlaEndVidDiaBuil vidPlaEndVidDiaBuil = this.mEndVideoDialogBuilder;
        if (vidPlaEndVidDiaBuil != null && vidPlaEndVidDiaBuil.isShowing()) {
            this.mEndVideoDialogBuilder.dismiss();
        }
        VidPlaTimDialBuil vidPlaTimDialBuil = this.mTimerDialogBuilder;
        if (vidPlaTimDialBuil != null && vidPlaTimDialBuil.isShowing()) {
            this.mTimerDialogBuilder.dismiss();
        }
        VidPlaySubDialBuil vidPlaySubDialBuil = this.mSubtitleDialogBuilder;
        if (vidPlaySubDialBuil == null || !vidPlaySubDialBuil.isShowing()) {
            return;
        }
        this.mSubtitleDialogBuilder.dismiss();
    }

    private int getCurrentDialogIsShowing() {
        VidPlayNxtInDialBuil vidPlayNxtInDialBuil = this.mNextInPlaylistDialogBuilder;
        if (vidPlayNxtInDialBuil != null && vidPlayNxtInDialBuil.isShowing()) {
            return 1;
        }
        VidPlaySubDialBuil vidPlaySubDialBuil = this.mSubtitleDialogBuilder;
        if (vidPlaySubDialBuil != null && vidPlaySubDialBuil.isShowing()) {
            return 2;
        }
        VidPlayMorDialBuil vidPlayMorDialBuil = this.mMoreVideoDialogBuilder;
        if (vidPlayMorDialBuil != null && vidPlayMorDialBuil.isShowing()) {
            return 3;
        }
        Dialog dialog = this.mEqualizerDialog;
        if (dialog != null && dialog.isShowing()) {
            return 4;
        }
        VidPlaTimDialBuil vidPlaTimDialBuil = this.mTimerDialogBuilder;
        if (vidPlaTimDialBuil != null && vidPlaTimDialBuil.isShowing()) {
            return 5;
        }
        Dialog dialog2 = this.mInfoVideoDialog;
        if (dialog2 != null && dialog2.isShowing()) {
            return 6;
        }
        VidPlaEndVidDiaBuil vidPlaEndVidDiaBuil = this.mEndVideoDialogBuilder;
        return (vidPlaEndVidDiaBuil == null || !vidPlaEndVidDiaBuil.isShowing()) ? 0 : 7;
    }

    private void startCastActivity() {
        try {
            startActivity(new Intent("android.settings.WIFI_DISPLAY_SETTINGS"));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                try {
                    startActivity(new Intent("com.samsung.wfd.LAUNCH_WFD_PICKER_DLG"));
                } catch (Exception unused) {
                    Toast.makeText(this, R.string.device_not_supported, 1).show();
                }
            } catch (Exception unused2) {
                startActivity(new Intent("android.settings.CAST_SETTINGS"));
            }
        }
    }

    private void sendBroadcastPauseMusic() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("RECEIVER_STOP_MUSIC"));
    }

    public void updateVideoTimeData(long j) {
        VideoInfo videoInfo;
        if (!isOnlinePlay && !isChooseVideo && this.mPresenter != null && (videoInfo = this.currentVideo) != null) {
            if (j >= videoInfo.getDuration()) {
                j = 0;
            }
            this.mPresenter.updateVideoTimeData(this.currentVideo.getVideoId(), j);
        }
    }

    public long getVideoTimeData() {
        if (!isOnlinePlay && !isChooseVideo && this.mSettingPrefUtils.isResumeVideo() && this.currentVideo != null && this.mPresenter != null) {
            long aVideoTimeData = this.mPresenter.getAVideoTimeData(this.currentVideo.getVideoId());
            if (aVideoTimeData > 0 && aVideoTimeData < this.currentVideo.getDuration()) {
                return aVideoTimeData;
            }
        }
        return 1L;
    }

    private void startMusicServiceForPlayBackground() {
        if (this.currentVideo == null) {
            finishAndRemoveTask();
            return;
        }
        final MusicInfo musicInfo = new MusicInfo();
        musicInfo.setId(-1L);
        musicInfo.setUri(this.currentVideo.getUri());
        musicInfo.setPath(this.currentVideo.getPath());
        musicInfo.setDisplayName(this.currentVideo.getDisplayName());
        Intent intent = new Intent(this, MusServ.class);
        startService(intent);
        bindService(intent, new ServiceConnection() { // from class: com.videoplayer.videox.activity.VidPlayActivity.12
            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
            }

            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MusServ service = ((MusServ.MyBinder) iBinder).getService();
                if (service != null) {
                    service.setMusicInfoList(new ArrayList(Collections.singletonList(musicInfo)));
                    service.setIndex(0);
                    service.setRepeatState(0);
                    service.playMusic();
                    service.seekTo((int) VidPlayActivity.this.currentSeek);
                }
                VidPlayActivity.this.unbindService(this);
            }
        }, 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.isCallOnStop = false;
        this.isPlayAudioBeforeUserLeave = false;
        sendBroadcastPauseMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("binhnk08 ", "onPause ");
    }
}
