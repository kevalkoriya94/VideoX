package com.videoplayer.videox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.videoplayer.videox.R;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;

import java.util.Objects;


public class VidPlayInfDialBuil {
    private final Dialog mDialog;

    public VidPlayInfDialBuil(Context context, VideoInfo videoInfo) {
        Dialog dialog = new Dialog(context, AppCon.Themes.THEMES_STYLE[new SettingPrefUtils(context).getThemes()]);
        this.mDialog = dialog;
        dialog.requestWindowFeature(1);
        Window window = dialog.getWindow();
        Objects.requireNonNull(window);
        window.setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_video_player_info);
        dialog.findViewById(R.id.view_click).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlayInfDialBuil$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                VidPlayInfDialBuil.this.m904lambda$new$0$comvideoplayervideoxdialogVidPlayInfDialBuil(view);
            }
        });
        ((TextView) dialog.findViewById(R.id.tv_info_name)).setText(videoInfo.getDisplayName());
        ((TextView) dialog.findViewById(R.id.tv_info_size)).setText(Utility.convertSize(videoInfo.getSize()));
        ((TextView) dialog.findViewById(R.id.tv_info_date)).setText(Utility.convertLongToTime(videoInfo.getDate(), "yyyy-MM-dd HH:mm:ss"));
        ((TextView) dialog.findViewById(R.id.tv_info_length)).setText(Utility.convertLongToDuration(videoInfo.getDuration()));
        ((TextView) dialog.findViewById(R.id.tv_info_resolution)).setText(videoInfo.getResolution());
        ((TextView) dialog.findViewById(R.id.tv_info_path)).setText(videoInfo.getPath());
    }

    /* renamed from: lambda$new$0$com-videoplayer-videox-dialog-VidPlayInfDialBuil, reason: not valid java name */
    void m904lambda$new$0$comvideoplayervideoxdialogVidPlayInfDialBuil(View view) {
        this.mDialog.dismiss();
    }

    public Dialog build() {
        return this.mDialog;
    }
}
