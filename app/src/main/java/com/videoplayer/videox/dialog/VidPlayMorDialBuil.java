package com.videoplayer.videox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.vid.VidPlaMorLimAdapter;
import com.videoplayer.videox.adapter.vid.VidPlayMorAdapter;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.uti.cons.AppCon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class VidPlayMorDialBuil {
    VidPlayMorAdapter mAdapter;
    final Dialog mDialog;
    final boolean mIsLimit;
    VidPlaMorLimAdapter mLimitAdapter;
    final RecyclerView mRvContent;

    public interface Callback {
        void onMoreClick(int i, boolean z);
    }

    public VidPlayMorDialBuil(Context context, boolean z, Callback callback) {
        this.mIsLimit = z;
        Dialog dialog = new Dialog(context, AppCon.Themes.THEMES_STYLE[new SettingPrefUtils(context).getThemes()]);
        this.mDialog = dialog;
        dialog.requestWindowFeature(1);
        Window window = dialog.getWindow();
        Objects.requireNonNull(window);
        window.setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_video_player_more);
        dialog.findViewById(R.id.view_click).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlayMorDialBuil$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                VidPlayMorDialBuil.this.m905lambda$new$0$comvideoplayervideoxdialogVidPlayMorDialBuil(view);
            }
        });
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.rv_content_dialog);
        this.mRvContent = recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        Integer valueOf = Integer.valueOf(R.string.video_more_lock);
        Integer valueOf2 = Integer.valueOf(R.string.video_more_timer);
        Integer valueOf3 = Integer.valueOf(R.string.equalizer);
        Integer valueOf4 = Integer.valueOf(R.string.video_more_mirror);
        Integer valueOf5 = Integer.valueOf(R.string.video_more_night_mode);
        Integer valueOf6 = Integer.valueOf(R.string.video_more_repeat_all);
        Integer valueOf7 = Integer.valueOf(R.string.video_more_cast);
        Integer valueOf8 = Integer.valueOf(R.drawable.baseline_lock_24);
        Integer valueOf9 = Integer.valueOf(R.drawable.baseline_access_time_filled_24);
        Integer valueOf10 = Integer.valueOf(R.drawable.baseline_tune_24px);
        Integer valueOf11 = Integer.valueOf(R.drawable.baseline_flip_24);
        Integer valueOf12 = Integer.valueOf(R.drawable.baseline_mode_night_24);
        Integer valueOf13 = Integer.valueOf(R.drawable.baseline_repeat_24px);
        Integer valueOf14 = Integer.valueOf(R.drawable.baseline_cast_connected_24);
        if (!z) {
            VidPlayMorAdapter vidPlayMorAdapter = new VidPlayMorAdapter(new ArrayList(Arrays.asList(Integer.valueOf(R.drawable.baseline_content_cut_24), Integer.valueOf(R.drawable.baseline_favorite_border_24), valueOf14, valueOf13, valueOf12, valueOf11, valueOf10, Integer.valueOf(R.drawable.baseline_share_24), Integer.valueOf(R.drawable.ic_video_player_hw), valueOf9, valueOf8, Integer.valueOf(R.drawable.baseline_info_24))), new ArrayList(Arrays.asList(Integer.valueOf(R.string.video_more_cut), Integer.valueOf(R.string.video_more_favorite), valueOf7, valueOf6, valueOf5, valueOf4, valueOf3, Integer.valueOf(R.string.video_more_share), Integer.valueOf(R.string.video_more_hw), valueOf2, valueOf, Integer.valueOf(R.string.info))), callback);
            this.mAdapter = vidPlayMorAdapter;
            recyclerView.setAdapter(vidPlayMorAdapter);
        } else {
            VidPlaMorLimAdapter vidPlaMorLimAdapter = new VidPlaMorLimAdapter(new ArrayList(Arrays.asList(valueOf14, valueOf13, valueOf12, valueOf11, valueOf10, valueOf9, valueOf8)), new ArrayList(Arrays.asList(valueOf7, valueOf6, valueOf5, valueOf4, valueOf3, valueOf2, valueOf)), callback);
            this.mLimitAdapter = vidPlaMorLimAdapter;
            recyclerView.setAdapter(vidPlaMorLimAdapter);
        }
    }

    /* renamed from: lambda$new$0$com-videoplayer-videox-dialog-VidPlayMorDialBuil, reason: not valid java name */
    void m905lambda$new$0$comvideoplayervideoxdialogVidPlayMorDialBuil(View view) {
        this.mDialog.dismiss();
    }

    public void setFavorite(boolean z) {
        VidPlayMorAdapter vidPlayMorAdapter = this.mAdapter;
        if (vidPlayMorAdapter != null) {
            vidPlayMorAdapter.setFavorite(z);
        }
    }

    public void setRepeatMode(int i) {
        VidPlaMorLimAdapter vidPlaMorLimAdapter;
        if (!this.mIsLimit || (vidPlaMorLimAdapter = this.mLimitAdapter) == null) {
            VidPlayMorAdapter vidPlayMorAdapter = this.mAdapter;
            if (vidPlayMorAdapter != null) {
                vidPlayMorAdapter.setRepeatMode(i);
                return;
            }
            return;
        }
        vidPlaMorLimAdapter.setRepeatMode(i);
    }

    public void setNightMode(boolean z) {
        VidPlaMorLimAdapter vidPlaMorLimAdapter;
        if (!this.mIsLimit || (vidPlaMorLimAdapter = this.mLimitAdapter) == null) {
            VidPlayMorAdapter vidPlayMorAdapter = this.mAdapter;
            if (vidPlayMorAdapter != null) {
                vidPlayMorAdapter.setNightMode(z);
                return;
            }
            return;
        }
        vidPlaMorLimAdapter.setNightMode(z);
    }

    public void setHWD(boolean z) {
        VidPlayMorAdapter vidPlayMorAdapter = this.mAdapter;
        if (vidPlayMorAdapter != null) {
            vidPlayMorAdapter.setHWD(z);
        }
    }

    public void setMirror(boolean z) {
        VidPlaMorLimAdapter vidPlaMorLimAdapter;
        if (!this.mIsLimit || (vidPlaMorLimAdapter = this.mLimitAdapter) == null) {
            VidPlayMorAdapter vidPlayMorAdapter = this.mAdapter;
            if (vidPlayMorAdapter != null) {
                vidPlayMorAdapter.setMirror(z);
                return;
            }
            return;
        }
        vidPlaMorLimAdapter.setMirror(z);
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
