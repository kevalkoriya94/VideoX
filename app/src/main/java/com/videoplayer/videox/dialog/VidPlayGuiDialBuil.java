package com.videoplayer.videox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.videoplayer.videox.R;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.uti.cons.AppCon;
import com.videoplayer.videox.uti.vid.VidPlayUti;

import java.util.Objects;


public class VidPlayGuiDialBuil {
    private final Dialog mDialog;
    private boolean step1 = true;

    public VidPlayGuiDialBuil(final Context context) {
        Dialog dialog = new Dialog(context, AppCon.Themes.THEMES_STYLE[new SettingPrefUtils(context).getThemes()]);
        this.mDialog = dialog;
        Window window = dialog.getWindow();
        Objects.requireNonNull(window);
        window.requestFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_video_player_guide);
        final TextView textView = (TextView) dialog.findViewById(R.id.tv_action);
        final ImageView imageView = (ImageView) dialog.findViewById(R.id.image_guide);
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlayGuiDialBuil$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                VidPlayGuiDialBuil.this.m903lambda$new$0$comvideoplayervideoxdialogVidPlayGuiDialBuil(imageView, textView, context, view);
            }
        });
    }

    /* renamed from: lambda$new$0$com-videoplayer-videox-dialog-VidPlayGuiDialBuil, reason: not valid java name */
    void m903lambda$new$0$comvideoplayervideoxdialogVidPlayGuiDialBuil(ImageView imageView, TextView textView, Context context, View view) {
        if (this.step1) {
            imageView.setImageResource(R.drawable.image_video_guide_3);
            textView.setText(R.string.got_it);
            this.step1 = false;
        } else {
            VidPlayUti.setShownGuide(context);
            this.mDialog.dismiss();
        }
    }

    public Dialog build() {
        return this.mDialog;
    }
}
