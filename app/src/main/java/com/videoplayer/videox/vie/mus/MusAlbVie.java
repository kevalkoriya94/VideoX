package com.videoplayer.videox.vie.mus;

import com.videoplayer.videox.db.entity.music.MusicAlbum;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.vie.BasVie;

import java.util.List;


public interface MusAlbVie extends BasVie {
    void onUpdateAlbum(List<MusicAlbum> list);

    void onUpdateMusicList(List<MusicInfo> list);
}
