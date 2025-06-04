package com.videoplayer.videox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.videoplayer.videox.R;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;

import java.util.Objects;


public class VidPlaEndVidDiaBuil {
    final CountDownTimer countDownTimer;
    final Dialog mDialog;
    int time;

    public interface Callback {
        void onNextVideo(VideoInfo videoInfo);

        void onReplayVideo();
    }

    public VidPlaEndVidDiaBuil(final Context context, final VideoInfo videoInfo, final Callback callback) {
        this.time = 5;
        Dialog dialog = new Dialog(context, AppCon.Themes.THEMES_STYLE[new SettingPrefUtils(context).getThemes()]);
        this.mDialog = dialog;
        dialog.requestWindowFeature(1);
        Window window = dialog.getWindow();
        Objects.requireNonNull(window);
        window.setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_video_player_end_video);
        final TextView textView = (TextView) dialog.findViewById(R.id.tv_up_next);
        Glide.with(context).load(videoInfo.getPath()).centerCrop().error(R.drawable.placeholder_video).into((ImageView) dialog.findViewById(R.id.iv_thumbnail));
        ((TextView) dialog.findViewById(R.id.tv_video_name)).setText(videoInfo.getDisplayName());
        ((TextView) dialog.findViewById(R.id.tv_total_time)).setText(Utility.convertLongToDuration(videoInfo.getDuration()));
        dialog.findViewById(R.id.btn_replay).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlaEndVidDiaBuil$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                VidPlaEndVidDiaBuil.new$0(callback, view);
            }
        });
        dialog.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlaEndVidDiaBuil$$ExternalSyntheticLambda1
            @Override 
            public final void onClick(View view) {
                VidPlaEndVidDiaBuil.new$1(callback, videoInfo, view);
            }
        });
        dialog.findViewById(R.id.layout_video).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlaEndVidDiaBuil$$ExternalSyntheticLambda2
            @Override 
            public final void onClick(View view) {
                VidPlaEndVidDiaBuil.new$2(callback, videoInfo, view);
            }
        });
        dialog.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlaEndVidDiaBuil$$ExternalSyntheticLambda3
            @Override 
            public final void onClick(View view) {
                VidPlaEndVidDiaBuil.this.m896lambda$new$3$comvideoplayervideoxdialogVidPlaEndVidDiaBuil(view);
            }
        });
        this.time = 5;
        final SettingPrefUtils settingPrefUtils = new SettingPrefUtils(context);
        CountDownTimer countDownTimer = new CountDownTimer(5500L, 1000L) { // from class: com.videoplayer.videox.dialog.VidPlaEndVidDiaBuil.1
            @Override // android.os.CountDownTimer
            public void onTick(long j) {
                textView.setText(context.getString(R.string.up_next_value, Integer.valueOf(VidPlaEndVidDiaBuil.this.time)));
                VidPlaEndVidDiaBuil.this.time--;
            }

            @Override // android.os.CountDownTimer
            public void onFinish() {
                Callback callback2;
                if (!settingPrefUtils.isAutoPlayNextVideo() || (callback2 = callback) == null) {
                    return;
                }
                callback2.onNextVideo(videoInfo);
            }
        };
        this.countDownTimer = countDownTimer;
        if (settingPrefUtils.isAutoPlayNextVideo()) {
            textView.setText(context.getString(R.string.up_next_value, Integer.valueOf(this.time)));
            countDownTimer.start();
        } else {
            textView.setText(context.getString(R.string.up_next));
        }
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.videoplayer.videox.dialog.VidPlaEndVidDiaBuil$$ExternalSyntheticLambda4
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                VidPlaEndVidDiaBuil.this.m897lambda$new$4$comvideoplayervideoxdialogVidPlaEndVidDiaBuil(dialogInterface);
            }
        });
    }

    /* renamed from: lambda$new$3$com-videoplayer-videox-dialog-VidPlaEndVidDiaBuil, reason: not valid java name */
    void m896lambda$new$3$comvideoplayervideoxdialogVidPlaEndVidDiaBuil(View view) {
        this.mDialog.dismiss();
    }

    /* renamed from: lambda$new$4$com-videoplayer-videox-dialog-VidPlaEndVidDiaBuil, reason: not valid java name */
    void m897lambda$new$4$comvideoplayervideoxdialogVidPlaEndVidDiaBuil(DialogInterface dialogInterface) {
        this.countDownTimer.cancel();
    }

    public static void new$0(Callback callback, View view) {
        if (callback != null) {
            callback.onReplayVideo();
        }
    }

    public static void new$1(Callback callback, VideoInfo videoInfo, View view) {
        if (callback != null) {
            callback.onNextVideo(videoInfo);
        }
    }

    public static void new$2(Callback callback, VideoInfo videoInfo, View view) {
        if (callback != null) {
            callback.onNextVideo(videoInfo);
        }
    }

    public Dialog build() {
        return this.mDialog;
    }

    public boolean isShowing() {
        Dialog dialog = this.mDialog;
        if (dialog == null) {
            return false;
        }
        return dialog.isShowing();
    }

    public void dismiss() {
        CountDownTimer countDownTimer = this.countDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        try {
            Dialog dialog = this.mDialog;
            if (dialog == null || !dialog.isShowing()) {
                return;
            }
            this.mDialog.dismiss();
        } catch (Exception e) {
            Log.d("binhnk08 ", "prevent crash " + e);
        }
    }
}
