package com.videoplayer.videox.dialog;

import android.app.Dialog;
import android.content.Context;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.BotMenAdapter;
import com.videoplayer.videox.uti.ads.Utility;

import java.util.ArrayList;
import java.util.Arrays;


public class BtmMenDialCont {
    private static BtmMenDialCont sInstance;
    Dialog mDialog;

    public static BtmMenDialCont getInstance() {
        if (sInstance == null) {
            sInstance = new BtmMenDialCont();
        }
        return sInstance;
    }

    public void showMoreDialogVideo(Context context, boolean z, final BotMenAdapter.Callback callback) {
        int i;
        int i2;
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            if (z) {
                i = R.string.remove_from_favorite;
                i2 = R.drawable.baseline_favorite_24;
            } else {
                i = R.string.add_to_favorite;
                i2 = R.drawable.baseline_favorite_border_24;
            }
            BottomSheetDialog build = new BtmMenDialBuil(context, new BotMenAdapter(new ArrayList(Arrays.asList(Integer.valueOf(R.string.play_background), Integer.valueOf(R.string.play_as_audio), Integer.valueOf(i), Integer.valueOf(R.string.convert_to_mp3), Integer.valueOf(R.string.rename), Integer.valueOf(R.string.share), Integer.valueOf(R.string.info), Integer.valueOf(R.string.delete_video))), new ArrayList(Arrays.asList(Integer.valueOf(R.drawable.baseline_headphones_24), Integer.valueOf(R.drawable.baseline_play_circle_24), Integer.valueOf(i2), Integer.valueOf(R.drawable.baseline_library_music_24), Integer.valueOf(R.drawable.baseline_edit_24), Integer.valueOf(R.drawable.baseline_share_24), Integer.valueOf(R.drawable.baseline_info_24), Integer.valueOf(R.drawable.baseline_delete_24))), new BotMenAdapter.Callback() { // from class: com.videoplayer.videox.dialog.BtmMenDialCont$$ExternalSyntheticLambda1
                @Override // com.videoplayer.videox.adapter.BotMenAdapter.Callback
                public final void onClick(int i3) {
                    BtmMenDialCont.this.m636x7785ba43(callback, i3);
                }
            })).build();
            this.mDialog = build;
            build.show();
        }
    }

    /* renamed from: lambda$showMoreDialogVideo$0$com-videoplayer-videox-dialog-BtmMenDialCont */
    void m636x7785ba43(BotMenAdapter.Callback callback, int i) {
        this.mDialog.dismiss();
        callback.onClick(i);
    }

    public void showMoreDialogHiddenVideo(Context context, final BotMenAdapter.Callback callback) {
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            BottomSheetDialog build = new BtmMenDialBuil(context, new BotMenAdapter(new ArrayList(Arrays.asList(Integer.valueOf(R.string.unhide_file))), new ArrayList(Arrays.asList(Integer.valueOf(R.drawable.baseline_folder_24))), new BotMenAdapter.Callback() { // from class: com.videoplayer.videox.dialog.BtmMenDialCont$$ExternalSyntheticLambda7
                @Override // com.videoplayer.videox.adapter.BotMenAdapter.Callback
                public final void onClick(int i) {
                    BtmMenDialCont.this.m632x9680236c(callback, i);
                }
            })).build();
            this.mDialog = build;
            build.show();
        }
    }

    /* renamed from: lambda$showMoreDialogHiddenVideo$1$com-videoplayer-videox-dialog-BtmMenDialCont */
    void m632x9680236c(BotMenAdapter.Callback callback, int i) {
        this.mDialog.dismiss();
        callback.onClick(i);
    }

    public void showMoreDialogMusic(Context context, boolean z, final BotMenAdapter.Callback callback) {
        int i;
        int i2;
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            if (z) {
                i = R.string.remove_from_favorite;
                i2 = R.drawable.baseline_favorite_24;
            } else {
                i = R.string.add_to_favorite;
                i2 = R.drawable.baseline_favorite_border_24;
            }
            BottomSheetDialog build = new BtmMenDialBuil(context, new BotMenAdapter(new ArrayList(Arrays.asList(Integer.valueOf(i), Integer.valueOf(R.string.rename), Integer.valueOf(R.string.share), Integer.valueOf(R.string.info), Integer.valueOf(R.string.delete_song))), new ArrayList(Arrays.asList(Integer.valueOf(i2), Integer.valueOf(R.drawable.baseline_edit_24), Integer.valueOf(R.drawable.baseline_share_24), Integer.valueOf(R.drawable.baseline_info_24), Integer.valueOf(R.drawable.baseline_delete_24))), new BotMenAdapter.Callback() { // from class: com.videoplayer.videox.dialog.BtmMenDialCont$$ExternalSyntheticLambda3
                @Override // com.videoplayer.videox.adapter.BotMenAdapter.Callback
                public final void onClick(int i3) {
                    BtmMenDialCont.this.m634xe9f45a97(callback, i3);
                }
            })).build();
            this.mDialog = build;
            build.show();
        }
    }

    /* renamed from: lambda$showMoreDialogMusic$2$com-videoplayer-videox-dialog-BtmMenDialCont */
    void m634xe9f45a97(BotMenAdapter.Callback callback, int i) {
        this.mDialog.dismiss();
        callback.onClick(i);
    }

    public void showMoreDialogVideoFolder(Context context, final BotMenAdapter.Callback callback) {
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            BottomSheetDialog build = new BtmMenDialBuil(context, new BotMenAdapter(new ArrayList(Arrays.asList(Integer.valueOf(R.string.play_as_audio), Integer.valueOf(R.string.delete_folder), Integer.valueOf(R.string.info))), new ArrayList(Arrays.asList(Integer.valueOf(R.drawable.baseline_headphones_24), Integer.valueOf(R.drawable.baseline_delete_24), Integer.valueOf(R.drawable.baseline_info_24))), new BotMenAdapter.Callback() { // from class: com.videoplayer.videox.dialog.BtmMenDialCont$$ExternalSyntheticLambda4
                @Override // com.videoplayer.videox.adapter.BotMenAdapter.Callback
                public final void onClick(int i) {
                    BtmMenDialCont.this.m637xcdca5fd2(callback, i);
                }
            })).build();
            this.mDialog = build;
            build.show();
        }
    }

    /* renamed from: lambda$showMoreDialogVideoFolder$3$com-videoplayer-videox-dialog-BtmMenDialCont */
    void m637xcdca5fd2(BotMenAdapter.Callback callback, int i) {
        this.mDialog.dismiss();
        callback.onClick(i);
    }

    public void showMoreDialogPlaylist(Context context, final BotMenAdapter.Callback callback) {
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            BottomSheetDialog build = new BtmMenDialBuil(context, new BotMenAdapter(new ArrayList(Arrays.asList(Integer.valueOf(R.string.rename), Integer.valueOf(R.string.duplicate_playlist), Integer.valueOf(R.string.delete_playlist))), new ArrayList(Arrays.asList(Integer.valueOf(R.drawable.baseline_edit_24), Integer.valueOf(R.drawable.baseline_file_copy_24), Integer.valueOf(R.drawable.baseline_delete_24))), new BotMenAdapter.Callback() { // from class: com.videoplayer.videox.dialog.BtmMenDialCont$$ExternalSyntheticLambda5
                @Override // com.videoplayer.videox.adapter.BotMenAdapter.Callback
                public final void onClick(int i) {
                    BtmMenDialCont.this.m635xb287553a(callback, i);
                }
            })).build();
            this.mDialog = build;
            build.show();
        }
    }

    /* renamed from: lambda$showMoreDialogPlaylist$4$com-videoplayer-videox-dialog-BtmMenDialCont */
    void m635xb287553a(BotMenAdapter.Callback callback, int i) {
        this.mDialog.dismiss();
        callback.onClick(i);
    }

    public void showMoreDialogHistory(Context context, final BotMenAdapter.Callback callback) {
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            BottomSheetDialog build = new BtmMenDialBuil(context, new BotMenAdapter(new ArrayList(Arrays.asList(Integer.valueOf(R.string.delete_from_history), Integer.valueOf(R.string.delete_file), Integer.valueOf(R.string.info))), new ArrayList(Arrays.asList(Integer.valueOf(R.drawable.baseline_delete_24), Integer.valueOf(R.drawable.baseline_delete_24), Integer.valueOf(R.drawable.baseline_info_24))), new BotMenAdapter.Callback() { // from class: com.videoplayer.videox.dialog.BtmMenDialCont$$ExternalSyntheticLambda2
                @Override // com.videoplayer.videox.adapter.BotMenAdapter.Callback
                public final void onClick(int i) {
                    BtmMenDialCont.this.m633x5b641b65(callback, i);
                }
            })).build();
            this.mDialog = build;
            build.show();
        }
    }

    /* renamed from: lambda$showMoreDialogHistory$5$com-videoplayer-videox-dialog-BtmMenDialCont */
    void m633x5b641b65(BotMenAdapter.Callback callback, int i) {
        this.mDialog.dismiss();
        callback.onClick(i);
    }

    public void showSettingVideoModeDialog(Context context, int i, SetVidModDiaBuil.OkVideoModeListener okVideoModeListener) {
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            BottomSheetDialog build = new SetVidModDiaBuil(context, i, okVideoModeListener).build();
            this.mDialog = build;
            build.show();
        }
    }

    public void showSortDialogForMusic(final Context context, final SorDialBuil.OkButtonClickListener okButtonClickListener) {
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            Dialog build = new SorDialBuil(context, new SorDialBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.dialog.BtmMenDialCont$$ExternalSyntheticLambda6
                @Override // com.videoplayer.videox.dialog.SorDialBuil.OkButtonClickListener
                public final void onClick(int i, boolean z) {
                    BtmMenDialCont.lambda$showSortDialogForMusic$6(context, okButtonClickListener, i, z);
                }
            }, Utility.getMusicSortMode(context), Utility.getMusicSortAscending(context)).build();
            this.mDialog = build;
            build.show();
        }
    }

    static void lambda$showSortDialogForMusic$6(Context context, SorDialBuil.OkButtonClickListener okButtonClickListener, int i, boolean z) {
        Utility.setMusicSortModeAndAscending(context, i, z);
        okButtonClickListener.onClick(i, z);
    }

    public void showSortDialogForVideo(final Context context, final SorDialBuil.OkButtonClickListener okButtonClickListener) {
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            Dialog build = new SorDialBuil(context, new SorDialBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.dialog.BtmMenDialCont$$ExternalSyntheticLambda0
                @Override // com.videoplayer.videox.dialog.SorDialBuil.OkButtonClickListener
                public final void onClick(int i, boolean z) {
                    BtmMenDialCont.lambda$showSortDialogForVideo$7(context, okButtonClickListener, i, z);
                }
            }, Utility.getVideoSortMode(context), Utility.getVideoSortAscending(context)).build();
            this.mDialog = build;
            build.show();
        }
    }

    static void lambda$showSortDialogForVideo$7(Context context, SorDialBuil.OkButtonClickListener okButtonClickListener, int i, boolean z) {
        Utility.setVideoSortModeAndAscending(context, i, z);
        okButtonClickListener.onClick(i, z);
    }
}
