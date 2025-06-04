package com.videoplayer.videox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.vid.NexVidPlaAdapter;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.uti.cons.AppCon;

import java.util.List;
import java.util.Objects;


public class VidPlayNxtInDialBuil {
    final NexVidPlaAdapter mAdapter;
    final Dialog mDialog;
    final RecyclerView mRvContent;

    public VidPlayNxtInDialBuil(Context context, int i, List<VideoInfo> list, NexVidPlaAdapter.Callback callback) {
        Dialog dialog = new Dialog(context, AppCon.Themes.THEMES_STYLE[new SettingPrefUtils(context).getThemes()]);
        this.mDialog = dialog;
        dialog.requestWindowFeature(1);
        Window window = dialog.getWindow();
        Objects.requireNonNull(window);
        window.setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_video_player_next_in);
        dialog.findViewById(R.id.view_click).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlayNxtInDialBuil$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                VidPlayNxtInDialBuil.this.m906lambda$new$0$comvideoplayervideoxdialogVidPlayNxtInDialBuil(view);
            }
        });
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.rv_content_dialog);
        this.mRvContent = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        NexVidPlaAdapter nexVidPlaAdapter = new NexVidPlaAdapter(context, i, list, callback);
        this.mAdapter = nexVidPlaAdapter;
        recyclerView.setAdapter(nexVidPlaAdapter);
        recyclerView.scrollToPosition(i);
    }

    /* renamed from: lambda$new$0$com-videoplayer-videox-dialog-VidPlayNxtInDialBuil, reason: not valid java name */
    void m906lambda$new$0$comvideoplayervideoxdialogVidPlayNxtInDialBuil(View view) {
        this.mDialog.dismiss();
    }

    public void updateCurrentPosition(int i) {
        this.mAdapter.updateCurrentPosition(i);
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
