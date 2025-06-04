package com.videoplayer.videox.dialog;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.videoplayer.videox.R;
import com.videoplayer.videox.uti.SharPrefUti;


public class RatDiaBuil {
    final EditText editText;
    final Dialog mDialog;
    final ImageView[] mStar;
    int rate;
    final TextView tvMayBeLater;
    final TextView tvSubmit;

    public interface Callback {
        void onDialogDismiss();
    }

    public RatDiaBuil(final Context context, final Callback callback) {
        this.rate = 0;
        Dialog dialog = new Dialog(context, R.style.CustomDialog);
        this.mDialog = dialog;
        this.mStar = new ImageView[]{(ImageView) mDialog.findViewById(R.id.rate1), (ImageView) mDialog.findViewById(R.id.rate2),
                (ImageView) mDialog.findViewById(R.id.rate3), (ImageView) mDialog.findViewById(R.id.rate4), (ImageView) mDialog.findViewById(R.id.rate5)};
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_rating);
        TextView textView = (TextView) dialog.findViewById(R.id.tv_submit);
        this.tvSubmit = textView;
        TextView textView2 = (TextView) dialog.findViewById(R.id.tv_maybe_later);
        this.tvMayBeLater = textView2;
        this.editText = (EditText) dialog.findViewById(R.id.edt_input_text);
        textView2.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.RatDiaBuil$$ExternalSyntheticLambda0
            @Override
            public final void onClick(View view) {
                RatDiaBuil.this.m890lambda$new$0$comvideoplayervideoxdialogRatDiaBuil(callback, view);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.RatDiaBuil$$ExternalSyntheticLambda1
            @Override
            public final void onClick(View view) {
                RatDiaBuil.this.m891lambda$new$1$comvideoplayervideoxdialogRatDiaBuil(context, callback, view);
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.RatDiaBuil$$ExternalSyntheticLambda2
            @Override
            public final void onClick(View view) {
                RatDiaBuil.this.m892lambda$new$2$comvideoplayervideoxdialogRatDiaBuil(context, callback, view);
            }
        };
        mStar[0].setOnClickListener(onClickListener);
        mStar[1].setOnClickListener(onClickListener);
        mStar[2].setOnClickListener(onClickListener);
        mStar[3].setOnClickListener(onClickListener);
        mStar[4].setOnClickListener(onClickListener);
    }

    /* renamed from: lambda$new$0$com-videoplayer-videox-dialog-RatDiaBuil, reason: not valid java name */
    void m890lambda$new$0$comvideoplayervideoxdialogRatDiaBuil(Callback callback, View view) {
        this.mDialog.dismiss();
        callback.onDialogDismiss();
    }

    /* renamed from: lambda$new$1$com-videoplayer-videox-dialog-RatDiaBuil, reason: not valid java name */
    void m891lambda$new$1$comvideoplayervideoxdialogRatDiaBuil(Context context, Callback callback, View view) {
        Toast.makeText(context, R.string.thank_for_rating, Toast.LENGTH_SHORT).show();
        this.mDialog.dismiss();
        callback.onDialogDismiss();
        SharPrefUti.putBoolean(context, "RATED_APP", true);
    }

    /* renamed from: lambda$new$2$com-videoplayer-videox-dialog-RatDiaBuil, reason: not valid java name */
    void m892lambda$new$2$comvideoplayervideoxdialogRatDiaBuil(Context context, Callback callback, View view) {
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
