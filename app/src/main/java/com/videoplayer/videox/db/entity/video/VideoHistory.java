package com.videoplayer.videox.db.entity.video;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class VideoHistory {

    @PrimaryKey(autoGenerate = true)
    public long mId;

    @ColumnInfo(name = "mCurrentPosition")
    public long mCurrentPosition;

    @ColumnInfo(name = "mDateAdded")
    public long mDateAdded;

    @Embedded
    public VideoInfo video;

    public VideoHistory() {
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public long getId() {
        return this.mId;
    }

    public VideoInfo getVideo() {
        return this.video;
    }

    public void setVideo(VideoInfo videoInfo) {
        this.video = videoInfo;
    }

    public void setCurrentPosition(long j) {
        this.mCurrentPosition = j;
    }

    public long getCurrentPosition() {
        return this.mCurrentPosition;
    }

    public long getDateAdded() {
        return this.mDateAdded;
    }

    public void setDateAdded(long j) {
        this.mDateAdded = j;
    }
}
