package com.videoplayer.videox.cv;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;

import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.videoplayer.videox.R;

import org.videolan.libvlc.MediaPlayer;


public final class YTOver extends ConstraintLayout implements PlayDbleTapList {
    public static final int[] YouTubeOverlay = {R.attr.yt_animationDuration, R.attr.yt_arcSize, R.attr.yt_backgroundCircleColor, R.attr.yt_icon, R.attr.yt_iconAnimationDuration, R.attr.yt_playerView, R.attr.yt_seekSeconds, R.attr.yt_tapCircleColor, R.attr.yt_textAppearance};
    private long animationDuration;
    private float arcSize;
    private final AttributeSet attrs;
    private long iconAnimationDuration;
    private MediaPlayer mediaPlayer;
    private PerformListener performListener;
    private SimpleExoPlayer player;
    private DbleTapPlayView playerView;
    private int playerViewRef;
    private SeekList seekListener;
    private int seekSeconds;
    private int textAppearance;

    public interface PerformListener {
        void onAnimationEnd();

        void onAnimationStart();
    }

    @Override // com.videoplayer.videox.p012cv.PlayDbleTapList
    public void onDoubleTapFinished() {
        C4905CC.$default$onDoubleTapFinished(this);
    }

    @Override // com.videoplayer.videox.p012cv.PlayDbleTapList
    public void onDoubleTapProgressDown(float f, float f2) {
        C4905CC.$default$onDoubleTapProgressDown(this, f, f2);
    }

    public YTOver(Context context) {
        this(context, null);
        setVisibility(View.INVISIBLE);
    }

