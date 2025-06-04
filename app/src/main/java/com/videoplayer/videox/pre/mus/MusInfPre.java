package com.videoplayer.videox.pre.mus;

import android.content.Context;

import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.repository.ILoaderRepository;
import com.videoplayer.videox.db.repository.MusicDataRepository;
import com.videoplayer.videox.pre.BasePre;
import com.videoplayer.videox.vie.mus.MusInfVie;

import java.util.List;


public class MusInfPre extends BasePre<MusInfVie> {
    private final MusicDataRepository mMusicRepository;

    public MusInfPre(Context context, MusInfVie musicInfoView, MusicDataRepository musicDataRepository) {
        super(musicInfoView);
        this.mMusicRepository = musicDataRepository;
    }

    public void openMusicsTab() {
        this.mMusicRepository.getAllMusics(new ILoaderRepository.LoadDataListener<MusicInfo>() { // from class: com.videoplayer.videox.pre.mus.MusInfPre.1
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onError() {
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onSuccess(List<MusicInfo> list) {
                if (MusInfPre.this.mView != null) {
                    ((MusInfVie) MusInfPre.this.mView).updateMusicList(list);
                }
            }
        });
    }
}
