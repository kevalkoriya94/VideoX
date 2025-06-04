package com.videoplayer.videox.db.entity.music;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.videoplayer.videox.db.database.MusicConverters;

import java.util.ArrayList;
import java.util.List;


@Entity
@TypeConverters({MusicConverters.class})
public class MusicPlaylist implements Parcelable {

    public static final Creator<MusicPlaylist> CREATOR = new Creator<MusicPlaylist>() {
        @Override
        public MusicPlaylist createFromParcel(Parcel parcel) {
            return new MusicPlaylist(parcel);
        }

        @Override
        public MusicPlaylist[] newArray(int i) {
            return new MusicPlaylist[i];
        }
    };

    @PrimaryKey(autoGenerate = true)
    public long Id;

    @ColumnInfo(name = "mDateAdded")
    private long mDateAdded;

    private List<MusicInfo> mMusicList;

    @ColumnInfo(name = "mPlaylistName")
    private String mPlaylistName;

    @Override
    public int describeContents() {
        return 0;
    }

    public MusicPlaylist() {
    }

    public MusicPlaylist(String str) {
        this.mPlaylistName = str;
    }

    protected MusicPlaylist(Parcel parcel) {
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
        return this.mPlaylistName;
    }

    public List<MusicInfo> getMusicList() {
        List<MusicInfo> list = this.mMusicList;
        return list == null ? new ArrayList() : list;
    }

    public void setMusicList(List<MusicInfo> list) {
        this.mMusicList = list;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mPlaylistName);
        parcel.writeLong(this.mDateAdded);
    }
}
