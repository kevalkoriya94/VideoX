package com.videoplayer.videox.pre.mus;

import android.content.Context;

import com.videoplayer.videox.db.entity.music.MusicHistory;
import com.videoplayer.videox.db.repository.ILoaderRepository;
import com.videoplayer.videox.db.repository.MusicDataRepository;
import com.videoplayer.videox.pre.BasePre;
import com.videoplayer.videox.vie.mus.MusHistVie;

import java.util.List;


public class MusHisPre extends BasePre<MusHistVie> {
    private final MusicDataRepository mMusicRepository;

    public MusHisPre(Context context, MusHistVie musicHistoryView, MusicDataRepository musicDataRepository) {
        super(musicHistoryView);
        this.mMusicRepository = musicDataRepository;
    }

    public void openMusicHistoryTab() {
        this.mMusicRepository.getHistoryMusics(new ILoaderRepository.LoadDataListener<MusicHistory>() { // from class: com.videoplayer.videox.pre.mus.MusHisPre.1
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onError() {
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onSuccess(List<MusicHistory> list) {
                if (MusHisPre.this.mView != null) {
                    ((MusHistVie) MusHisPre.this.mView).updateMusicHistoryList(list);
                }
            }
        });
    }

    public void deleteAHistoryMusicById(long j) {
        this.mMusicRepository.deleteAHistoryMusicById(j);
    }
}
