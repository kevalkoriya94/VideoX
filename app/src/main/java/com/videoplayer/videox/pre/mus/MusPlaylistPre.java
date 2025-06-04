package com.videoplayer.videox.pre.mus;

import android.content.Context;

import com.videoplayer.videox.db.entity.music.MusicPlaylist;
import com.videoplayer.videox.db.repository.ILoaderRepository;
import com.videoplayer.videox.db.repository.MusicDataRepository;
import com.videoplayer.videox.pre.BasePre;
import com.videoplayer.videox.vie.mus.MusPlylistVie;

import java.util.List;


public class MusPlaylistPre extends BasePre<MusPlylistVie> {
    private final MusicDataRepository mMusicRepository;

    public MusPlaylistPre(Context context, MusPlylistVie musicPlaylistView, MusicDataRepository musicDataRepository) {
        super(musicPlaylistView);
        this.mMusicRepository = musicDataRepository;
    }

    public void openPlaylistTab() {
        this.mMusicRepository.getAllPlaylistMusics(new ILoaderRepository.LoadDataListener<MusicPlaylist>() { // from class: com.videoplayer.videox.pre.mus.MusPlaylistPre.1
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onError() {
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onSuccess(List<MusicPlaylist> list) {
                if (MusPlaylistPre.this.mView != null) {
                    ((MusPlylistVie) MusPlaylistPre.this.mView).updateMusicPlaylist(list);
                }
            }
        });
    }

    public void createMusicPlaylist(String str) {
        final MusicPlaylist musicPlaylist = new MusicPlaylist();
        musicPlaylist.setPlaylistName(str.trim());
        musicPlaylist.setDateAdded(System.currentTimeMillis());
        this.mMusicRepository.createMusicPlaylist(new ILoaderRepository.InsertDataListener() { // from class: com.videoplayer.videox.pre.mus.MusPlaylistPre.2
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.InsertDataListener
            public void onSuccess() {
                if (MusPlaylistPre.this.mView != null) {
                    ((MusPlylistVie) MusPlaylistPre.this.mView).onCreatePlaylist(true, musicPlaylist);
                }
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.InsertDataListener
            public void onError() {
                if (MusPlaylistPre.this.mView != null) {
                    ((MusPlylistVie) MusPlaylistPre.this.mView).onCreatePlaylist(false, musicPlaylist);
                }
            }
        }, musicPlaylist);
    }

    public void deletePlaylist(MusicPlaylist musicPlaylist) {
        this.mMusicRepository.deletePlaylist(musicPlaylist);
    }

    public void duplicateMusicPlaylist(String str, MusicPlaylist musicPlaylist) {
        final MusicPlaylist musicPlaylist2 = new MusicPlaylist(str);
        musicPlaylist2.setDateAdded(System.currentTimeMillis());
        musicPlaylist2.setMusicList(musicPlaylist.getMusicList());
        this.mMusicRepository.duplicateMusicPlaylist(new ILoaderRepository.InsertDataListener() { // from class: com.videoplayer.videox.pre.mus.MusPlaylistPre.3
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.InsertDataListener
            public void onSuccess() {
                if (MusPlaylistPre.this.mView != null) {
                    ((MusPlylistVie) MusPlaylistPre.this.mView).onDuplicationPlaylist(musicPlaylist2);
                }
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.InsertDataListener
            public void onError() {
                if (MusPlaylistPre.this.mView != null) {
                    ((MusPlylistVie) MusPlaylistPre.this.mView).onDuplicationPlaylist(null);
                }
            }
        }, musicPlaylist2);
    }

    public void updatePlaylistName(MusicPlaylist musicPlaylist, final String str, final int i) {
        this.mMusicRepository.updatePlaylistName(new ILoaderRepository.InsertDataListener() { // from class: com.videoplayer.videox.pre.mus.MusPlaylistPre.4
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.InsertDataListener
            public void onSuccess() {
                if (MusPlaylistPre.this.mView != null) {
                    ((MusPlylistVie) MusPlaylistPre.this.mView).onUpdatePlaylistName(i, str, true);
                }
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.InsertDataListener
            public void onError() {
                if (MusPlaylistPre.this.mView != null) {
                    ((MusPlylistVie) MusPlaylistPre.this.mView).onUpdatePlaylistName(i, str, false);
                }
            }
        }, musicPlaylist, str);
    }
}
