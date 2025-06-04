package com.videoplayer.videox.cv;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Consumer;

import com.videoplayer.videox.R;


public final class SecView extends ConstraintLayout {
    private long cycleDuration;
    private final ValueAnimator fifthAnimator;
    private final ValueAnimator firstAnimator;
    private final ValueAnimator fourthAnimator;
    private int icon;
    private boolean isForward;
    private final ValueAnimator secondAnimator;
    private int seconds;
    private final ValueAnimator thirdAnimator;

    /* renamed from: lambda$new$0$com-videoplayer-videox-cv-SecView, reason: not valid java name */
    void m866lambda$new$0$comvideoplayervideoxcvSecView() {
        findViewById(R.id.icon_1).setAlpha(0.0f);
        findViewById(R.id.icon_2).setAlpha(0.0f);
        findViewById(R.id.icon_3).setAlpha(0.0f);
    }

    /* renamed from: lambda$new$1$com-videoplayer-videox-cv-SecView, reason: not valid java name */
    void m867lambda$new$1$comvideoplayervideoxcvSecView(Float f) {
        findViewById(R.id.icon_1).setAlpha(f.floatValue());
    }

    /* renamed from: lambda$new$2$com-videoplayer-videox-cv-SecView, reason: not valid java name */
    void m873lambda$new$2$comvideoplayervideoxcvSecView() {
        this.secondAnimator.start();
    }

    /* renamed from: lambda$new$3$com-videoplayer-videox-cv-SecView, reason: not valid java name */
    void m874lambda$new$3$comvideoplayervideoxcvSecView() {
        findViewById(R.id.icon_1).setAlpha(1.0f);
        findViewById(R.id.icon_2).setAlpha(0.0f);
        findViewById(R.id.icon_3).setAlpha(0.0f);
    }

    /* renamed from: lambda$new$4$com-videoplayer-videox-cv-SecView, reason: not valid java name */
    void m875lambda$new$4$comvideoplayervideoxcvSecView(Float f) {
        findViewById(R.id.icon_2).setAlpha(f.floatValue());
    }

    /* renamed from: lambda$new$5$com-videoplayer-videox-cv-SecView, reason: not valid java name */
    void m876lambda$new$5$comvideoplayervideoxcvSecView() {
        this.thirdAnimator.start();
    }

    /* renamed from: lambda$new$6$com-videoplayer-videox-cv-SecView, reason: not valid java name */
    void m877lambda$new$6$comvideoplayervideoxcvSecView() {
        findViewById(R.id.icon_1).setAlpha(1.0f);
        findViewById(R.id.icon_2).setAlpha(1.0f);
        findViewById(R.id.icon_3).setAlpha(0.0f);
    }

    /* renamed from: lambda$new$7$com-videoplayer-videox-cv-SecView, reason: not valid java name */
    void m878lambda$new$7$comvideoplayervideoxcvSecView(Float f) {
        findViewById(R.id.icon_1).setAlpha(1.0f - findViewById(R.id.icon_3).getAlpha());
        findViewById(R.id.icon_3).setAlpha(f.floatValue());
    }

    /* renamed from: lambda$new$8$com-videoplayer-videox-cv-SecView, reason: not valid java name */
    void m879lambda$new$8$comvideoplayervideoxcvSecView() {
        this.fourthAnimator.start();
    }

    /* renamed from: lambda$new$9$com-videoplayer-videox-cv-SecView, reason: not valid java name */
    void m880lambda$new$9$comvideoplayervideoxcvSecView() {
        findViewById(R.id.icon_1).setAlpha(0.0f);
        findViewById(R.id.icon_2).setAlpha(1.0f);
        findViewById(R.id.icon_3).setAlpha(1.0f);
    }

    /* renamed from: lambda$new$10$com-videoplayer-videox-cv-SecView, reason: not valid java name */
    void m868lambda$new$10$comvideoplayervideoxcvSecView(Float f) {
        findViewById(R.id.icon_2).setAlpha(1.0f - f.floatValue());
    }

    /* renamed from: lambda$new$11$com-videoplayer-videox-cv-SecView, reason: not valid java name */
    void m869lambda$new$11$comvideoplayervideoxcvSecView() {
        this.fifthAnimator.start();
    }

    /* renamed from: lambda$new$12$com-videoplayer-videox-cv-SecView, reason: not valid java name */
    void m870lambda$new$12$comvideoplayervideoxcvSecView() {
        findViewById(R.id.icon_1).setAlpha(0.0f);
        findViewById(R.id.icon_2).setAlpha(0.0f);
        findViewById(R.id.icon_3).setAlpha(1.0f);
    }

    /* renamed from: lambda$new$13$com-videoplayer-videox-cv-SecView, reason: not valid java name */
    void m871lambda$new$13$comvideoplayervideoxcvSecView(Float f) {
        findViewById(R.id.icon_3).setAlpha(1.0f - f.floatValue());
    }

    /* renamed from: lambda$new$14$com-videoplayer-videox-cv-SecView, reason: not valid java name */
    void m872lambda$new$14$comvideoplayervideoxcvSecView() {
        this.firstAnimator.start();
    }

