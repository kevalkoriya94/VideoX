package com.videoplayer.videox.cv;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.GestureDetectorCompat;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.videoplayer.videox.R;
import com.videoplayer.videox.activity.VidPlayActivity;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.uti.vid.BrightCon;
import com.videoplayer.videox.uti.vid.VidPlayUti;

import org.videolan.libvlc.util.VLCVideoLayout;

import java.util.Collections;


public class CustStyPlayView extends StyledPlayerView implements GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {
    private final float IGNORE_BORDER;
    private final float SCROLL_STEP;
    private final float SCROLL_STEP_SEEK;
    private boolean canBoostVolume;
    private boolean canScale;
    private boolean canSetAutoBrightness;
    private int eventClick;
    private final TextView exoErrorMessage;
    private Orientation gestureOrientation;
    private float gestureScrollX;
    private float gestureScrollY;
    private boolean isAudioMode;
    private boolean isLockMode;
    private boolean isMirror;
    private boolean isNightMode;
    private boolean isPlayVlc;
    private boolean isSlideForBrightness;
    private boolean isSlideForSound;
    private final ImageView ivVolume;
    private final FrameLayout layoutNightMode;
    private final FrameLayout layoutPlayAsAudio;
    private final AudioManager mAudioManager;
    private final Context mContext;
    private final GestureDetectorCompat mDetector;
    private final ScaleGestureDetector mScaleDetector;
    private float mScaleFactor;
    private float mScaleFactorFit;
    private boolean restorePlayState;
    private final RelativeLayout rlBrightness;
    private final RelativeLayout rlVolume;
    public final Runnable runnableBrightness;
    public final Runnable runnableVolume;
    private final VertSeekBar sbBrightness;
    private final VertSeekBar sbVolume;
    private long seekChange;
    private long seekMax;
    private long seekStart;
    private final SettingPrefUtils settingPrefUtils;
    final Rect systemGestureExclusionRect;
    public final Runnable textClearRunnable;
    private final TextView tvBrightness;
    private final TextView tvVolume;
    private final VLCVideoLayout vlcVideoLayout;

    public enum Orientation {
        HORIZONTAL,
        VERTICAL,
        UNKNOWN
    }

    private boolean isCrossingThreshold(float f, float f2, float f3) {
        return (f < f3 && f2 >= f3) || (f > f3 && f2 <= f3);
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onLongPress(MotionEvent motionEvent) {
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    public CustStyPlayView(Context context) {
        this(context, null);
    }

    public CustStyPlayView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CustStyPlayView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.gestureOrientation = Orientation.UNKNOWN;
        this.gestureScrollY = 0.0f;
        this.gestureScrollX = 0.0f;
        this.canBoostVolume = false;
        this.canSetAutoBrightness = false;
        this.IGNORE_BORDER = VidPlayUti.dpToPx(24);
        this.SCROLL_STEP = VidPlayUti.dpToPx(16);
        this.SCROLL_STEP_SEEK = VidPlayUti.dpToPx(8);
        this.canScale = true;
        this.mScaleFactor = 1.0f;
        this.systemGestureExclusionRect = new Rect();
        this.textClearRunnable = new Runnable() { // from class: com.videoplayer.videox.cv.CustStyPlayView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CustStyPlayView.this.m864lambda$new$0$comvideoplayervideoxcvCustStyPlayView();
            }
        };
        this.runnableVolume = new Runnable() { // from class: com.videoplayer.videox.cv.CustStyPlayView.1
            @Override // java.lang.Runnable
            public void run() {
                CustStyPlayView.this.rlVolume.setVisibility(View.GONE);
            }
        };
        this.runnableBrightness = new Runnable() { // from class: com.videoplayer.videox.cv.CustStyPlayView.2
            @Override // java.lang.Runnable
            public void run() {
                CustStyPlayView.this.rlBrightness.setVisibility(View.GONE);
            }
        };
        this.eventClick = 0;
        this.isMirror = false;
        this.isNightMode = false;
        this.isLockMode = false;
        this.isPlayVlc = false;
        this.mDetector = new GestureDetectorCompat(context, this);
        this.mContext = context;
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        this.exoErrorMessage = (TextView) findViewById(R.id.exo_error_message);
        this.rlBrightness = (RelativeLayout) findViewById(R.id.rl_brightness);
        this.rlVolume = (RelativeLayout) findViewById(R.id.rl_volume);
        this.tvBrightness = (TextView) findViewById(R.id.tv_brightness);
        this.tvVolume = (TextView) findViewById(R.id.tv_volume);
        VertSeekBar vertSeekBar = (VertSeekBar) findViewById(R.id.sb_brightness);
        this.sbBrightness = vertSeekBar;
        VertSeekBar vertSeekBar2 = (VertSeekBar) findViewById(R.id.sb_volume);
        this.sbVolume = vertSeekBar2;
        this.ivVolume = (ImageView) findViewById(R.id.iv_volume);
        this.layoutNightMode = (FrameLayout) findViewById(R.id.background_night_mode);
        this.layoutPlayAsAudio = (FrameLayout) findViewById(R.id.view_play_audio);
        this.vlcVideoLayout = (VLCVideoLayout) findViewById(R.id.vlc_video_view);
        vertSeekBar2.setEnabled(false);
        vertSeekBar.setEnabled(false);
        this.mScaleDetector = new ScaleGestureDetector(context, this);
        SettingPrefUtils settingPrefUtils = new SettingPrefUtils(context);
        this.settingPrefUtils = settingPrefUtils;
        this.isSlideForSound = settingPrefUtils.isSlideForSound();
        this.isSlideForBrightness = settingPrefUtils.isSlideForBrightness();
    }

