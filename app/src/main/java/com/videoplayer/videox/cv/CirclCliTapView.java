package com.videoplayer.videox.cv;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.videoplayer.videox.R;


public final class CirclCliTapView extends View {
    private float arcSize;
    private final Paint backgroundPaint;

    /* renamed from: cX */
    private float f581cX;

    /* renamed from: cY */
    private float f582cY;
    private final Paint circlePaint;
    private float currentRadius;
    private boolean forceReset;
    private int heightPx;
    private boolean isLeft;
    private int maxRadius;
    private int minRadius;
    Runnable performAtEnd;
    private final Path shapePath;
    private ValueAnimator valueAnimator;
    private int widthPx;

    static void lambda$new$0() {
    }

    public CirclCliTapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        this.backgroundPaint = paint;
        Paint paint2 = new Paint();
        this.circlePaint = paint2;
        this.shapePath = new Path();
        this.isLeft = true;
        this.f581cX = 0.0f;
        this.f582cY = 0.0f;
        this.currentRadius = 0.0f;
        this.forceReset = false;
        this.arcSize = 80.0f;
        this.performAtEnd = new Runnable() { // from class: com.videoplayer.videox.cv.CirclCliTapView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CirclCliTapView.lambda$new$0();
            }
        };
        this.widthPx = 0;
        this.heightPx = 0;
        this.minRadius = 0;
        this.maxRadius = 0;
        this.valueAnimator = null;
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(ContextCompat.getColor(context, R.color.dtpv_yt_background_circle_color));
        paint2.setStyle(Paint.Style.FILL);
        paint2.setAntiAlias(true);
        paint2.setColor(ContextCompat.getColor(context, R.color.dtpv_yt_tap_circle_color));
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        this.widthPx = displayMetrics.widthPixels;
        this.heightPx = displayMetrics.heightPixels;
        this.minRadius = (int) (displayMetrics.density * 30.0f);
        this.maxRadius = (int) (displayMetrics.density * 400.0f);
        updatePathShape();
        this.valueAnimator = getCircleAnimator();
    }

    public void setPerformAtEnd(Runnable runnable) {
        this.performAtEnd = runnable;
    }

    public float getArcSize() {
        return this.arcSize;
    }

    public void setArcSize(float f) {
        this.arcSize = f;
        updatePathShape();
    }

    public int getCircleBackgroundColor() {
        return this.backgroundPaint.getColor();
    }

    public void setCircleBackgroundColor(int i) {
        this.backgroundPaint.setColor(i);
    }

    public int getCircleColor() {
        return this.circlePaint.getColor();
    }

    public void setCircleColor(int i) {
        this.circlePaint.setColor(i);
    }

    public long getAnimationDuration() {
        ValueAnimator valueAnimator = this.valueAnimator;
        if (valueAnimator != null) {
            return valueAnimator.getDuration();
        }
        return 650L;
    }

    public void setAnimationDuration(long j) {
        getCircleAnimator().setDuration(j);
    }

    public void updatePosition(float f, float f2) {
        this.f581cX = f;
        this.f582cY = f2;
        boolean z = f <= ((float) (getResources().getDisplayMetrics().widthPixels / 2));
        if (this.isLeft != z) {
            this.isLeft = z;
            updatePathShape();
        }
    }

    public void invalidateWithCurrentRadius(float f) {
        int i = this.minRadius;
        this.currentRadius = i + ((this.maxRadius - i) * f);
        invalidate();
    }

    private void updatePathShape() {
        float f = this.widthPx * 0.5f;
        this.shapePath.reset();
        boolean z = this.isLeft;
        float f2 = z ? 0.0f : this.widthPx;
        int i = z ? 1 : -1;
        this.shapePath.moveTo(f2, 0.0f);
        float f3 = i;
        this.shapePath.lineTo(((f - this.arcSize) * f3) + f2, 0.0f);
        float f4 = this.arcSize;
        float f5 = this.heightPx;
        this.shapePath.quadTo(((f + f4) * f3) + f2, f5 / 2.0f, (f3 * (f - f4)) + f2, f5);
        this.shapePath.lineTo(f2, this.heightPx);
        this.shapePath.close();
        invalidate();
    }

    private ValueAnimator getCircleAnimator() {
        if (this.valueAnimator == null) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.valueAnimator = ofFloat;
            ofFloat.setDuration(getAnimationDuration());
            this.valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.videoplayer.videox.cv.CirclCliTapView$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    CirclCliTapView.this.m605xbac665cf(valueAnimator);
                }
            });
            this.valueAnimator.addListener(new Animator.AnimatorListener() { // from class: com.videoplayer.videox.cv.CirclCliTapView.1
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    CirclCliTapView.this.setVisibility(View.VISIBLE);
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (CirclCliTapView.this.forceReset) {
                        return;
                    }
                    CirclCliTapView.this.performAtEnd.run();
                }
            });
        }
        return this.valueAnimator;
    }

    /* renamed from: lambda$getCircleAnimator$1$com-videoplayer-videox-cv-CirclCliTapView */
    void m605xbac665cf(ValueAnimator valueAnimator) {
        invalidateWithCurrentRadius(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    public void resetAnimation(Runnable runnable) {
        this.forceReset = true;
        getCircleAnimator().end();
        runnable.run();
        this.forceReset = false;
        getCircleAnimator().start();
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.widthPx = i;
        this.heightPx = i2;
        updatePathShape();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas != null) {
            canvas.clipPath(this.shapePath);
        }
        if (canvas != null) {
            canvas.drawPath(this.shapePath, this.backgroundPaint);
        }
        if (canvas != null) {
            canvas.drawCircle(this.f581cX, this.f582cY, this.currentRadius, this.circlePaint);
        }
    }
}
