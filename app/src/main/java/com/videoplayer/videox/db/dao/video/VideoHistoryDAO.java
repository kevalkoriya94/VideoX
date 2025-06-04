package com.videoplayer.videox.db.dao.video;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.videoplayer.videox.db.entity.video.VideoHistory;

import java.util.List;

@Dao
public abstract class VideoHistoryDAO {

    @Query("DELETE FROM VideoHistory")
    public abstract void deleteAllVideoHistory();

    @Query("DELETE FROM VideoHistory where videoId = :j")
    public abstract void deleteVideoHistoryById(long j);

    @Query("SELECT * FROM VideoHistory where mId = :j")
    public abstract VideoHistory getAHistoryVideo(long j);

    @Query("SELECT mDateAdded FROM VideoHistory where mId = :j")
    public abstract int getAVideoTimeData(long j);

    @Query("SELECT * FROM VideoHistory order by mDateAdded DESC")
    public abstract List<VideoHistory> getAllHistoryVideo();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertNewHistoryVideo(VideoHistory videoHistory);

    @Update
    public abstract void updateHistoryVideo(VideoHistory videoHistory);

    @Update
    public void updateVideoTimeData(long j, long j2) {
        VideoHistory aHistoryVideo = getAHistoryVideo(j);
        if (aHistoryVideo != null) {
            aHistoryVideo.setCurrentPosition(j2);
            updateHistoryVideo(aHistoryVideo);
        }
    }
}