    public YTOver(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.iconAnimationDuration = 750L;
        this.attrs = attributeSet;
        this.playerViewRef = -1;
        LayoutInflater.from(context).inflate(R.layout.yt_overlay, (ViewGroup) this, true);
        initializeAttributes();
        ((SecView) findViewById(R.id.seconds_view)).setForward(true);
        changeConstraints(true);
        ((CirclCliTapView) findViewById(R.id.circle_clip_tap_view)).setPerformAtEnd(new Runnable() { // from class: com.videoplayer.videox.cv.YTOver$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                YTOver.this.m881lambda$new$0$comvideoplayervideoxcvYTOver();
            }
        });
    }

    /* renamed from: lambda$new$0$com-videoplayer-videox-cv-YTOver, reason: not valid java name */
    void m881lambda$new$0$comvideoplayervideoxcvYTOver() {
        PerformListener performListener = this.performListener;
        if (performListener != null) {
            performListener.onAnimationEnd();
        }
        SecView secView = (SecView) findViewById(R.id.seconds_view);
        secView.setVisibility(View.INVISIBLE);
        secView.setSeconds(0);
        secView.stop();
    }

    private void initializeAttributes() {
        if (this.attrs != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(this.attrs, YouTubeOverlay, 0, 0);
            this.playerViewRef = obtainStyledAttributes.getResourceId(5, -1);
            setAnimationDuration(obtainStyledAttributes.getInt(0, 650));
            this.seekSeconds = obtainStyledAttributes.getInt(6, 10);
            setIconAnimationDuration(obtainStyledAttributes.getInt(4, 750));
            setArcSize(obtainStyledAttributes.getDimensionPixelSize(1, getContext().getResources().getDimensionPixelSize(R.dimen.dtpv_yt_arc_size)));
            setTapCircleColor(obtainStyledAttributes.getColor(7, ContextCompat.getColor(getContext(), R.color.dtpv_yt_tap_circle_color)));
            setCircleBackgroundColor(obtainStyledAttributes.getColor(2, ContextCompat.getColor(getContext(), R.color.dtpv_yt_background_circle_color)));
            setTextAppearance(obtainStyledAttributes.getResourceId(8, R.style.YTOSecondsTextAppearance));
            setIcon(obtainStyledAttributes.getResourceId(3, R.drawable.ic_play_triangle));
            obtainStyledAttributes.recycle();
            return;
        }
        setArcSize(getContext().getResources().getDimensionPixelSize(R.dimen.dtpv_yt_arc_size));
        setTapCircleColor(ContextCompat.getColor(getContext(), R.color.dtpv_yt_tap_circle_color));
        setCircleBackgroundColor(ContextCompat.getColor(getContext(), R.color.dtpv_yt_background_circle_color));
        setAnimationDuration(650L);
        setIconAnimationDuration(750L);
        this.seekSeconds = 10;
        setTextAppearance(R.style.YTOSecondsTextAppearance);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.playerViewRef != -1) {
            playerView((DbleTapPlayView) ((View) getParent()).findViewById(this.playerViewRef));
        }
    }

    public YTOver playerView(DbleTapPlayView doubleTapPlayerView) {
        this.playerView = doubleTapPlayerView;
        return this;
    }

    public YTOver player(SimpleExoPlayer simpleExoPlayer, MediaPlayer mediaPlayer) {
        this.player = simpleExoPlayer;
        this.mediaPlayer = mediaPlayer;
        return this;
    }

    public YTOver seekListener(SeekList seekListener) {
        this.seekListener = seekListener;
        return this;
    }

    public YTOver performListener(PerformListener performListener) {
        this.performListener = performListener;
        return this;
    }

    public int getSeekSeconds() {
        return this.seekSeconds;
    }

    public YTOver seekSeconds(int i) {
        this.seekSeconds = i;
        return this;
    }

    public int getTapCircleColor() {
        return ((CirclCliTapView) findViewById(R.id.circle_clip_tap_view)).getCircleColor();
    }

    private void setTapCircleColor(int i) {
        ((CirclCliTapView) findViewById(R.id.circle_clip_tap_view)).setCircleColor(i);
    }

    public YTOver tapCircleColorRes(int i) {
        setTapCircleColor(ContextCompat.getColor(getContext(), i));
        return this;
    }

    public YTOver tapCircleColorInt(int i) {
        setTapCircleColor(i);
        return this;
    }

    public int getCircleBackgroundColor() {
        return ((CirclCliTapView) findViewById(R.id.circle_clip_tap_view)).getCircleBackgroundColor();
    }

    private void setCircleBackgroundColor(int i) {
        ((CirclCliTapView) findViewById(R.id.circle_clip_tap_view)).setCircleBackgroundColor(i);
    }

    public YTOver circleBackgroundColorRes(int i) {
        setCircleBackgroundColor(ContextCompat.getColor(getContext(), i));
        return this;
    }

    public YTOver circleBackgroundColorInt(int i) {
        setCircleBackgroundColor(i);
        return this;
    }

    public long getAnimationDuration() {
        return ((CirclCliTapView) findViewById(R.id.circle_clip_tap_view)).getAnimationDuration();
    }

    private void setAnimationDuration(long j) {
        ((CirclCliTapView) findViewById(R.id.circle_clip_tap_view)).setAnimationDuration(j);
    }

    public YTOver animationDuration(long j) {
        setAnimationDuration(j);
        return this;
    }

    public float getArcSize() {
        return ((CirclCliTapView) findViewById(R.id.circle_clip_tap_view)).getArcSize();
    }

    private void setArcSize(float f) {
        ((CirclCliTapView) findViewById(R.id.circle_clip_tap_view)).setArcSize(f);
    }

    public YTOver arcSize(int i) {
        setArcSize(getContext().getResources().getDimension(i));
        return this;
    }

    public YTOver arcSize(float f) {
        setArcSize(f);
        return this;
    }

    public long getIconAnimationDuration() {
        return ((SecView) findViewById(R.id.seconds_view)).getCycleDuration();
    }

    private void setIconAnimationDuration(long j) {
        ((SecView) findViewById(R.id.seconds_view)).setCycleDuration(j);
        this.iconAnimationDuration = j;
    }

    public YTOver iconAnimationDuration(long j) {
        setIconAnimationDuration(j);
        return this;
    }

    public int getIcon() {
        return ((SecView) findViewById(R.id.seconds_view)).getIcon();
    }

    private void setIcon(int i) {
        ((SecView) findViewById(R.id.seconds_view)).setIcon(i);
    }

    public YTOver icon(int i) {
        setIcon(i);
        return this;
    }

    public int getTextAppearance() {
        return this.textAppearance;
    }

    private void setTextAppearance(int i) {
        TextViewCompat.setTextAppearance(((SecView) findViewById(R.id.seconds_view)).getTextView(), i);
        this.textAppearance = i;
    }

    public YTOver textAppearance(int i) {
        setTextAppearance(i);
        return this;
    }

    public TextView getSecondsTextView() {
        return ((SecView) findViewById(R.id.seconds_view)).getTextView();
    }

    @Override // com.videoplayer.videox.p012cv.PlayDbleTapList
    public void onDoubleTapStarted(float f, float f2) {
        DbleTapPlayView dbleTapPlayView;
        SimpleExoPlayer simpleExoPlayer = this.player;
        if (simpleExoPlayer == null || simpleExoPlayer.getCurrentPosition() < 0 || (dbleTapPlayView = this.playerView) == null || dbleTapPlayView.getWidth() <= 0) {
            return;
        }
        double d = f;
        if (d < this.playerView.getWidth() * 0.35d || d > this.playerView.getWidth() * 0.65d) {
            return;
        }
        if (this.player.isPlaying()) {
            this.player.pause();
            return;
        }
        this.player.play();
        if (this.playerView.isControllerFullyVisible()) {
            this.playerView.hideController();
        }
    }

    @Override // com.videoplayer.videox.p012cv.PlayDbleTapList
    public void onDoubleTapProgressUp(final float f, final float f2) {
        DbleTapPlayView dbleTapPlayView;
        if (this.player == null || this.mediaPlayer == null || (dbleTapPlayView = this.playerView) == null || dbleTapPlayView.getWidth() < 0) {
            return;
        }
        if (getVisibility() != 0) {
            double d = f;
            if (d >= this.playerView.getWidth() * 0.35d && d <= this.playerView.getWidth() * 0.65d) {
                return;
            }
            PerformListener performListener = this.performListener;
            if (performListener != null) {
                performListener.onAnimationStart();
            }
            SecView secView = (SecView) findViewById(R.id.seconds_view);
            secView.setVisibility(View.VISIBLE);
            secView.start();
        }
        double d2 = f;
        if (d2 < this.playerView.getWidth() * 0.35d) {
            SecView secView2 = (SecView) findViewById(R.id.seconds_view);
            if (secView2.isForward()) {
                changeConstraints(false);
                secView2.setForward(false);
                secView2.setSeconds(0);
            }
            ((CirclCliTapView) findViewById(R.id.circle_clip_tap_view)).resetAnimation(new Runnable() { // from class: com.videoplayer.videox.cv.YTOver$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    YTOver.this.m882lambda$onDoubleTapProgressUp$1$comvideoplayervideoxcvYTOver(f, f2);
                }
            });
            rewinding();
            return;
        }
        if (d2 > this.playerView.getWidth() * 0.65d) {
            SecView secView3 = (SecView) findViewById(R.id.seconds_view);
            if (!secView3.isForward()) {
                changeConstraints(true);
                secView3.setForward(true);
                secView3.setSeconds(0);
            }
            ((CirclCliTapView) findViewById(R.id.circle_clip_tap_view)).resetAnimation(new Runnable() { // from class: com.videoplayer.videox.cv.YTOver$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    YTOver.this.m883lambda$onDoubleTapProgressUp$2$comvideoplayervideoxcvYTOver(f, f2);
                }
            });
            forwarding();
        }
    }

    /* renamed from: lambda$onDoubleTapProgressUp$1$com-videoplayer-videox-cv-YTOver, reason: not valid java name */
    void m882lambda$onDoubleTapProgressUp$1$comvideoplayervideoxcvYTOver(float f, float f2) {
        ((CirclCliTapView) findViewById(R.id.circle_clip_tap_view)).updatePosition(f, f2);
    }

    /* renamed from: lambda$onDoubleTapProgressUp$2$com-videoplayer-videox-cv-YTOver, reason: not valid java name */
    void m883lambda$onDoubleTapProgressUp$2$comvideoplayervideoxcvYTOver(float f, float f2) {
        ((CirclCliTapView) findViewById(R.id.circle_clip_tap_view)).updatePosition(f, f2);
    }

    private void seekToPosition(long j) {
        SimpleExoPlayer simpleExoPlayer = this.player;
        if (simpleExoPlayer == null || this.playerView == null) {
            return;
        }
        simpleExoPlayer.setSeekParameters(SeekParameters.EXACT);
        if (j <= 0) {
            this.player.seekTo(0L);
            SeekList seekList = this.seekListener;
            if (seekList != null) {
                seekList.onVideoStartReached();
                return;
            }
            return;
        }
        long duration = this.player.getDuration();
        if (j >= duration) {
            this.player.seekTo(duration);
            SeekList seekList2 = this.seekListener;
            if (seekList2 != null) {
                seekList2.onVideoEndReached();
                return;
            }
            return;
        }
        this.playerView.keepInDoubleTapMode();
        this.player.seekTo(j);
    }

    private void seekVlcPosition(long j) {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null || this.playerView == null) {
            return;
        }
        if (j <= 0) {
            mediaPlayer.setTime(0L);
            return;
        }
        long length = mediaPlayer.getLength();
        if (j >= length) {
            this.mediaPlayer.setTime(length);
        } else {
            this.mediaPlayer.setTime(j);
        }
    }

    private void forwarding() {
        SecView secView = (SecView) findViewById(R.id.seconds_view);
        secView.setSeconds(secView.getSeconds() + this.seekSeconds);
        SimpleExoPlayer simpleExoPlayer = this.player;
        seekToPosition((simpleExoPlayer != null ? Long.valueOf(simpleExoPlayer.getCurrentPosition() + (this.seekSeconds * 1000)) : null).longValue());
        try {
            MediaPlayer mediaPlayer = this.mediaPlayer;
            seekVlcPosition((mediaPlayer != null ? Long.valueOf(mediaPlayer.getTime() + (this.seekSeconds * 1000)) : null).longValue());
        } catch (IllegalStateException unused) {
        }
    }

    private void rewinding() {
        SecView secView = (SecView) findViewById(R.id.seconds_view);
        secView.setSeconds(secView.getSeconds() + this.seekSeconds);
        SimpleExoPlayer simpleExoPlayer = this.player;
        seekToPosition((simpleExoPlayer != null ? Long.valueOf(simpleExoPlayer.getCurrentPosition() - (this.seekSeconds * 1000)) : null).longValue());
        try {
            MediaPlayer mediaPlayer = this.mediaPlayer;
            seekVlcPosition((mediaPlayer != null ? Long.valueOf(mediaPlayer.getTime() - (this.seekSeconds * 1000)) : null).longValue());
        } catch (IllegalStateException unused) {
        }
    }

    private void changeConstraints(boolean z) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone((ConstraintLayout) findViewById(R.id.root_constraint_layout));
        SecView secView = (SecView) findViewById(R.id.seconds_view);
        if (z) {
            constraintSet.clear(secView.getId(), 6);
            constraintSet.connect(secView.getId(), 7, 0, 7);
        } else {
            constraintSet.clear(secView.getId(), 7);
            constraintSet.connect(secView.getId(), 6, 0, 6);
        }
        secView.start();
        constraintSet.applyTo((ConstraintLayout) findViewById(R.id.root_constraint_layout));
    }
}
