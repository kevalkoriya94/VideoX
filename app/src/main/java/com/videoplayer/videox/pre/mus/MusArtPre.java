package com.videoplayer.videox.pre.mus;

import android.content.Context;

import com.videoplayer.videox.db.entity.music.MusicArtist;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.repository.ILoaderRepository;
import com.videoplayer.videox.db.repository.MusicDataRepository;
import com.videoplayer.videox.pre.BasePre;
import com.videoplayer.videox.vie.mus.MusArtVie;

import java.util.List;


public class MusArtPre extends BasePre<MusArtVie> {
    private final MusicDataRepository mMusicRepository;

    public MusArtPre(Context context, MusArtVie musicArtistView, MusicDataRepository musicDataRepository) {
        super(musicArtistView);
        this.mMusicRepository = musicDataRepository;
    }

    public void openArtistTab() {
        this.mMusicRepository.getAllMusicArtist(new ILoaderRepository.LoadDataListener<MusicArtist>() { // from class: com.videoplayer.videox.pre.mus.MusArtPre.1
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onError() {
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onSuccess(List<MusicArtist> list) {
                if (MusArtPre.this.mView != null) {
                    ((MusArtVie) MusArtPre.this.mView).onOpenArtist(list);
                }
            }
        });
    }

    public void getAllSongsOfArtist(MusicArtist musicArtist) {
        this.mMusicRepository.getAllSongsOfArtist(new ILoaderRepository.LoadDataListener<MusicInfo>() { // from class: com.videoplayer.videox.pre.mus.MusArtPre.2
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onError() {
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onSuccess(List<MusicInfo> list) {
                if (MusArtPre.this.mView != null) {
                    ((MusArtVie) MusArtPre.this.mView).onUpdateMusicOfArtist(list);
                }
            }
        }, musicArtist);
    }
}
