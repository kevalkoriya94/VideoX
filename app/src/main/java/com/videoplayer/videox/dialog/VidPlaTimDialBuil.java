package com.videoplayer.videox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.NumberPicker;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.vid.VidTimAdapter;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.uti.cons.AppCon;

import java.util.Objects;


public class VidPlaTimDialBuil {
    private final ConstraintLayout layoutCustomTime;
    private final VidTimAdapter mAdapter;
    private final Dialog mDialog;
    private final NumberPicker pickerHour;
    private final NumberPicker pickerMinute;

    public interface Callback {
        void startTimeCountdown(long j);
    }

    public VidPlaTimDialBuil(Context context, final Callback callback) {
        Dialog dialog = new Dialog(context, AppCon.Themes.THEMES_STYLE[new SettingPrefUtils(context).getThemes()]);
        this.mDialog = dialog;
        dialog.requestWindowFeature(1);
        Window window = dialog.getWindow();
        Objects.requireNonNull(window);
        window.setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_video_player_timer);
        ConstraintLayout constraintLayout = (ConstraintLayout) dialog.findViewById(R.id.layout_custom_time);
        this.layoutCustomTime = constraintLayout;
        NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.picker_hour);
        this.pickerHour = numberPicker;
        NumberPicker numberPicker2 = (NumberPicker) dialog.findViewById(R.id.picker_min);
        this.pickerMinute = numberPicker2;
        dialog.findViewById(R.id.view_click).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlaTimDialBuil$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                VidPlaTimDialBuil.this.m898lambda$new$0$comvideoplayervideoxdialogVidPlaTimDialBuil(view);
            }
        });
        dialog.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlaTimDialBuil$$ExternalSyntheticLambda1
            @Override 
            public final void onClick(View view) {
                VidPlaTimDialBuil.this.m899lambda$new$1$comvideoplayervideoxdialogVidPlaTimDialBuil(view);
            }
        });
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.rv_content_dialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, 0, false));
        VidTimAdapter vidTimAdapter = new VidTimAdapter(context, new VidTimAdapter.Callback() { // from class: com.videoplayer.videox.dialog.VidPlaTimDialBuil$$ExternalSyntheticLambda2
            @Override // com.videoplayer.videox.adapter.vid.VidTimAdapter.Callback
            public final void onTimerSelect(int i) {
                VidPlaTimDialBuil.this.m900lambda$new$2$comvideoplayervideoxdialogVidPlaTimDialBuil(i);
            }
        });
        this.mAdapter = vidTimAdapter;
        recyclerView.setAdapter(vidTimAdapter);
        dialog.findViewById(R.id.iv_done).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlaTimDialBuil$$ExternalSyntheticLambda3
            @Override 
            public final void onClick(View view) {
                VidPlaTimDialBuil.this.m901lambda$new$3$comvideoplayervideoxdialogVidPlaTimDialBuil(callback, view);
            }
        });
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker2.setMaxValue(59);
        numberPicker2.setMinValue(0);
        numberPicker2.setWrapSelectorWheel(true);
        constraintLayout.setAlpha(0.4f);
        numberPicker.setEnabled(false);
        numberPicker2.setEnabled(false);
    }

    /* renamed from: lambda$new$0$com-videoplayer-videox-dialog-VidPlaTimDialBuil, reason: not valid java name */
    void m898lambda$new$0$comvideoplayervideoxdialogVidPlaTimDialBuil(View view) {
        this.mDialog.dismiss();
    }

    /* renamed from: lambda$new$1$com-videoplayer-videox-dialog-VidPlaTimDialBuil, reason: not valid java name */
    void m899lambda$new$1$comvideoplayervideoxdialogVidPlaTimDialBuil(View view) {
        this.mDialog.dismiss();
    }

    /* renamed from: lambda$new$2$com-videoplayer-videox-dialog-VidPlaTimDialBuil, reason: not valid java name */
    void m900lambda$new$2$comvideoplayervideoxdialogVidPlaTimDialBuil(int i) {
        if (i == 7) {
            this.layoutCustomTime.setAlpha(1.0f);
            this.pickerHour.setEnabled(true);
            this.pickerMinute.setEnabled(true);
        } else {
            this.layoutCustomTime.setAlpha(0.4f);
            this.pickerHour.setEnabled(false);
            this.pickerMinute.setEnabled(false);
        }
    }

    /* renamed from: lambda$new$3$com-videoplayer-videox-dialog-VidPlaTimDialBuil, reason: not valid java name */
    void m901lambda$new$3$comvideoplayervideoxdialogVidPlaTimDialBuil(Callback callback, View view) {
        long numberSelected;
        if (this.mAdapter.getNumberSelected() == 0) {
            numberSelected = -1;
        } else if (this.mAdapter.getNumberSelected() == 7) {
            numberSelected = (this.pickerHour.getValue() * 3600000) + (this.pickerMinute.getValue() * 60000);
        } else {
            numberSelected = this.mAdapter.getNumberSelected() * 10 * 60000;
        }
        callback.startTimeCountdown(numberSelected);
        this.mDialog.dismiss();
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
