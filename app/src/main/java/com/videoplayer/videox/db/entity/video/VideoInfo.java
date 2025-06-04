package com.videoplayer.videox.db.entity.video;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class VideoInfo implements Serializable {
    @ColumnInfo(name = "mDateCreated")
    public long mDateCreated;
    @ColumnInfo(name = "mDisplayName")
    private String mDisplayName;
    @ColumnInfo(name = "mDuration")
    private long mDuration;

    @PrimaryKey
    private long videoId;
    @ColumnInfo(name = "mPath")
    private String mPath;
    @ColumnInfo(name = "mResolution")
    private String mResolution;
    @ColumnInfo(name = "mSize")
    private long mSize;
    @ColumnInfo(name = "mUri")
    private String mUri;
    @ColumnInfo(name = "mimeType")
    private String mimeType;

    public VideoInfo() {
    }

    public String getDisplayName() {
        String str = this.mDisplayName;
        return str == null ? "" : str;
    }

    public void setDisplayName(String str) {
        this.mDisplayName = str;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public void setDuration(long j) {
        this.mDuration = j;
    }

    public long getmDateCreated() {
        return mDateCreated;
    }

    public void setmDateCreated(long mDateCreated) {
        this.mDateCreated = mDateCreated;
    }

    public String getmDisplayName() {
        return mDisplayName;
    }

    public void setmDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public long getmDuration() {
        return mDuration;
    }

    public void setmDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    public long getVideoId() {
        return videoId;
    }

    public void setVideoId(long videoId) {
        this.videoId = videoId;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public String getmResolution() {
        return mResolution;
    }

    public void setmResolution(String mResolution) {
        this.mResolution = mResolution;
    }

    public long getmSize() {
        return mSize;
    }

    public void setmSize(long mSize) {
        this.mSize = mSize;
    }

    public String getmUri() {
        return mUri;
    }

    public void setmUri(String mUri) {
        this.mUri = mUri;
    }

    public String getPath() {
        String str = this.mPath;
        return str == null ? "" : str;
    }

    public void setPath(String str) {
        this.mPath = str;
    }

    public long getDate() {
        return this.mDateCreated;
    }

    public void setDate(long mDateCreated) {
        this.mDateCreated = mDateCreated;
    }

    public String getUri() {
        String str = this.mUri;
        return str == null ? "" : str;
    }

    public void setUri(String str) {
        this.mUri = str;
    }

    public String getMimeType() {
        String str = this.mimeType;
        return str == null ? "" : str;
    }

    public void setMimeType(String str) {
        this.mimeType = str;
    }

    public String getResolution() {
        String str = this.mResolution;
        return str == null ? "" : str;
    }

    public void setResolution(String str) {
        this.mResolution = str;
    }

    public long getSize() {
        return this.mSize;
    }

    public void setSize(long j) {
        this.mSize = j;
    }

    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == getClass() && ((VideoInfo) obj).getVideoId() == this.videoId;
    }

    @NonNull
    public String toString() {
        return "VideoInfo{Id=" + this.videoId + ", mPath='" + this.mPath + "', mDisplayName='" + this.mDisplayName + "', mDuration=" + this.mDuration + ", mDateCreated=" + this.mDateCreated + ", mUri='" + this.mUri + "', mimeType='" + this.mimeType + "', mResolution='" + this.mResolution + "', mSize=" + this.mSize + '}';
    }
}
