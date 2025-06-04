package com.videoplayer.videox.db.entity.video;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.videoplayer.videox.db.database.VideoConverters;

import java.util.ArrayList;
import java.util.List;


@Entity
@TypeConverters({VideoConverters.class})
public class VideoPlaylist implements Parcelable {

    public static final Creator<VideoPlaylist> CREATOR = new Creator<VideoPlaylist>() {
        @Override
        public VideoPlaylist createFromParcel(Parcel parcel) {
            return new VideoPlaylist(parcel);
        }

        @Override
        public VideoPlaylist[] newArray(int i) {
            return new VideoPlaylist[i];
        }
    };

    @PrimaryKey(autoGenerate = true)
    public long Id;

    @ColumnInfo(name = "mDateAdded")
    private long mDateAdded;

    @ColumnInfo(name = "mPlaylistName")
    private String mPlaylistName;

    //    @EmbeddedfromString
    private List<VideoInfo> mVideoList;

    @Override
    public int describeContents() {
        return 0;
    }

    public VideoPlaylist() {
    }

    public VideoPlaylist(String str) {
        this.mPlaylistName = str;
    }

    protected VideoPlaylist(Parcel parcel) {
        this.mPlaylistName = parcel.readString();
        this.mDateAdded = parcel.readLong();
    }

    public void setPlaylistName(String str) {
        this.mPlaylistName = str;
    }

    public long getDateAdded() {
        return this.mDateAdded;
    }

    public void setDateAdded(long j) {
        this.mDateAdded = j;
    }

    public String getPlaylistName() {
        String str = this.mPlaylistName;
        return str == null ? "" : str;
    }

    public List<VideoInfo> getVideoList() {
        List<VideoInfo> list = this.mVideoList;
        return list == null ? new ArrayList() : list;
    }

    public void setVideoList(List<VideoInfo> list) {
        this.mVideoList = list;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mPlaylistName);
        parcel.writeLong(this.mDateAdded);
    }
}
