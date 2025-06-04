package com.videoplayer.videox.cv;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;

import com.videoplayer.videox.R;


public class DbleTapPlayView extends CustStyPlayView {
    public static final int[] DoubleTapPlayerView = {R.attr.dtpv_controller};
    private int controllerRef;
    private long doubleTapDelay;
    private final GestureDetectorCompat gestureDetector;
    private final DoubleTapGestureListener gestureListener;
    private boolean isDoubleTapEnabled;

    private PlayDbleTapList getController() {
        return this.gestureListener.getControls();
    }

    private void setController(PlayDbleTapList playerDoubleTapListener) {
        this.gestureListener.setControls(playerDoubleTapListener);
    }

    public DbleTapPlayView(Context context) {
        this(context, null);
    }

    public DbleTapPlayView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DbleTapPlayView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.controllerRef = -1;
        DoubleTapGestureListener doubleTapGestureListener = new DoubleTapGestureListener(this);
        this.gestureListener = doubleTapGestureListener;
        this.gestureDetector = new GestureDetectorCompat(context, doubleTapGestureListener);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, DoubleTapPlayerView, 0, 0);
            this.controllerRef = obtainStyledAttributes != null ? obtainStyledAttributes.getResourceId(0, -1) : -1;
            if (obtainStyledAttributes != null) {
                obtainStyledAttributes.recycle();
            }
        }
        this.isDoubleTapEnabled = true;
        this.doubleTapDelay = 700L;
    }

    public final boolean isDoubleTapEnabled() {
        return this.isDoubleTapEnabled;
    }

    public final void setDoubleTapEnabled(boolean z) {
        this.isDoubleTapEnabled = z;
    }

    public final long getDoubleTapDelay() {
        return this.gestureListener.getDoubleTapDelay();
    }

    public final void setDoubleTapDelay(long j) {
        this.gestureListener.setDoubleTapDelay(j);
        this.doubleTapDelay = j;
    }

    public final DbleTapPlayView controller(PlayDbleTapList playerDoubleTapListener) {
        setController(playerDoubleTapListener);
        return this;
    }

    public final boolean isInDoubleTapMode() {
        return this.gestureListener.isDoubleTapping();
    }

    public final void keepInDoubleTapMode() {
        this.gestureListener.keepInDoubleTapMode();
    }

    public final void cancelInDoubleTapMode() {
        this.gestureListener.cancelInDoubleTapMode();
    }

    @Override // com.videoplayer.videox.p012cv.CustStyPlayView, com.google.android.exoplayer2.ui.StyledPlayerView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.isDoubleTapEnabled) {
            return super.onTouchEvent(motionEvent);
        }
        if (this.gestureDetector.onTouchEvent(motionEvent)) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.controllerRef != -1) {
            try {
                KeyEvent.Callback findViewById = ((View) getParent()).findViewById(this.controllerRef);
                if (findViewById instanceof PlayDbleTapList) {
                    controller((PlayDbleTapList) findViewById);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("DoubleTapPlayerView", "controllerRef is either invalid or not PlayerDoubleTapListener: ${e.message}");
            }
        }
    }

    public static final class DoubleTapGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final boolean DEBUG = false;
        private static final String TAG = ".DTGListener";
        private PlayDbleTapList controls;
        private boolean isDoubleTapping;
        private final CustStyPlayView rootView;
        private final Handler mHandler = new Handler();
        private final Runnable mRunnable = new Runnable() { // from class: com.videoplayer.videox.cv.DbleTapPlayView$DoubleTapGestureListener$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                DoubleTapGestureListener.this.m606xacc94266();
            }
        };
        private long doubleTapDelay = 650;

        /* renamed from: lambda$new$0$com-videoplayer-videox-cv-DbleTapPlayView$DoubleTapGestureListener */
        void m606xacc94266() {
            setDoubleTapping(false);
            setDoubleTapping(false);
            if (getControls() != null) {
                getControls().onDoubleTapFinished();
            }
        }

        public boolean isDoubleTapping() {
            return this.isDoubleTapping;
        }

        public void setDoubleTapping(boolean z) {
            this.isDoubleTapping = z;
        }

        public long getDoubleTapDelay() {
            return this.doubleTapDelay;
        }

        public void setDoubleTapDelay(long j) {
            this.doubleTapDelay = j;
        }

        public PlayDbleTapList getControls() {
            return this.controls;
        }

        public void setControls(PlayDbleTapList playerDoubleTapListener) {
            this.controls = playerDoubleTapListener;
        }

        public void keepInDoubleTapMode() {
            this.isDoubleTapping = true;
            this.mHandler.removeCallbacks(this.mRunnable);
            this.mHandler.postDelayed(this.mRunnable, this.doubleTapDelay);
        }

        public void cancelInDoubleTapMode() {
            this.mHandler.removeCallbacks(this.mRunnable);
            this.isDoubleTapping = false;
            PlayDbleTapList playDbleTapList = this.controls;
            if (playDbleTapList != null) {
                playDbleTapList.onDoubleTapFinished();
            }
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            if (!this.isDoubleTapping) {
                return super.onDown(motionEvent);
            }
            PlayDbleTapList playDbleTapList = this.controls;
            if (playDbleTapList == null) {
                return true;
            }
            playDbleTapList.onDoubleTapProgressDown(motionEvent.getX(), motionEvent.getY());
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            if (!this.isDoubleTapping) {
                return super.onSingleTapUp(motionEvent);
            }
            PlayDbleTapList playDbleTapList = this.controls;
            if (playDbleTapList == null) {
                return true;
            }
            playDbleTapList.onDoubleTapProgressUp(motionEvent.getX(), motionEvent.getY());
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            if (this.isDoubleTapping) {
                return true;
            }
            return this.rootView.tap();
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent motionEvent) {
            if (!this.isDoubleTapping) {
                this.isDoubleTapping = true;
                keepInDoubleTapMode();
                PlayDbleTapList playDbleTapList = this.controls;
                if (playDbleTapList != null) {
                    playDbleTapList.onDoubleTapStarted(motionEvent.getX(), motionEvent.getY());
                }
            }
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() != 1 || !this.isDoubleTapping) {
                return super.onDoubleTapEvent(motionEvent);
            }
            PlayDbleTapList playDbleTapList = this.controls;
            if (playDbleTapList != null) {
                playDbleTapList.onDoubleTapProgressUp(motionEvent.getX(), motionEvent.getY());
            }
            return true;
        }

        public DoubleTapGestureListener(CustStyPlayView customStyledPlayerView) {
            this.rootView = customStyledPlayerView;
        }
    }
}
