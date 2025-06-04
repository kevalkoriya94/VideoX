package com.videoplayer.videox.pre.mus;

import android.content.Context;

import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.entity.music.MusicPlaylist;
import com.videoplayer.videox.db.repository.ILoaderRepository;
import com.videoplayer.videox.db.repository.MusicDataRepository;
import com.videoplayer.videox.pre.BasePre;
import com.videoplayer.videox.vie.MusVie;

import java.util.List;


public class MusicPresenter extends BasePre<MusVie> {
    private final MusicDataRepository mMusicRepository;

    public MusicPresenter(Context context, MusVie musicView, MusicDataRepository musicDataRepository) {
        super(musicView);
        this.mMusicRepository = musicDataRepository;
    }

    public void deleteAllMusicHistory() {
        this.mMusicRepository.deleteAllMusicHistory();
    }

    public void getAllFavoriteMusic() {
        this.mMusicRepository.getAllFavoriteMusic(new ILoaderRepository.LoadDataListener<MusicInfo>() { // from class: com.videoplayer.videox.pre.mus.MusicPresenter.1
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onError() {
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onSuccess(List<MusicInfo> list) {
                if (MusicPresenter.this.mView != null) {
                    ((MusVie) MusicPresenter.this.mView).openFavoriteMusic(list);
                }
            }
        });
    }

    public void searchMusicByMusicName(String str) {
        this.mMusicRepository.searchMusicByMusicName(new ILoaderRepository.LoadDataListener<MusicInfo>() { // from class: com.videoplayer.videox.pre.mus.MusicPresenter.2
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onError() {
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onSuccess(List<MusicInfo> list) {
                if (MusicPresenter.this.mView != null) {
                    ((MusVie) MusicPresenter.this.mView).onSearchMusic(list);
                }
            }
        }, str);
    }

    public void addMusicToPlaylist(MusicPlaylist musicPlaylist, List<MusicInfo> list) {
        this.mMusicRepository.updateMusicListForPlaylist(musicPlaylist, list);
    }
}
