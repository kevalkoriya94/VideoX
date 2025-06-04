package com.videoplayer.videox.db.dao.music;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.videoplayer.videox.db.entity.music.MusicHistory;

import java.util.List;

@Dao
public abstract class MusicHistoryDAO {

    @Query("DELETE FROM MusicHistory")
    public abstract void deleteAllMusicHistory();

    @Query("DELETE FROM MusicHistory where mId = :j")
    public abstract void deleteMusicHistoryById(long j);

    @Query("SELECT * FROM MusicHistory where mDateAdded = :j")
    public abstract MusicHistory getAHistoryMusic(int j);

    @Query("SELECT * FROM MusicHistory")
    public abstract List<MusicHistory> getAllHistoryMusic();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertNewHistoryMusic(MusicHistory musicHistory);

    @Update
    public abstract int updateHistoryMusic(MusicHistory musicHistory);

    @Update
    public void updateMusicTimeData(int j, int i) {
        MusicHistory aHistoryMusic = getAHistoryMusic(j);
        aHistoryMusic.setCurrentPosition(i);
        updateHistoryMusic(aHistoryMusic);
    }
}
