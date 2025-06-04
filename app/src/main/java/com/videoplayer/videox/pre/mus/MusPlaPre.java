package com.videoplayer.videox.pre.mus;

import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.repository.MusicDataRepository;


public class MusPlaPre {
    private final MusicDataRepository mMusicRepository;

    public MusPlaPre(MusicDataRepository musicDataRepository) {
        this.mMusicRepository = musicDataRepository;
    }

    public void updateMusicHistoryData(MusicInfo musicInfo) {
        this.mMusicRepository.updateMusicHistoryData(musicInfo);
    }
}
