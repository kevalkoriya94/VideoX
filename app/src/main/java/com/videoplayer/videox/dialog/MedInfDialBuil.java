package com.videoplayer.videox.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.videoplayer.videox.R;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.uti.ads.Utility;


public class MedInfDialBuil<T> {
    private final BottomSheetDialog mDialog;

    /* JADX WARN: Multi-variable type inference failed */
    public MedInfDialBuil(Context context, T t) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.CustomDialog);
        this.mDialog = bottomSheetDialog;
        bottomSheetDialog.requestWindowFeature(1);
        bottomSheetDialog.setContentView(R.layout.dialog_media_info);
        TextView textView = (TextView) bottomSheetDialog.findViewById(R.id.tv_info_name);
        TextView textView2 = (TextView) bottomSheetDialog.findViewById(R.id.tv_info_artist);
        TextView textView3 = (TextView) bottomSheetDialog.findViewById(R.id.tv_info_album);
        TextView textView4 = (TextView) bottomSheetDialog.findViewById(R.id.tv_info_size);
        TextView textView5 = (TextView) bottomSheetDialog.findViewById(R.id.tv_info_date);
        TextView textView6 = (TextView) bottomSheetDialog.findViewById(R.id.tv_info_length);
        TextView textView7 = (TextView) bottomSheetDialog.findViewById(R.id.tv_info_resolution);
        TextView textView8 = (TextView) bottomSheetDialog.findViewById(R.id.tv_info_path);
        TextView textView9 = (TextView) bottomSheetDialog.findViewById(R.id.tv_title_artist);
        TextView textView10 = (TextView) bottomSheetDialog.findViewById(R.id.tv_title_album);
        TextView textView11 = (TextView) bottomSheetDialog.findViewById(R.id.tv_title_resolution);
        if (t instanceof VideoInfo) {
            VideoInfo videoInfo = (VideoInfo) t;
            textView.setText(videoInfo.getDisplayName());
            textView4.setText(Utility.convertSize(videoInfo.getSize()));
            textView5.setText(Utility.convertLongToTime(videoInfo.getDate(), "yyyy-MM-dd HH:mm:ss"));
            textView6.setText(Utility.convertLongToDuration(videoInfo.getDuration()));
            textView7.setText(videoInfo.getResolution());
            textView8.setText(videoInfo.getPath());
            textView2.setVisibility(View.GONE);
            textView9.setVisibility(View.GONE);
            textView3.setVisibility(View.GONE);
            textView10.setVisibility(View.GONE);
            return;
        }
        if (t instanceof MusicInfo) {
            MusicInfo musicInfo = (MusicInfo) t;
            textView.setText(musicInfo.getDisplayName());
            textView2.setText(musicInfo.getArtist());
            textView3.setText(musicInfo.getAlbum());
            textView4.setText(Utility.convertSize(musicInfo.getSize()));
            textView5.setText(Utility.convertLongToTime(musicInfo.getDate() * 1000, "yyyy-MM-dd HH:mm:ss"));
            textView6.setText(Utility.convertLongToDuration(musicInfo.getDuration()));
            textView8.setText(musicInfo.getPath());
            textView7.setVisibility(View.GONE);
            textView11.setVisibility(View.GONE);
        }
    }

    public BottomSheetDialog build() {
        return this.mDialog;
    }
}