    public SecView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.cycleDuration = 750L;
        this.seconds = 0;
        this.isForward = true;
        this.icon = R.drawable.ic_play_triangle;
        this.firstAnimator = new CustomValueAnimator(new Runnable() { // from class: com.videoplayer.videox.cv.SecView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SecView.this.m866lambda$new$0$comvideoplayervideoxcvSecView();
            }
        }, new Consumer() { // from class: com.videoplayer.videox.cv.SecView$$ExternalSyntheticLambda11
            @Override // androidx.core.util.Consumer
            public final void accept(Object obj) {
                SecView.this.m867lambda$new$1$comvideoplayervideoxcvSecView((Float) obj);
            }
        }, new Runnable() { // from class: com.videoplayer.videox.cv.SecView$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                SecView.this.m873lambda$new$2$comvideoplayervideoxcvSecView();
            }
        });
        this.secondAnimator = new CustomValueAnimator(new Runnable() { // from class: com.videoplayer.videox.cv.SecView$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                SecView.this.m874lambda$new$3$comvideoplayervideoxcvSecView();
            }
        }, new Consumer() { // from class: com.videoplayer.videox.cv.SecView$$ExternalSyntheticLambda14
            @Override // androidx.core.util.Consumer
            public final void accept(Object obj) {
                SecView.this.m875lambda$new$4$comvideoplayervideoxcvSecView((Float) obj);
            }
        }, new Runnable() { // from class: com.videoplayer.videox.cv.SecView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                SecView.this.m876lambda$new$5$comvideoplayervideoxcvSecView();
            }
        });
        this.thirdAnimator = new CustomValueAnimator(new Runnable() { // from class: com.videoplayer.videox.cv.SecView$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                SecView.this.m877lambda$new$6$comvideoplayervideoxcvSecView();
            }
        }, new Consumer() { // from class: com.videoplayer.videox.cv.SecView$$ExternalSyntheticLambda3
            @Override // androidx.core.util.Consumer
            public final void accept(Object obj) {
                SecView.this.m878lambda$new$7$comvideoplayervideoxcvSecView((Float) obj);
            }
        }, new Runnable() { // from class: com.videoplayer.videox.cv.SecView$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                SecView.this.m879lambda$new$8$comvideoplayervideoxcvSecView();
            }
        });
        this.fourthAnimator = new CustomValueAnimator(new Runnable() { // from class: com.videoplayer.videox.cv.SecView$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                SecView.this.m880lambda$new$9$comvideoplayervideoxcvSecView();
            }
        }, new Consumer() { // from class: com.videoplayer.videox.cv.SecView$$ExternalSyntheticLambda6
            @Override // androidx.core.util.Consumer
            public final void accept(Object obj) {
                SecView.this.m868lambda$new$10$comvideoplayervideoxcvSecView((Float) obj);
            }
        }, new Runnable() { // from class: com.videoplayer.videox.cv.SecView$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                SecView.this.m869lambda$new$11$comvideoplayervideoxcvSecView();
            }
        });
        this.fifthAnimator = new CustomValueAnimator(new Runnable() { // from class: com.videoplayer.videox.cv.SecView$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                SecView.this.m870lambda$new$12$comvideoplayervideoxcvSecView();
            }
        }, new Consumer() { // from class: com.videoplayer.videox.cv.SecView$$ExternalSyntheticLambda9
            @Override // androidx.core.util.Consumer
            public final void accept(Object obj) {
                SecView.this.m871lambda$new$13$comvideoplayervideoxcvSecView((Float) obj);
            }
        }, new Runnable() { // from class: com.videoplayer.videox.cv.SecView$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                SecView.this.m872lambda$new$14$comvideoplayervideoxcvSecView();
            }
        });
        LayoutInflater.from(context).inflate(R.layout.yt_seconds_view, (ViewGroup) this, true);
    }

    public long getCycleDuration() {
        return this.cycleDuration;
    }

    public void setCycleDuration(long j) {
        long j2 = j / 5;
        this.firstAnimator.setDuration(j2);
        this.secondAnimator.setDuration(j2);
        this.thirdAnimator.setDuration(j2);
        this.fourthAnimator.setDuration(j2);
        this.fifthAnimator.setDuration(j2);
        this.cycleDuration = j;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public void setSeconds(int i) {
        ((TextView) findViewById(R.id.tv_seconds)).setText(getContext().getResources().getQuantityString(R.plurals.quick_seek_x_second, i, Integer.valueOf(i)));
        this.seconds = i;
    }

    public boolean isForward() {
        return this.isForward;
    }

    public void setForward(boolean z) {
        ((LinearLayout) findViewById(R.id.triangle_container)).setRotation(z ? 0.0f : 180.0f);
        this.isForward = z;
    }

    public TextView getTextView() {
        return (TextView) findViewById(R.id.tv_seconds);
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int i) {
        if (i > 0) {
            ((ImageView) findViewById(R.id.icon_1)).setImageResource(i);
            ((ImageView) findViewById(R.id.icon_2)).setImageResource(i);
            ((ImageView) findViewById(R.id.icon_3)).setImageResource(i);
        }
        this.icon = i;
    }

    public void start() {
        stop();
        this.firstAnimator.start();
    }

    public void stop() {
        this.firstAnimator.cancel();
        this.secondAnimator.cancel();
        this.thirdAnimator.cancel();
        this.fourthAnimator.cancel();
        this.fifthAnimator.cancel();
        reset();
    }

    private void reset() {
        findViewById(R.id.icon_1).setAlpha(0.0f);
        findViewById(R.id.icon_2).setAlpha(0.0f);
        findViewById(R.id.icon_3).setAlpha(0.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class CustomValueAnimator extends ValueAnimator {
        public CustomValueAnimator(final Runnable runnable, final Consumer<Float> consumer, final Runnable runnable2) {
            setDuration(SecView.this.getCycleDuration() / 5);
            setFloatValues(0.0f, 1.0f);
            addUpdateListener(new AnimatorUpdateListener() { // from class: com.videoplayer.videox.cv.SecView$CustomValueAnimator$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    consumer.accept((Float) valueAnimator.getAnimatedValue());
                }
            });
            addListener(new AnimatorListener() { // from class: com.videoplayer.videox.cv.SecView.CustomValueAnimator.1
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    runnable.run();
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    runnable2.run();
                }
            });
        }
    }
}
