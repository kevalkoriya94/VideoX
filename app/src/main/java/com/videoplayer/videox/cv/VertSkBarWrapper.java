package com.videoplayer.videox.cv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.core.view.ViewCompat;


public class VertSkBarWrapper extends FrameLayout {
    public VertSkBarWrapper(Context context) {
        this(context, null, 0);
    }

    public VertSkBarWrapper(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public VertSkBarWrapper(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        if (useViewRotation()) {
            onSizeChangedUseViewRotation(i, i2, i3, i4);
        } else {
            onSizeChangedTraditionalRotation(i, i2, i3, i4);
        }
    }

    private void onSizeChangedTraditionalRotation(int i, int i2, int i3, int i4) {
        VertSeekBar childSeekBar = getChildSeekBar();
        if (childSeekBar != null) {
            int paddingLeft = getPaddingLeft() + getPaddingRight();
            int paddingTop = getPaddingTop() + getPaddingBottom();
            LayoutParams layoutParams = (LayoutParams) childSeekBar.getLayoutParams();
            layoutParams.width = -2;
            int i5 = i2 - paddingTop;
            layoutParams.height = Math.max(0, i5);
            childSeekBar.setLayoutParams(layoutParams);
            childSeekBar.measure(0, 0);
            int measuredWidth = childSeekBar.getMeasuredWidth();
            int i6 = i - paddingLeft;
            childSeekBar.measure(MeasureSpec.makeMeasureSpec(Math.max(0, i6), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(Math.max(0, i5), 1073741824));
            layoutParams.gravity = 51;
            layoutParams.leftMargin = (Math.max(0, i6) - measuredWidth) / 2;
            childSeekBar.setLayoutParams(layoutParams);
        }
        super.onSizeChanged(i, i2, i3, i4);
    }

    private void onSizeChangedUseViewRotation(int i, int i2, int i3, int i4) {
        VertSeekBar childSeekBar = getChildSeekBar();
        if (childSeekBar != null) {
            childSeekBar.measure(MeasureSpec.makeMeasureSpec(Math.max(0, i2 - (getPaddingTop() + getPaddingBottom())), 1073741824), MeasureSpec.makeMeasureSpec(Math.max(0, i - (getPaddingLeft() + getPaddingRight())), Integer.MIN_VALUE));
        }
        applyViewRotation(i, i2);
        super.onSizeChanged(i, i2, i3, i4);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int measuredWidth;
        int measuredHeight;
        VertSeekBar childSeekBar = getChildSeekBar();
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        if (childSeekBar == null || mode == 1073741824) {
            super.onMeasure(i, i2);
            return;
        }
        int paddingLeft = getPaddingLeft() + getPaddingRight();
        int paddingTop = getPaddingTop() + getPaddingBottom();
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(0, size - paddingLeft), mode);
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(Math.max(0, size2 - paddingTop), mode2);
        if (useViewRotation()) {
            childSeekBar.measure(makeMeasureSpec2, makeMeasureSpec);
            measuredWidth = childSeekBar.getMeasuredHeight();
            measuredHeight = childSeekBar.getMeasuredWidth();
        } else {
            childSeekBar.measure(makeMeasureSpec, makeMeasureSpec2);
            measuredWidth = childSeekBar.getMeasuredWidth();
            measuredHeight = childSeekBar.getMeasuredHeight();
        }
        setMeasuredDimension(resolveSizeAndState(measuredWidth + paddingLeft, i, 0), resolveSizeAndState(measuredHeight + paddingTop, i2, 0));
    }

    public void applyViewRotation() {
        applyViewRotation(getWidth(), getHeight());
    }

    private void applyViewRotation(int i, int i2) {
        VertSeekBar childSeekBar = getChildSeekBar();
        if (childSeekBar != null) {
            boolean z = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR;
            int rotationAngle = childSeekBar.getRotationAngle();
            int measuredWidth = childSeekBar.getMeasuredWidth();
            int measuredHeight = childSeekBar.getMeasuredHeight();
            float max = (Math.max(0, i - (getPaddingLeft() + getPaddingRight())) - measuredHeight) * 0.5f;
            ViewGroup.LayoutParams layoutParams = childSeekBar.getLayoutParams();
            int paddingTop = i2 - (getPaddingTop() + getPaddingBottom());
            layoutParams.width = Math.max(0, paddingTop);
            layoutParams.height = -2;
            childSeekBar.setLayoutParams(layoutParams);
            childSeekBar.setPivotX(z ? 0.0f : Math.max(0, paddingTop));
            childSeekBar.setPivotY(0.0f);
            if (rotationAngle == 90) {
                childSeekBar.setRotation(90.0f);
                if (z) {
                    childSeekBar.setTranslationX(measuredHeight + max);
                    childSeekBar.setTranslationY(0.0f);
                    return;
                }
                childSeekBar.setTranslationX(-max);
                childSeekBar.setTranslationY(measuredWidth);
            } else if (rotationAngle == 270) {
                childSeekBar.setRotation(270.0f);
                if (z) {
                    childSeekBar.setTranslationX(max);
                    childSeekBar.setTranslationY(measuredWidth);
                    return;
                }
                childSeekBar.setTranslationX(-(measuredHeight + max));
                childSeekBar.setTranslationY(0.0f);
            }
        }
    }

    private VertSeekBar getChildSeekBar() {
        View childAt = getChildCount() > 0 ? getChildAt(0) : null;
        if (childAt instanceof VertSeekBar) {
            return (VertSeekBar) childAt;
        }
        return null;
    }

    private boolean useViewRotation() {
        VertSeekBar childSeekBar = getChildSeekBar();
        if (childSeekBar != null) {
            return childSeekBar.useViewRotation();
        }
        return false;
    }
}
