package com.videoplayer.videox.vie.mus;

import com.videoplayer.videox.db.entity.music.MusicArtist;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.vie.BasVie;

import java.util.List;


public interface MusArtVie extends BasVie {
    void onOpenArtist(List<MusicArtist> list);

    void onUpdateMusicOfArtist(List<MusicInfo> list);
}
