package com.videoplayer.videox.uti;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.videoplayer.videox.R;


public class CircleSeekBar extends View {
    private static final int ANGLE_OFFSET = -90;
    private static final float INVALID_VALUE = -1.0f;
    public static final int MAX = 100;
    public static final int MIN = 0;
    private static final int TEXT_SIZE_DEFAULT = 72;
    private boolean isMax;
    private boolean isMin;
    private double mAngle;
    private Paint mArcPaint;
    private RectF mArcRect;
    private int mArcWidth;
    private int mCenterX;
    private int mCenterY;
    private int mCircleRadius;
    private float mCurrentProgress;
    private boolean mIsShowText;
    private boolean mIsThumbSelected;
    private int mMax;
    private int mMin;
    private OnSeekBarChangedListener mOnSeekBarChangeListener;
    private int mPadding;
    private float mPreviousProgress;
    private int mProgressDisplay;
    private Paint mProgressPaint;
    private float mProgressSweep;
    private int mProgressWidth;
    private int mStep;
    private Paint mTextPaint;
    private Rect mTextRect;
    private int mTextSize;
    Drawable mThumbDrawable;
    private int mThumbSize;
    private int mThumbX;
    private int mThumbY;
    private int mUpdateTimes;

    public interface OnSeekBarChangedListener {
        void onPointsChanged(CircleSeekBar circleSeekBar, int points, boolean fromUser);

        void onStartTrackingTouch(CircleSeekBar circleSeekBar);

        void onStopTrackingTouch(CircleSeekBar circleSeekBar);
    }

    public CircleSeekBar(Context context) {
        super(context);
        this.mProgressDisplay = 0;
        this.mMin = 0;
        this.mMax = 100;
        this.mStep = 1;
        this.mArcWidth = 8;
        this.mProgressWidth = 12;
        this.mUpdateTimes = 0;
        this.mPreviousProgress = -1.0f;
        this.mCurrentProgress = 0.0f;
        this.isMax = false;
        this.isMin = false;
        this.mArcRect = new RectF();
        this.mTextSize = TEXT_SIZE_DEFAULT;
        this.mTextRect = new Rect();
        this.mIsShowText = true;
        this.mIsThumbSelected = false;
        init(context, null);
    }

