package com.videoplayer.videox.vie;

import com.videoplayer.videox.db.entity.music.MusicInfo;

import java.util.List;


public interface MusVie extends BasVie {
    void onSearchMusic(List<MusicInfo> list);

    void openFavoriteMusic(List<MusicInfo> list);
}
