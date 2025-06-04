package com.videoplayer.videox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.videoplayer.videox.R;


public class QueDiaBuil {
    private final Dialog mDialog;

    public interface OkButtonClickListener {
        void onCancelClick();

        void onOkClick();
    }

    public QueDiaBuil(Context context, final OkButtonClickListener okButtonClickListener) {
        Dialog dialog = new Dialog(context, R.style.CustomDialog);
        this.mDialog = dialog;
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_question);
        dialog.findViewById(R.id.tv_dialog_cancel).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.QueDiaBuil$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                QueDiaBuil.this.m888lambda$new$0$comvideoplayervideoxdialogQueDiaBuil(okButtonClickListener, view);
            }
        });
        dialog.findViewById(R.id.tv_dialog_ok).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.QueDiaBuil$$ExternalSyntheticLambda1
            @Override 
            public final void onClick(View view) {
                QueDiaBuil.this.m889lambda$new$1$comvideoplayervideoxdialogQueDiaBuil(okButtonClickListener, view);
            }
        });
    }

    /* renamed from: lambda$new$0$com-videoplayer-videox-dialog-QueDiaBuil, reason: not valid java name */
    void m888lambda$new$0$comvideoplayervideoxdialogQueDiaBuil(OkButtonClickListener okButtonClickListener, View view) {
        okButtonClickListener.onCancelClick();
        this.mDialog.dismiss();
    }

    /* renamed from: lambda$new$1$com-videoplayer-videox-dialog-QueDiaBuil, reason: not valid java name */
    void m889lambda$new$1$comvideoplayervideoxdialogQueDiaBuil(OkButtonClickListener okButtonClickListener, View view) {
        okButtonClickListener.onOkClick();
        this.mDialog.dismiss();
    }

    public QueDiaBuil setTitle(int i, int i2) {
        TextView textView = (TextView) this.mDialog.findViewById(R.id.tv_dialog_title);
        textView.setTextColor(i2);
        textView.setText(i);
        return this;
    }

    public QueDiaBuil setQuestion(int i) {
        ((TextView) this.mDialog.findViewById(R.id.tv_question)).setText(i);
        return this;
    }

    public QueDiaBuil setQuestion(String str) {
        ((TextView) this.mDialog.findViewById(R.id.tv_question)).setText(str);
        return this;
    }

    public QueDiaBuil setOkButtonText(int i) {
        ((TextView) this.mDialog.findViewById(R.id.tv_dialog_ok)).setText(i);
        return this;
    }

    public QueDiaBuil setCancelButtonText(int i) {
        ((TextView) this.mDialog.findViewById(R.id.tv_dialog_cancel)).setText(i);
        return this;
    }

    public Dialog build() {
        return this.mDialog;
    }
}
