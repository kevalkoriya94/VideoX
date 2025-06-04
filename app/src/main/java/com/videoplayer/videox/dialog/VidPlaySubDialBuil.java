package com.videoplayer.videox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.videoplayer.videox.R;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.uti.cons.AppCon;

import java.util.Objects;


public class VidPlaySubDialBuil {
    private final Dialog mDialog;
    private final RadioButton rbColor1;
    private final RadioButton rbColor2;
    private final RadioButton rbColor3;
    private final RadioButton rbColor4;
    private final RadioButton rbColor5;
    private final RadioButton rbNone;
    private final RadioButton rbSubtitle;
    private final SeekBar sbSize;
    private final TextView tvSize;

    public interface Callback {
        void onColorSubtitleChanged(int i);

        void onEnableSubtitle(boolean z);

        void onSizeSubtitleChanged(float f);

        void onSubtitleFileSelect();

        void onSubtitleOnlineSelect();
    }

    public VidPlaySubDialBuil(Context context, final Callback callback) {
        Dialog dialog = new Dialog(context, AppCon.Themes.THEMES_STYLE[new SettingPrefUtils(context).getThemes()]);
        this.mDialog = dialog;
        dialog.requestWindowFeature(1);
        Window window = dialog.getWindow();
        Objects.requireNonNull(window);
        window.setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_video_player_subtitle);
        RadioButton radioButton = (RadioButton) dialog.findViewById(R.id.rb_subtitle);
        this.rbSubtitle = radioButton;
        RadioButton radioButton2 = (RadioButton) dialog.findViewById(R.id.rb_none);
        this.rbNone = radioButton2;
        dialog.findViewById(R.id.view_click).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlaySubDialBuil$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                VidPlaySubDialBuil.this.m907lambda$new$0$comvideoplayervideoxdialogVidPlaySubDialBuil(view);
            }
        });
        dialog.findViewById(R.id.tv_select_subtitle).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlaySubDialBuil$$ExternalSyntheticLambda1
            @Override 
            public final void onClick(View view) {
                callback.onSubtitleFileSelect();
            }
        });
        dialog.findViewById(R.id.tv_online_subtitle).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlaySubDialBuil$$ExternalSyntheticLambda2
            @Override 
            public final void onClick(View view) {
                callback.onSubtitleOnlineSelect();
            }
        });
        radioButton.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlaySubDialBuil$$ExternalSyntheticLambda3
            @Override 
            public final void onClick(View view) {
                VidPlaySubDialBuil.this.m908lambda$new$3$comvideoplayervideoxdialogVidPlaySubDialBuil(callback, view);
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlaySubDialBuil$$ExternalSyntheticLambda4
            @Override 
            public final void onClick(View view) {
                VidPlaySubDialBuil.this.m909lambda$new$4$comvideoplayervideoxdialogVidPlaySubDialBuil(callback, view);
            }
        });
        this.tvSize = (TextView) dialog.findViewById(R.id.tv_size);
        ((RadioGroup) dialog.findViewById(R.id.rg_color)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.videoplayer.videox.dialog.VidPlaySubDialBuil$$ExternalSyntheticLambda5
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public final void onCheckedChanged(RadioGroup radioGroup, int i) {
                VidPlaySubDialBuil.new$5(callback, radioGroup, i);
            }
        });
        SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.sb_size_subtitle);
        this.sbSize = seekBar;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.videoplayer.videox.dialog.VidPlaySubDialBuil.1
            int progress = 0;

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar2, int i, boolean z) {
                int i2 = i + 20;
                VidPlaySubDialBuil.this.tvSize.setText(i2 + "px");
                this.progress = i2;
                Callback callback2 = callback;
                if (callback2 != null) {
                    callback2.onSizeSubtitleChanged(i2);
                }
            }
        });
        this.rbColor1 = (RadioButton) dialog.findViewById(R.id.rb_1);
        this.rbColor2 = (RadioButton) dialog.findViewById(R.id.rb_2);
        this.rbColor3 = (RadioButton) dialog.findViewById(R.id.rb_3);
        this.rbColor4 = (RadioButton) dialog.findViewById(R.id.rb_4);
        this.rbColor5 = (RadioButton) dialog.findViewById(R.id.rb_5);
    }

    /* renamed from: lambda$new$0$com-videoplayer-videox-dialog-VidPlaySubDialBuil, reason: not valid java name */
    void m907lambda$new$0$comvideoplayervideoxdialogVidPlaySubDialBuil(View view) {
        this.mDialog.dismiss();
    }

    /* renamed from: lambda$new$3$com-videoplayer-videox-dialog-VidPlaySubDialBuil, reason: not valid java name */
    void m908lambda$new$3$comvideoplayervideoxdialogVidPlaySubDialBuil(Callback callback, View view) {
        if (callback != null) {
            callback.onEnableSubtitle(true);
        }
        this.mDialog.dismiss();
    }

    /* renamed from: lambda$new$4$com-videoplayer-videox-dialog-VidPlaySubDialBuil, reason: not valid java name */
    void m909lambda$new$4$comvideoplayervideoxdialogVidPlaySubDialBuil(Callback callback, View view) {
        if (callback != null) {
            callback.onEnableSubtitle(false);
        }
        this.mDialog.dismiss();
    }

    public static void new$5(Callback callback, RadioGroup radioGroup, int i) {
        int i2;
        if (i == R.id.rb_2) {
            i2 = 2;
        } else if (i == R.id.rb_3) {
            i2 = 3;
        } else if (i == R.id.rb_4) {
            i2 = 4;
        } else {
            i2 = i == R.id.rb_5 ? 5 : 1;
        }
        callback.onColorSubtitleChanged(i2);
    }

    public void setCurrentSubtitle(String str) {
        if (TextUtils.isEmpty(str)) {
            this.rbSubtitle.setEnabled(false);
            this.rbSubtitle.setAlpha(0.4f);
            this.rbSubtitle.setText(R.string.no_subtitle_selected);
            this.rbNone.setChecked(true);
            return;
        }
        this.rbSubtitle.setEnabled(true);
        this.rbSubtitle.setAlpha(1.0f);
        this.rbSubtitle.setText(str);
    }

    public void setEnableSubtitle(boolean z) {
        if (z) {
            this.rbSubtitle.setChecked(true);
        } else {
            this.rbNone.setChecked(true);
        }
    }

    public void setColorSubtitle(int i) {
        if (i == 1) {
            this.rbColor1.setChecked(true);
            return;
        }
        if (i == 2) {
            this.rbColor2.setChecked(true);
            return;
        }
        if (i == 3) {
            this.rbColor3.setChecked(true);
        } else if (i == 4) {
            this.rbColor4.setChecked(true);
        } else if (i == 5) {
            this.rbColor5.setChecked(true);
        }
    }

    public void setSubtitleSize(float f) {
        this.sbSize.setProgress((int) (f - 20.0f));
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
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            return;
        }
        this.mDialog.dismiss();
    }
}
