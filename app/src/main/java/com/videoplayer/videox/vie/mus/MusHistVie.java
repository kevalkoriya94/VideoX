package com.videoplayer.videox.vie.mus;

import com.videoplayer.videox.db.entity.music.MusicHistory;
import com.videoplayer.videox.vie.BasVie;

import java.util.List;


public interface MusHistVie extends BasVie {
    void updateMusicHistoryList(List<MusicHistory> list);
}
