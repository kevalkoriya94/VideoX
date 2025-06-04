package com.videoplayer.videox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.videoplayer.videox.R;


public class InpDialBui {
    private final Dialog mDialog;

    public interface OkButtonClickListener {
        void onClick(String str);
    }

    public InpDialBui(Context context, final OkButtonClickListener okButtonClickListener, String str) {
        Dialog dialog = new Dialog(context, R.style.CustomDialog);
        this.mDialog = dialog;
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_input_text);
        dialog.getWindow().setSoftInputMode(4);
        final EditText editText = (EditText) dialog.findViewById(R.id.edt_input_text);
        if (!TextUtils.isEmpty(str)) {
            editText.setText(str);
        }
        dialog.findViewById(R.id.tv_dialog_cancel).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.InpDialBui$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                InpDialBui.this.m886lambda$new$0$comvideoplayervideoxdialogInpDialBui(view);
            }
        });
        dialog.findViewById(R.id.tv_dialog_ok).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.InpDialBui$$ExternalSyntheticLambda1
            @Override 
            public final void onClick(View view) {
                InpDialBui.this.m887lambda$new$1$comvideoplayervideoxdialogInpDialBui(okButtonClickListener, editText, view);
            }
        });
    }

    /* renamed from: lambda$new$0$com-videoplayer-videox-dialog-InpDialBui, reason: not valid java name */
    void m886lambda$new$0$comvideoplayervideoxdialogInpDialBui(View view) {
        this.mDialog.dismiss();
    }

    /* renamed from: lambda$new$1$com-videoplayer-videox-dialog-InpDialBui, reason: not valid java name */
    void m887lambda$new$1$comvideoplayervideoxdialogInpDialBui(OkButtonClickListener okButtonClickListener, EditText editText, View view) {
        okButtonClickListener.onClick(editText.getText().toString());
        this.mDialog.dismiss();
    }

    public InpDialBui setTitle(int i, int i2) {
        TextView textView = (TextView) this.mDialog.findViewById(R.id.tv_dialog_title);
        textView.setTextColor(i2);
        textView.setText(i);
        return this;
    }

    public Dialog build() {
        return this.mDialog;
    }
}
