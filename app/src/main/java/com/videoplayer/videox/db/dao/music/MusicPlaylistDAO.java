package com.videoplayer.videox.db.dao.music;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.entity.music.MusicPlaylist;

import java.util.List;

@Dao
public abstract class MusicPlaylistDAO {

    @Query("DELETE FROM MusicPlaylist where mDateAdded = :j")
    public abstract void deletePlaylist(long j);

    @Query("SELECT * FROM MusicPlaylist")
    public abstract List<MusicPlaylist> getAllPlaylist();

    @Query("SELECT * FROM MusicPlaylist where mDateAdded = :j")
    public abstract MusicPlaylist getPlaylistByDateAdded(long j);

    @Query("SELECT mPlaylistName FROM MusicPlaylist where mPlaylistName = :str")
    public abstract String getPlaylistContainingSpecificName(String str);

    @Insert
    public abstract void insertNewPlaylist(MusicPlaylist musicPlaylist);

    @Update
    public abstract int updateVideoPlaylist(MusicPlaylist musicPlaylist);

    @Update
    public void updateMusicListForPlaylist(long j, List<MusicInfo> list) {
        MusicPlaylist playlistByDateAdded = getPlaylistByDateAdded(j);
        playlistByDateAdded.setMusicList(list);
        updateVideoPlaylist(playlistByDateAdded);
    }

    @Update
    public void updatePlaylistName(long j, String str) {
        MusicPlaylist playlistByDateAdded = getPlaylistByDateAdded(j);
        playlistByDateAdded.setPlaylistName(str);
        updateVideoPlaylist(playlistByDateAdded);
    }
}
