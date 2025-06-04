package com.videoplayer.videox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.videoplayer.videox.R;


public class SorDialBuil {
    private final Dialog mDialog;
    private final RadioGroup rgSort;
    private final RadioGroup rgUnit;

    public interface OkButtonClickListener {
        void onClick(int i, boolean z);
    }

    public SorDialBuil(Context context, final OkButtonClickListener okButtonClickListener, int i, boolean z) {
        Dialog dialog = new Dialog(context, R.style.CustomDialog);
        this.mDialog = dialog;
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_sort);
        RadioButton radioButton = (RadioButton) dialog.findViewById(R.id.rb_name);
        RadioButton radioButton2 = (RadioButton) dialog.findViewById(R.id.rb_size);
        RadioButton radioButton3 = (RadioButton) dialog.findViewById(R.id.rb_date);
        RadioButton radioButton4 = (RadioButton) dialog.findViewById(R.id.rb_length);
        this.rgUnit = (RadioGroup) dialog.findViewById(R.id.rg_unit);
        this.rgSort = (RadioGroup) dialog.findViewById(R.id.rg_sort);
        RadioButton radioButton5 = (RadioButton) dialog.findViewById(R.id.rb_ascending);
        RadioButton radioButton6 = (RadioButton) dialog.findViewById(R.id.rb_descending);
        if (i == 0) {
            radioButton3.setChecked(true);
        } else if (i == 1) {
            radioButton.setChecked(true);
        } else if (i == 2) {
            radioButton2.setChecked(true);
        } else if (i == 3) {
            radioButton4.setChecked(true);
        }
        if (z) {
            radioButton5.setChecked(true);
        } else {
            radioButton6.setChecked(true);
        }
        dialog.findViewById(R.id.tv_dialog_cancel).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.SorDialBuil$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                SorDialBuil.this.m894lambda$new$0$comvideoplayervideoxdialogSorDialBuil(view);
            }
        });
        dialog.findViewById(R.id.tv_dialog_ok).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.SorDialBuil$$ExternalSyntheticLambda1
            @Override 
            public final void onClick(View view) {
                SorDialBuil.this.m895lambda$new$1$comvideoplayervideoxdialogSorDialBuil(okButtonClickListener, view);
            }
        });
    }

    /* renamed from: lambda$new$0$com-videoplayer-videox-dialog-SorDialBuil, reason: not valid java name */
    void m894lambda$new$0$comvideoplayervideoxdialogSorDialBuil(View view) {
        this.mDialog.dismiss();
    }

    /* renamed from: newSSortDialogBuilder, reason: merged with bridge method [inline-methods] */
    public void m895lambda$new$1$comvideoplayervideoxdialogSorDialBuil(OkButtonClickListener okButtonClickListener, View view) {
        int i;
        this.mDialog.dismiss();
        int checkedRadioButtonId = this.rgUnit.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.rb_length) {
            i = 3;
        } else if (checkedRadioButtonId == R.id.rb_name) {
            i = 1;
        } else {
            i = checkedRadioButtonId == R.id.rb_size ? 2 : 0;
        }
        okButtonClickListener.onClick(i, this.rgSort.getCheckedRadioButtonId() == R.id.rb_ascending);
    }

    public Dialog build() {
        return this.mDialog;
    }
}
