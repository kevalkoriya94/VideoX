package com.videoplayer.videox.vie.mus;

import com.videoplayer.videox.db.entity.music.MusicPlaylist;
import com.videoplayer.videox.vie.BasVie;

import java.util.List;


public interface MusPlylistVie extends BasVie {
    void onCreatePlaylist(boolean z, MusicPlaylist musicPlaylist);

    void onDuplicationPlaylist(MusicPlaylist musicPlaylist);

    void onUpdatePlaylistName(int i, String str, boolean z);

    void updateMusicPlaylist(List<MusicPlaylist> list);
}
