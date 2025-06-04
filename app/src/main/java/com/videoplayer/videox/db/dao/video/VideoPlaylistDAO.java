package com.videoplayer.videox.db.dao.video;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.entity.video.VideoPlaylist;

import java.util.List;

@Dao
public abstract class VideoPlaylistDAO {

    @Query("DELETE FROM VideoPlaylist Where mDateAdded = :j")
    public abstract void deletePlaylist(long j);

    @Query("SELECT * FROM VideoPlaylist")
    public abstract List<VideoPlaylist> getAllPlaylist();

    @Query("SELECT * FROM VideoPlaylist where mDateAdded = :j")
    public abstract VideoPlaylist getPlaylistByDateAdded(long j);

    @Query("SELECT mPlaylistName FROM VideoPlaylist where mPlaylistName = :str")
    public abstract String getPlaylistContainingSpecificName(String str);

    @Insert
    public abstract void insertNewPlaylist(VideoPlaylist videoPlaylist);

    @Update
    public abstract int updateVideoPlaylist(VideoPlaylist videoPlaylist);

    @Update
    public void updateVideoListForPlaylist(long j, List<VideoInfo> list) {
        VideoPlaylist playlistByDateAdded = getPlaylistByDateAdded(j);
        playlistByDateAdded.setVideoList(list);
        updateVideoPlaylist(playlistByDateAdded);
    }

    @Update
    public void updatePlaylistName(long j, String str) {
        VideoPlaylist playlistByDateAdded = getPlaylistByDateAdded(j);
        playlistByDateAdded.setPlaylistName(str);
        updateVideoPlaylist(playlistByDateAdded);
    }
}
