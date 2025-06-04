package com.videoplayer.videox.db.datasource;

import android.util.LongSparseArray;

import com.videoplayer.videox.db.entity.music.MusicInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MusicDatabaseControl {
    private static MusicDatabaseControl sInstance;
    private final LongSparseArray<MusicInfo> mSparseMusics = new LongSparseArray<>();
    private final Set<MusicInfo> mSetMusics = new HashSet();

    private MusicDatabaseControl() {
    }

    public static MusicDatabaseControl getInstance() {
        if (sInstance == null) {
            sInstance = new MusicDatabaseControl();
        }
        return sInstance;
    }

    public void addMusic(MusicInfo musicInfo) {
        this.mSparseMusics.put(musicInfo.getId(), musicInfo);
        this.mSetMusics.add(musicInfo);
    }

    public void clearAll() {
        this.mSparseMusics.clear();
        this.mSetMusics.clear();
    }

    public Set<MusicInfo> getAllMusics() {
        return this.mSetMusics;
    }

    public MusicInfo getMusicById(long j) {
        return this.mSparseMusics.get(j);
    }

    public void removeMusicById(long j) {
        this.mSetMusics.remove(this.mSparseMusics.get(j));
        this.mSparseMusics.remove(j);
    }

    public List<MusicInfo> searchMusicByMusicName(final String str) {
        ArrayList arrayList = new ArrayList();
        return (List) this.mSetMusics.stream().filter((Predicate) obj -> {
            boolean contains;
            contains = ((MusicInfo) obj).getDisplayName().toLowerCase().contains(str.trim().toLowerCase());
            return contains;
        }).collect(Collectors.toList());
    }

    public MusicInfo getMusicByFilePath(String str) {
        for (MusicInfo musicInfo : this.mSetMusics) {
            if (musicInfo.getPath().equals(str)) {
                return musicInfo;
            }
        }
        return null;
    }
}
