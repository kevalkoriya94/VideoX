package com.videoplayer.videox.dialog;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.mus.NextInMusPlaAdapter;
import com.videoplayer.videox.db.entity.music.MusicInfo;

import java.util.List;


public class MusPlayNxtInDialBuil {
    private NextInMusPlaAdapter mAdapter;
    private final BottomSheetDialog mDialog;

    public MusPlayNxtInDialBuil(Context context, List<MusicInfo> list, int i, NextInMusPlaAdapter.Callback callback) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.CustomDialog);
        this.mDialog = bottomSheetDialog;
        bottomSheetDialog.setContentView(R.layout.dialog_music_player_next_in);
        RecyclerView recyclerView = (RecyclerView) bottomSheetDialog.findViewById(R.id.rv_next_in_playlist);
        if (list == null || list.isEmpty()) {
            bottomSheetDialog.dismiss();
            return;
        }
        NextInMusPlaAdapter nextInMusPlaAdapter = new NextInMusPlaAdapter(context, list, i, callback);
        this.mAdapter = nextInMusPlaAdapter;
        recyclerView.setAdapter(nextInMusPlaAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.scrollToPosition(i);
    }

    public void updateCurrentSong(int i) {
        this.mAdapter.updateCurrentPosition(i);
    }

    public BottomSheetDialog build() {
        return this.mDialog;
    }
}