    public CircleSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mProgressDisplay = 0;
        this.mMin = 0;
        this.mMax = 100;
        this.mStep = 1;
        this.mArcWidth = 8;
        this.mProgressWidth = 12;
        this.mUpdateTimes = 0;
        this.mPreviousProgress = -1.0f;
        this.mCurrentProgress = 0.0f;
        this.isMax = false;
        this.isMin = false;
        this.mArcRect = new RectF();
        this.mTextSize = TEXT_SIZE_DEFAULT;
        this.mTextRect = new Rect();
        this.mIsShowText = true;
        this.mIsThumbSelected = false;
        init(context, attrs);
    }

    public void setStep(int mStep) {
        this.mStep = mStep;
    }

    public void setThumbDrawable(Drawable mIndicatorIcon) {
        this.mThumbDrawable = mIndicatorIcon;
    }

    public void setArcWidth(int mArcWidth) {
        this.mArcWidth = mArcWidth;
    }

    public void setProgressWidth(int mProgressWidth) {
        this.mProgressWidth = mProgressWidth;
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }

    public void setIsShowText(boolean mIsShowText) {
        this.mIsShowText = mIsShowText;
    }

    public void setProgressDisplay(int progressDisplay) {
        this.mProgressDisplay = progressDisplay;
        int i = this.mMax;
        if (progressDisplay > i) {
            progressDisplay = i;
        }
        this.mProgressDisplay = progressDisplay;
        int i2 = this.mMin;
        if (progressDisplay < i2) {
            progressDisplay = i2;
        }
        this.mProgressDisplay = progressDisplay;
        float valuePerDegree = progressDisplay / valuePerDegree();
        this.mProgressSweep = valuePerDegree;
        this.mAngle = 1.5707963267948966d - ((valuePerDegree * 3.141592653589793d) / 180.0d);
    }

    public void setProgressDisplayAndInvalidate(int progressDisplay) {
        setProgressDisplay(progressDisplay);
        OnSeekBarChangedListener onSeekBarChangedListener = this.mOnSeekBarChangeListener;
        if (onSeekBarChangedListener != null) {
            onSeekBarChangedListener.onPointsChanged(this, this.mProgressDisplay, false);
        }
        invalidate();
    }

    public int getProgressDisplay() {
        return this.mProgressDisplay;
    }

    public int getMin() {
        return this.mMin;
    }

    public int getMax() {
        return this.mMax;
    }

    public int getStep() {
        return this.mStep;
    }

    public float getCurrentProgress() {
        return this.mCurrentProgress;
    }

    public double getAngle() {
        return this.mAngle;
    }

    public static int[] CircleSeekBar = {R.attr.csb_arcColor, R.attr.csb_arcWidth, R.attr.csb_isShowText, R.attr.csb_max, R.attr.csb_min, R.attr.csb_progress, R.attr.csb_progressColor, R.attr.csb_progressWidth, R.attr.csb_step, R.attr.csb_textColor, R.attr.csb_textSize, R.attr.csb_thumbDrawable, R.attr.csb_thumbSize};
    private void init(Context context, AttributeSet attrs) {
        float f = context.getResources().getDisplayMetrics().density;
        int color = ContextCompat.getColor(context, R.color.color_progress);
        int color2 = ContextCompat.getColor(context, R.color.color_arc);
        int color3 = ContextCompat.getColor(context, R.color.color_text);
        this.mProgressWidth = (int) (this.mProgressWidth * f);
        this.mArcWidth = (int) (this.mArcWidth * f);
        this.mTextSize = (int) (f * this.mTextSize);
        this.mThumbDrawable = ContextCompat.getDrawable(context, R.drawable.thumb);
        if (attrs != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, CircleSeekBar, 0, 0);
            Drawable drawable = obtainStyledAttributes.getDrawable(11);
            if (drawable != null) {
                this.mThumbDrawable = drawable;
            }
            this.mProgressDisplay = obtainStyledAttributes.getInteger(5, this.mProgressDisplay);
            this.mThumbSize = obtainStyledAttributes.getDimensionPixelSize(12, 50);
            this.mMin = obtainStyledAttributes.getInteger(4, this.mMin);
            this.mMax = obtainStyledAttributes.getInteger(3, this.mMax);
            this.mStep = obtainStyledAttributes.getInteger(8, this.mStep);
            this.mTextSize = (int) obtainStyledAttributes.getDimension(10, this.mTextSize);
            color3 = obtainStyledAttributes.getColor(9, color3);
            this.mIsShowText = obtainStyledAttributes.getBoolean(2, this.mIsShowText);
            this.mProgressWidth = (int) obtainStyledAttributes.getDimension(7, this.mProgressWidth);
            color = obtainStyledAttributes.getColor(6, color);
            this.mArcWidth = (int) obtainStyledAttributes.getDimension(1, this.mArcWidth);
            color2 = obtainStyledAttributes.getColor(0, color2);
            this.mPadding = (((((getPaddingLeft() + getPaddingRight()) + getPaddingBottom()) + getPaddingTop()) + getPaddingEnd()) + getPaddingStart()) / 6;
            obtainStyledAttributes.recycle();
        }
        int i = this.mProgressDisplay;
        int i2 = this.mMax;
        if (i > i2) {
            i = i2;
        }
        this.mProgressDisplay = i;
        int i3 = this.mMin;
        if (i < i3) {
            i = i3;
        }
        this.mProgressDisplay = i;
        float valuePerDegree = i / valuePerDegree();
        this.mProgressSweep = valuePerDegree;
        this.mAngle = 1.5707963267948966d - ((valuePerDegree * 3.141592653589793d) / 180.0d);
        this.mCurrentProgress = Math.round(valuePerDegree * valuePerDegree());
        Paint paint = new Paint();
        this.mArcPaint = paint;
        paint.setColor(color2);
        this.mArcPaint.setAntiAlias(true);
        this.mArcPaint.setStyle(Paint.Style.STROKE);
        this.mArcPaint.setStrokeWidth(this.mArcWidth);
        Paint paint2 = new Paint();
        this.mProgressPaint = paint2;
        paint2.setColor(color);
        this.mProgressPaint.setAntiAlias(true);
        this.mProgressPaint.setStyle(Paint.Style.STROKE);
        this.mProgressPaint.setStrokeWidth(this.mProgressWidth);
        Paint paint3 = new Paint();
        this.mTextPaint = paint3;
        paint3.setColor(color3);
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setStyle(Paint.Style.FILL);
        this.mTextPaint.setTextSize(this.mTextSize);
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int min = Math.min(w, h);
        int i = ((w - min) / 2) + min;
        int i2 = ((h - min) / 2) + min;
        this.mCenterX = (i / 2) + ((w - i) / 2);
        this.mCenterY = (i2 / 2) + ((h - i2) / 2);
        float f = min - this.mPadding;
        float f2 = f / 2.0f;
        this.mCircleRadius = (int) f2;
        float f3 = (h / 2) - f2;
        float f4 = (w / 2) - f2;
        this.mArcRect.set(f4, f3, f4 + f, f + f3);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mIsShowText) {
            String valueOf = String.valueOf(this.mProgressDisplay);
            this.mTextPaint.getTextBounds(valueOf, 0, valueOf.length(), this.mTextRect);
            canvas.drawText(String.valueOf(this.mProgressDisplay), (canvas.getWidth() / 2) - (this.mTextRect.width() / 2), (int) (this.mArcRect.centerY() - ((this.mTextPaint.descent() + this.mTextPaint.ascent()) / 2.0f)), this.mTextPaint);
        }
        canvas.drawCircle(this.mCenterX, this.mCenterY, this.mCircleRadius, this.mArcPaint);
        canvas.drawArc(this.mArcRect, -90.0f, this.mProgressSweep, false, this.mProgressPaint);
        this.mThumbX = (int) (this.mCenterX + (this.mCircleRadius * Math.cos(this.mAngle)));
        int sin = (int) (this.mCenterY - (this.mCircleRadius * Math.sin(this.mAngle)));
        this.mThumbY = sin;
        Drawable drawable = this.mThumbDrawable;
        int i = this.mThumbX;
        int i2 = this.mThumbSize;
        drawable.setBounds(i - (i2 / 2), sin - (i2 / 2), i + (i2 / 2), sin + (i2 / 2));
        this.mThumbDrawable.draw(canvas);
    }

    private float valuePerDegree() {
        return this.mMax / 360.0f;
    }

    private void updateProgressState(int touchX, int touchY) {
        int i = touchX - this.mCenterX;
        int i2 = this.mCenterY - touchY;
        double d = i;
        double acos = Math.acos(d / Math.sqrt(Math.pow(d, 2.0d) + Math.pow(i2, 2.0d)));
        this.mAngle = acos;
        if (i2 < 0) {
            this.mAngle = -acos;
        }
        float f = (float) (90.0d - ((this.mAngle * 180.0d) / 3.141592653589793d));
        this.mProgressSweep = f;
        if (f < 0.0f) {
            this.mProgressSweep = f + 360.0f;
        }
        updateProgress(Math.round(this.mProgressSweep * valuePerDegree()), true);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == 0) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            int i = this.mThumbX;
            int i2 = this.mThumbSize;
            if (x < i + i2 && x > i - i2) {
                int i3 = this.mThumbY;
                if (y < i3 + i2 && y > i3 - i2) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    this.mIsThumbSelected = true;
                    updateProgressState(x, y);
                    OnSeekBarChangedListener onSeekBarChangedListener = this.mOnSeekBarChangeListener;
                    if (onSeekBarChangedListener != null) {
                        onSeekBarChangedListener.onStartTrackingTouch(this);
                    }
                }
            }
        } else if (action != 1) {
            if (action == 2 && this.mIsThumbSelected) {
                updateProgressState((int) event.getX(), (int) event.getY());
            }
        } else {
            getParent().requestDisallowInterceptTouchEvent(false);
            this.mIsThumbSelected = false;
            OnSeekBarChangedListener onSeekBarChangedListener2 = this.mOnSeekBarChangeListener;
            if (onSeekBarChangedListener2 != null) {
                onSeekBarChangedListener2.onStopTrackingTouch(this);
            }
        }
        return true;
    }

    private void updateProgress(int progress, boolean fromUser) {
        int i = this.mMax;
        int i2 = (int) (i * 0.99d);
        int i3 = this.mMin;
        int i4 = ((int) (i * 0.005d)) + i3;
        int i5 = this.mUpdateTimes + 1;
        this.mUpdateTimes = i5;
        float f = progress;
        if (f == -1.0f) {
            return;
        }
        if (progress <= i2 || this.mPreviousProgress != -1.0f) {
            if (i5 == 1) {
                this.mCurrentProgress = f;
            } else {
                this.mPreviousProgress = this.mCurrentProgress;
                this.mCurrentProgress = f;
            }
            int i6 = progress - (progress % this.mStep);
            this.mProgressDisplay = i6;
            if (i5 > 1 && !this.isMin && !this.isMax) {
                float f2 = this.mPreviousProgress;
                float f3 = i2;
                if (f2 >= f3) {
                    float f4 = this.mCurrentProgress;
                    if (f4 <= i4 && f2 > f4) {
                        this.isMax = true;
                        this.mProgressDisplay = i;
                        this.mProgressSweep = 360.0f;
                        OnSeekBarChangedListener onSeekBarChangedListener = this.mOnSeekBarChangeListener;
                        if (onSeekBarChangedListener != null) {
                            onSeekBarChangedListener.onPointsChanged(this, i, fromUser);
                        }
                        invalidate();
                        progress = i;
                    }
                }
                float f5 = this.mCurrentProgress;
                if ((f5 >= f3 && f2 <= i4 && f5 > f2) || f5 <= i3) {
                    this.isMin = true;
                    this.mProgressDisplay = i3;
                    this.mProgressSweep = i3 / valuePerDegree();
                    OnSeekBarChangedListener onSeekBarChangedListener2 = this.mOnSeekBarChangeListener;
                    if (onSeekBarChangedListener2 != null) {
                        onSeekBarChangedListener2.onPointsChanged(this, i3, fromUser);
                    }
                    invalidate();
                    progress = i3;
                }
            } else {
                boolean z = this.isMax;
                float f6 = this.mCurrentProgress;
                float f7 = this.mPreviousProgress;
                if ((z & (f6 < f7)) && f6 >= i2) {
                    this.isMax = false;
                }
                if (this.isMin && f7 < f6) {
                    float f8 = i4;
                    if (f7 <= f8 && f6 <= f8 && i6 >= i3) {
                        this.isMin = false;
                    }
                }
            }
            if (this.isMax || this.isMin) {
                return;
            }
            int i7 = this.mMax;
            if (progress > i7) {
                progress = i7;
            }
            int i8 = this.mMin;
            if (progress < i8) {
                progress = i8;
            }
            OnSeekBarChangedListener onSeekBarChangedListener3 = this.mOnSeekBarChangeListener;
            if (onSeekBarChangedListener3 != null) {
                onSeekBarChangedListener3.onPointsChanged(this, progress - (progress % this.mStep), fromUser);
            }
            invalidate();
        }
    }

    public void setSeekBarChangeListener(OnSeekBarChangedListener seekBarChangeListener) {
        this.mOnSeekBarChangeListener = seekBarChangeListener;
    }
}
