package com.videoplayer.videox.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.videoplayer.videox.R;
import com.videoplayer.videox.uti.SharPrefUti;


public class ExiDialBuil {
    final EditText editText;
    final Dialog mDialog;
    final ImageView[] mStar;
    int rate;
    final TextView tvMayBeLater;
    final TextView tv_exit;

    public interface Callback {
        void onDialogDismiss();
    }

    public ExiDialBuil(final Context context, final Callback callback) {
        this.rate = 0;
        Dialog dialog = new Dialog(context, R.style.CustomDialog);
        this.mDialog = dialog;
        ImageView[] imageViewArr = {(ImageView) mDialog.findViewById(R.id.rate1), (ImageView) mDialog.findViewById(R.id.rate2),
                (ImageView) mDialog.findViewById(R.id.rate3), (ImageView) mDialog.findViewById(R.id.rate4), (ImageView) mDialog.findViewById(R.id.rate5)};
        this.mStar = imageViewArr;
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_exit);
        TextView textView = (TextView) dialog.findViewById(R.id.tv_exit);
        this.tv_exit = textView;
        TextView textView2 = (TextView) dialog.findViewById(R.id.tv_maybe_later);
        this.tvMayBeLater = textView2;
        this.editText = (EditText) dialog.findViewById(R.id.edt_input_text);
        textView2.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.ExiDialBuil$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                ExiDialBuil.this.m884lambda$new$0$comvideoplayervideoxdialogExiDialBuil(callback, view);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.ExiDialBuil$$ExternalSyntheticLambda1
            @Override 
            public final void onClick(View view) {
                ExiDialBuil.lambda$new$1(context, view);
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.ExiDialBuil$$ExternalSyntheticLambda2
            @Override 
            public final void onClick(View view) {
                ExiDialBuil.this.m885lambda$new$2$comvideoplayervideoxdialogExiDialBuil(context, callback, view);
            }
        };
        imageViewArr[0].setOnClickListener(onClickListener);
        imageViewArr[1].setOnClickListener(onClickListener);
        imageViewArr[2].setOnClickListener(onClickListener);
        imageViewArr[3].setOnClickListener(onClickListener);
        imageViewArr[4].setOnClickListener(onClickListener);
    }

    /* renamed from: lambda$new$0$com-videoplayer-videox-dialog-ExiDialBuil, reason: not valid java name */
    void m884lambda$new$0$comvideoplayervideoxdialogExiDialBuil(Callback callback, View view) {
        this.mDialog.dismiss();
        callback.onDialogDismiss();
    }

    static void lambda$new$1(Context context, View view) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putBoolean("firstTime", false);
        edit.apply();
        ((Activity) context).finishAffinity();
    }

    /* renamed from: lambda$new$2$com-videoplayer-videox-dialog-ExiDialBuil, reason: not valid java name */
    void m885lambda$new$2$comvideoplayervideoxdialogExiDialBuil(Context context, Callback callback, View view) {
        int id = view.getId();
        if (id == R.id.rate1) {
            this.rate = 1;
        } else if (id == R.id.rate2) {
            this.rate = 2;
        } else if (id == R.id.rate3) {
            this.rate = 3;
        } else if (id == R.id.rate4) {
            this.rate = 4;
        } else if (id == R.id.rate5) {
            this.rate = 5;
        }
        setContentRate(this.rate, context, callback);
    }

    private void setContentRate(int i, Context context, Callback callback) {
        int i2 = 0;
        while (true) {
            boolean z = true;
            if (i2 >= 5) {
                break;
            }
            ImageView imageView = this.mStar[i2];
            if (i2 >= i) {
                z = false;
            }
            imageView.setActivated(z);
            i2++;
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
        intent.addFlags(1208483840);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
        }
        this.mDialog.dismiss();
        callback.onDialogDismiss();
        SharPrefUti.putBoolean(context, "RATED_APP", true);
    }

    public Dialog build() {
        return this.mDialog;
    }
}