    /* renamed from: lambda$new$0$com-videoplayer-videox-cv-CustStyPlayView, reason: not valid java name */
    void m864lambda$new$0$comvideoplayervideoxcvCustStyPlayView() {
        setCustomErrorMessage(null);
        clearIcon();
    }

    public void clearIcon() {
        this.exoErrorMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        setHighlight(false);
    }

    @Override // com.google.android.exoplayer2.ui.StyledPlayerView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.gestureOrientation == Orientation.UNKNOWN) {
            this.mScaleDetector.onTouchEvent(motionEvent);
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            removeCallbacks(this.textClearRunnable);
        } else if (actionMasked == 1) {
            if (this.gestureOrientation == Orientation.HORIZONTAL) {
                setCustomErrorMessage(null);
            } else {
                postDelayed(this.textClearRunnable, 400L);
            }
            if (this.restorePlayState && VidPlayActivity.player != null) {
                this.restorePlayState = false;
                VidPlayActivity.player.play();
            }
            setControllerAutoShow(true);
        }
        if (motionEvent.getPointerCount() == 1) {
            this.mDetector.onTouchEvent(motionEvent);
        }
        return true;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent motionEvent) {
        this.gestureScrollY = 0.0f;
        this.gestureScrollX = 0.0f;
        this.gestureOrientation = Orientation.UNKNOWN;
        this.isSlideForSound = this.settingPrefUtils.isSlideForSound();
        this.isSlideForBrightness = this.settingPrefUtils.isSlideForBrightness();
        return false;
    }

    public boolean tap() {
        if (!VidPlayActivity.controllerVisibleFully) {
            showController();
            return true;
        }
        hideController();
        return true;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (!this.isLockMode && !this.mScaleDetector.isInProgress() && VidPlayActivity.player != null && VidPlayActivity.mediaPlayer != null && motionEvent.getY() >= this.IGNORE_BORDER && motionEvent.getX() >= this.IGNORE_BORDER && motionEvent.getY() <= getHeight() - this.IGNORE_BORDER && motionEvent.getX() <= getWidth() - this.IGNORE_BORDER) {
            if (this.gestureScrollY == 0.0f || this.gestureScrollX == 0.0f) {
                this.gestureScrollY = 1.0E-4f;
                this.gestureScrollX = 1.0E-4f;
            } else {
                if (this.gestureOrientation == Orientation.HORIZONTAL || this.gestureOrientation == Orientation.UNKNOWN) {
                    float f3 = this.gestureScrollX + f;
                    this.gestureScrollX = f3;
                    if (Math.abs(f3) > this.SCROLL_STEP || (this.gestureOrientation == Orientation.HORIZONTAL && Math.abs(this.gestureScrollX) > this.SCROLL_STEP_SEEK)) {
                        setControllerAutoShow(false);
                        if (this.gestureOrientation == Orientation.UNKNOWN) {
                            if (VidPlayActivity.player.isPlaying()) {
                                this.restorePlayState = true;
                                VidPlayActivity.player.pause();
                            }
                            clearIcon();
                            if (this.isPlayVlc) {
                                this.seekStart = VidPlayActivity.mediaPlayer.getTime();
                                this.seekChange = 0L;
                                this.seekMax = VidPlayActivity.mediaPlayer.getLength();
                            } else {
                                this.seekStart = VidPlayActivity.player.getCurrentPosition();
                                this.seekChange = 0L;
                                this.seekMax = VidPlayActivity.player.getDuration();
                            }
                        }
                        this.gestureOrientation = Orientation.HORIZONTAL;
                        float max = Math.max(0.5f, Math.min(Math.abs(VidPlayUti.pxToDp(f) / 4.0f), 10.0f));
                        if (this.gestureScrollX > 0.0f) {
                            long j = this.seekStart;
                            long j2 = this.seekChange;
                            float f4 = max * 1000.0f;
                            if ((j + j2) - f4 >= 0.0f) {
                                long j3 = (long) (j2 - f4);
                                this.seekChange = j3;
                                long j4 = j + j3;
                                if (this.isPlayVlc) {
                                    VidPlayActivity.mediaPlayer.setTime(j4);
                                } else {
                                    VidPlayActivity.player.setSeekParameters(SeekParameters.PREVIOUS_SYNC);
                                    VidPlayActivity.player.seekTo(j4);
                                }
                            }
                        } else {
                            long j5 = this.seekMax;
                            if (j5 == C.TIME_UNSET) {
                                long j6 = (long) (this.seekChange + (max * 1000.0f));
                                this.seekChange = j6;
                                long j7 = this.seekStart + j6;
                                if (this.isPlayVlc) {
                                    VidPlayActivity.mediaPlayer.setTime(j7);
                                } else {
                                    VidPlayActivity.player.setSeekParameters(SeekParameters.NEXT_SYNC);
                                    VidPlayActivity.player.seekTo(j7);
                                }
                            } else {
                                long j8 = this.seekStart;
                                long j9 = this.seekChange;
                                if (j8 + j9 + 1000 < j5) {
                                    long j10 = (long) (j9 + (max * 1000.0f));
                                    this.seekChange = j10;
                                    long j11 = j8 + j10;
                                    if (this.isPlayVlc) {
                                        VidPlayActivity.mediaPlayer.setTime(j11);
                                    } else {
                                        VidPlayActivity.player.setSeekParameters(SeekParameters.NEXT_SYNC);
                                        VidPlayActivity.player.seekTo(j11);
                                    }
                                }
                            }
                        }
                        setCustomErrorMessage(VidPlayUti.formatMilisSign(this.seekChange));
                        this.gestureScrollX = 1.0E-4f;
                    }
                }
                if (this.gestureOrientation == Orientation.VERTICAL || this.gestureOrientation == Orientation.UNKNOWN) {
                    float f5 = this.gestureScrollY + f2;
                    this.gestureScrollY = f5;
                    if (Math.abs(f5) > this.SCROLL_STEP) {
                        if (this.gestureOrientation == Orientation.UNKNOWN) {
                            this.canBoostVolume = VidPlayUti.isVolumeMax(this.mAudioManager);
                            this.canSetAutoBrightness = BrightCon.getInstance().getCurrentBrightnessLevel() <= 0;
                        }
                        this.gestureOrientation = Orientation.VERTICAL;
                        if (motionEvent.getX() < getWidth() / 2) {
                            if (this.isSlideForBrightness) {
                                this.eventClick = 1;
                                BrightCon.getInstance().changeBrightness((Activity) this.mContext, this, this.gestureScrollY > 0.0f, this.canSetAutoBrightness);
                            }
                        } else if (this.isSlideForSound) {
                            this.eventClick = 2;
                            VidPlayUti.adjustVolume(this.mContext, this, this.gestureScrollY > 0.0f, this.canBoostVolume);
                        }
                        this.gestureScrollY = 1.0E-4f;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        Log.d("binhnk08", " onFling:  eventClick = " + this.eventClick);
        this.eventClick = 0;
        return false;
    }

    @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        if (!this.canScale) {
            return false;
        }
        float f = this.mScaleFactor;
        float scaleFactor = scaleGestureDetector.getScaleFactor() * f;
        this.mScaleFactor = scaleFactor;
        float max = Math.max(0.25f, Math.min(scaleFactor, 3.0f));
        this.mScaleFactor = max;
        if (isCrossingThreshold(f, max, 1.0f) || isCrossingThreshold(f, this.mScaleFactor, this.mScaleFactorFit)) {
            performHapticFeedback(1);
        }
        setScale(this.mScaleFactor);
        restoreSurfaceView();
        clearIcon();
        setCustomErrorMessage(((int) (this.mScaleFactor * 100.0f)) + "%");
        return true;
    }

    @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        if (this.isAudioMode || this.isLockMode || !this.settingPrefUtils.isPitchToZoom()) {
            return false;
        }
        if (this.isPlayVlc) {
            Toast.makeText(this.mContext, "Video format does not support this function yet", Toast.LENGTH_SHORT).show();
            return false;
        }
        this.mScaleFactor = Math.abs(getVideoSurfaceView().getScaleX());
        if (getResizeMode() != 4) {
            this.canScale = false;
            setAspectRatioListener(new AspectRatioFrameLayout.AspectRatioListener() { // from class: com.videoplayer.videox.cv.CustStyPlayView$$ExternalSyntheticLambda1
                @Override // com.google.android.exoplayer2.ui.AspectRatioFrameLayout.AspectRatioListener
                public final void onAspectRatioUpdated(float f, float f2, boolean z) {
                    CustStyPlayView.this.m865lambda$onScaleBegin$1$comvideoplayervideoxcvCustStyPlayView(f, f2, z);
                }
            });
            getVideoSurfaceView().setAlpha(0.0f);
            setResizeMode(4);
        } else {
            this.mScaleFactorFit = getScaleFit();
            this.canScale = true;
        }
        return true;
    }

    /* renamed from: lambda$onScaleBegin$1$com-videoplayer-videox-cv-CustStyPlayView, reason: not valid java name */
    void m865lambda$onScaleBegin$1$comvideoplayervideoxcvCustStyPlayView(float f, float f2, boolean z) {
        setAspectRatioListener(null);
        float scaleFit = getScaleFit();
        this.mScaleFactorFit = scaleFit;
        this.mScaleFactor = scaleFit;
        this.canScale = true;
    }

    @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        restoreSurfaceView();
        Log.d("binhnk08", " onScaleEnd:  click_zoom_and_pan");
    }

    private void restoreSurfaceView() {
        if (getVideoSurfaceView().getAlpha() != 1.0f) {
            getVideoSurfaceView().setAlpha(1.0f);
        }
    }

    private float getScaleFit() {
        return Math.min(getHeight() / getVideoSurfaceView().getHeight(), getWidth() / getVideoSurfaceView().getWidth());
    }

    public void setMirror(boolean z) {
        this.isMirror = z;
        float scaleX = getVideoSurfaceView().getScaleX();
        getVideoSurfaceView().setScaleX(z ? -Math.abs(scaleX) : Math.abs(scaleX));
    }

    public boolean isMirror() {
        return this.isMirror;
    }

    public void setNightMode(boolean z) {
        this.layoutNightMode.setVisibility(z ? 0 : 8);
        this.isNightMode = z;
    }

    public boolean isNightMode() {
        return this.isNightMode;
    }

    public void showVolume(int i, boolean z) {
        if (i > 25) {
            i = 25;
        }
        this.rlVolume.setVisibility(View.VISIBLE);
        StringBuilder sb = new StringBuilder();
        int i2 = i * 4;
        sb.append(i2);
        sb.append(" %");
        this.tvVolume.setText(sb.toString());
        this.sbVolume.setProgress(i2);
        if (z) {
            this.ivVolume.setImageResource(R.drawable.ic_volume);
        } else {
            this.ivVolume.setImageResource(R.drawable.ic_volume_off);
        }
        this.rlBrightness.setVisibility(View.GONE);
        removeCallbacks(this.runnableVolume);
        postDelayed(this.runnableVolume, 500L);
    }

    public void showBrightness(int i, boolean z) {
        this.rlBrightness.setVisibility(View.VISIBLE);
        this.rlVolume.setVisibility(View.GONE);
        this.sbBrightness.setProgress(i);
        if (z) {
            this.tvBrightness.setText(R.string.auto);
        } else {
            this.tvBrightness.setText(i + " %");
        }
        removeCallbacks(this.runnableBrightness);
        postDelayed(this.runnableBrightness, 500L);
    }

    public void setHighlight(boolean z) {
        this.exoErrorMessage.getBackground().setTint(z ? SupportMenu.CATEGORY_MASK : ContextCompat.getColor(this.mContext, R.color.ui_controls_background));
    }

    public void setScale(float f) {
        View videoSurfaceView = getVideoSurfaceView();
        if (Float.isNaN(f)) {
            return;
        }
        videoSurfaceView.setScaleX(this.isMirror ? -f : f);
        videoSurfaceView.setScaleY(f);
    }

    public boolean isAudioMode() {
        return this.isAudioMode;
    }

    public void setAudioMode(boolean z) {
        this.isAudioMode = z;
        this.layoutPlayAsAudio.setVisibility(z ? 0 : 8);
    }

    public void setLockMode(boolean z) {
        this.isLockMode = z;
    }

    public boolean isPlayVlc() {
        return this.isPlayVlc;
    }

    public void setPlayVlc(boolean z) {
        this.isPlayVlc = z;
        this.vlcVideoLayout.setVisibility(z ? 0 : 8);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (Build.VERSION.SDK_INT >= 29) {
            this.systemGestureExclusionRect.left = i;
            this.systemGestureExclusionRect.right = i3;
            setSystemGestureExclusionRects(Collections.singletonList(this.systemGestureExclusionRect));
        }
    }
}
