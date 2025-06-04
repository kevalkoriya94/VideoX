package com.videoplayer.videox.dialog;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.videoplayer.videox.R;


public class SetVidModDiaBuil {
    final BottomSheetDialog mDialog;

    public interface OkVideoModeListener {
        void onClick(int i);
    }

    public SetVidModDiaBuil(Context context, final int i, final OkVideoModeListener okVideoModeListener) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.CustomDialog);
        this.mDialog = bottomSheetDialog;
        bottomSheetDialog.requestWindowFeature(1);
        bottomSheetDialog.setContentView(R.layout.dialog_setting_video_mode);
        RadioButton radioButton = (RadioButton) bottomSheetDialog.findViewById(R.id.rb_sensor);
        RadioButton radioButton2 = (RadioButton) bottomSheetDialog.findViewById(R.id.rb_portrait);
        RadioButton radioButton3 = (RadioButton) bottomSheetDialog.findViewById(R.id.rb_landscape);
        RadioGroup radioGroup = (RadioGroup) bottomSheetDialog.findViewById(R.id.rg_video_mode);
        if (i == 1) {
            radioButton.setChecked(true);
        } else if (i == 2) {
            radioButton2.setChecked(true);
        } else if (i == 3) {
            radioButton3.setChecked(true);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.videoplayer.videox.dialog.SetVidModDiaBuil$$ExternalSyntheticLambda0
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public final void onCheckedChanged(RadioGroup radioGroup2, int i2) {
                SetVidModDiaBuil.this.m893lambda$new$0$comvideoplayervideoxdialogSetVidModDiaBuil(i, okVideoModeListener, radioGroup2, i2);
            }
        });
    }

    /* renamed from: lambda$new$0$com-videoplayer-videox-dialog-SetVidModDiaBuil, reason: not valid java name */
    void m893lambda$new$0$comvideoplayervideoxdialogSetVidModDiaBuil(int i, OkVideoModeListener okVideoModeListener, RadioGroup radioGroup, int i2) {
        if (i == R.id.rb_landscape) {
            okVideoModeListener.onClick(3);
        } else if (i == R.id.rb_portrait) {
            okVideoModeListener.onClick(2);
        } else if (i == R.id.rb_sensor) {
            okVideoModeListener.onClick(1);
        }
        this.mDialog.dismiss();
    }

    public BottomSheetDialog build() {
        return this.mDialog;
    }
}
