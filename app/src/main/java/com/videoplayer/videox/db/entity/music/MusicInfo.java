package com.videoplayer.videox.db.entity.music;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity
public class MusicInfo implements Serializable {
    @PrimaryKey
    public long Id;
    @ColumnInfo(name = "mAlbum")
    private String mAlbum;
    @ColumnInfo(name = "mArtist")
    private String mArtist;
    @ColumnInfo(name = "mDateCreated")
    public long mDateCreated;
    @ColumnInfo(name = "mDisplayName")
    private String mDisplayName;
    @ColumnInfo(name = "mDuration")
    private long mDuration;

    @ColumnInfo(name = "mPath")
    private String mPath;
    @ColumnInfo(name = "mSize")
    private long mSize;
    @ColumnInfo(name = "uri")
    private String uri;

    public MusicInfo() {
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

    public long getId() {
        return this.Id;
    }

    public void setId(long j) {
        this.Id = j;
    }

    public long getDate() {
        return this.mDateCreated;
    }

    public void setDate(long mDateCreated) {
        this.mDateCreated = mDateCreated;
    }

    public String getPath() {
        String str = this.mPath;
        return str == null ? "" : str;
    }

    public String getUri() {
        String str = this.uri;
        return str == null ? "" : str;
    }

    public void setUri(String str) {
        this.uri = str;
    }

    public void setPath(String str) {
        this.mPath = str;
    }

    public void setArtist(String str) {
        this.mArtist = str;
    }

    public String getArtist() {
        return TextUtils.isEmpty(this.mArtist) ? "<unknown>" : this.mArtist;
    }

    public String getAlbum() {
        return TextUtils.isEmpty(this.mAlbum) ? "<unknown>" : this.mAlbum;
    }

    public void setAlbum(String str) {
        this.mAlbum = str;
    }

    public long getSize() {
        return this.mSize;
    }

    public void setSize(long j) {
        this.mSize = j;
    }

    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == getClass() && ((MusicInfo) obj).getId() == this.Id;
    }

    @NonNull
    public String toString() {
        return "MusicInfo{Id=" + this.Id + ", mDisplayName='" + this.mDisplayName + "', mDuration=" + this.mDuration + ", mDateAdded=" + this.mDateCreated + ", mPath='" + this.mPath + "', mArtist='" + this.mArtist + "', mAlbum='" + this.mAlbum + "', mSize=" + this.mSize + '}';
    }
}
