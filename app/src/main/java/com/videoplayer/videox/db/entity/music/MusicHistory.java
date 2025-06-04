package com.videoplayer.videox.db.entity.music;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MusicHistory {

    @PrimaryKey(autoGenerate = true)
    public long mId;

    @ColumnInfo(name = "mCurrentPosition")
    public long mCurrentPosition;

    @ColumnInfo(name = "mDateAdded")
    public long mDateAdded;

    @Embedded
    public MusicInfo musics;

    public MusicHistory() {
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public long getId() {
        return this.mId;
    }

    public MusicInfo getMusics() {
        return this.musics;
    }

    public void setMusic(MusicInfo musicInfo) {
        this.musics = musicInfo;
    }

    public void setCurrentPosition(long l2) {
        this.mCurrentPosition = l2;
    }

    public long getCurrentPosition() {
        return this.mCurrentPosition;
    }

    public void setDateAdded(long l2) {
        this.mDateAdded = l2;
    }

    public long getDateAdded() {
        return this.mDateAdded;
    }
}
