package com.videoplayer.videox.pre.mus;

import android.content.Context;

import com.videoplayer.videox.db.entity.music.MusicAlbum;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.repository.ILoaderRepository;
import com.videoplayer.videox.db.repository.MusicDataRepository;
import com.videoplayer.videox.pre.BasePre;
import com.videoplayer.videox.vie.mus.MusAlbVie;

import java.util.List;


public class MusAlbPre extends BasePre<MusAlbVie> {
    private final MusicDataRepository mMusicRepository;

    public MusAlbPre(Context context, MusAlbVie musicAlbumView, MusicDataRepository musicDataRepository) {
        super(musicAlbumView);
        this.mMusicRepository = musicDataRepository;
    }

    public void openAlbumTab() {
        this.mMusicRepository.getAllMusicAlbum(new ILoaderRepository.LoadDataListener<MusicAlbum>() { // from class: com.videoplayer.videox.pre.mus.MusAlbPre.1
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onError() {
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onSuccess(List<MusicAlbum> list) {
                if (MusAlbPre.this.mView != null) {
                    ((MusAlbVie) MusAlbPre.this.mView).onUpdateAlbum(list);
                }
            }
        });
    }

    public void getAllMusicOfAlbum(MusicAlbum musicAlbum) {
        this.mMusicRepository.getAllMusicOfAlbum(new ILoaderRepository.LoadDataListener<MusicInfo>() { // from class: com.videoplayer.videox.pre.mus.MusAlbPre.2
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onError() {
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onSuccess(List<MusicInfo> list) {
                if (MusAlbPre.this.mView != null) {
                    ((MusAlbVie) MusAlbPre.this.mView).onUpdateMusicList(list);
                }
            }
        }, musicAlbum);
    }
}
