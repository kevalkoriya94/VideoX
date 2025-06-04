package com.videoplayer.videox.vie.mus;

import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.vie.BasVie;

import java.util.List;


public interface MusInfVie extends BasVie {
    void updateMusicList(List<MusicInfo> list);
}
